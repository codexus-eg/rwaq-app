package com.khater.rwaq.presentation.util

import co.touchlab.kermit.Logger
import com.khater.rwaq.data.source.local.setting.pendingReferralCode
import com.khater.rwaq.data.source.local.setting.referralDeviceId
import com.khater.rwaq.domain.entities.referral.ReferralClaimResult
import com.khater.rwaq.domain.repository.referral.ReferralRepository
import com.khater.rwaq.domain.util.NoNetworkException
import com.khater.rwaq.domain.util.UnAuthorizedException
import com.russhwolf.settings.Settings
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// Platform bridge that reads a referral identifier left behind by the install /
// deep-link flow:
//   • Android → Play Store install referrer (InstallReferrerClient)
//   • iOS     → NSUserDefaults value written by the Swift AppDelegate
// It returns the RAW payload (e.g. "ref=ABC123"); the manager parses the code.
expect class ReferralHelper {
    fun fetchReferral(onResult: (String?) -> Unit)
}

// Orchestrates the two-phase referral flow described in the spec:
//
//   1. CAPTURE (on app open): read any referral code left by the install/deep
//      link and store it locally as the *pending* code. We do NOT reward here.
//
//   2. CLAIM (after login/signup success): if a pending code exists, submit it
//      to the backend, which validates and awards points. The pending code is
//      cleared once the server reaches a terminal decision (success, or a
//      permanent rejection such as self-referral / already-referred) so we never
//      retry a code that can never succeed. Transient failures (not logged in
//      yet, no network) keep the code for the next attempt.
class ReferralManager(
    private val helper: ReferralHelper,
    private val referralRepository: ReferralRepository,
    private val settings: Settings
) {
    // Phase 1 — capture. Suspends until the platform callback returns. Stores the
    // parsed code as pending (without overwriting one already waiting to be
    // claimed) and returns it for diagnostics. Safe to call on every app open.
    suspend fun capturePendingReferral(): String? = suspendCoroutine { continuation ->
        // Guard against the platform invoking the callback more than once (e.g.
        // InstallReferrer "setup finished" then "disconnected"), which would
        // otherwise resume the continuation twice and crash.
        var resumed = false
        helper.fetchReferral { raw ->
            if (resumed) return@fetchReferral
            resumed = true
            val code = parseReferralCode(raw)
            Logger.i { "[Referral] captured raw=$raw -> code=$code" }
            if (!code.isNullOrBlank() && settings.pendingReferralCode.isBlank()) {
                settings.pendingReferralCode = code
            }
            continuation.resume(code)
        }
    }

    // Phase 2 — claim. No-op (returns null) when there's nothing pending. Must be
    // called only when the user is authenticated.
    suspend fun claimPendingReferral(): ReferralClaimResult? {
        val code = settings.pendingReferralCode
        if (code.isBlank()) return null

        return try {
            val result = referralRepository.claimReferral(
                referralCode = code,
                deviceId = settings.referralDeviceId
            )
            // Terminal success → stop tracking it.
            settings.pendingReferralCode = ""
            Logger.i { "[Referral] claim success: +${result.pointsAwarded} pts (alreadyClaimed=${result.alreadyClaimed})" }
            result
        } catch (e: UnAuthorizedException) {
            // Not authenticated yet — keep the code and retry after login.
            Logger.i { "[Referral] claim deferred: not authenticated" }
            null
        } catch (e: NoNetworkException) {
            // Transient — keep the code and retry later.
            Logger.i { "[Referral] claim deferred: no network" }
            null
        } catch (e: Exception) {
            // Permanent rejection (already referred, self-referral, invalid code,
            // programme disabled). Clear so we don't retry forever.
            Logger.i { "[Referral] claim rejected permanently: ${e.message}" }
            settings.pendingReferralCode = ""
            null
        }
    }
}
