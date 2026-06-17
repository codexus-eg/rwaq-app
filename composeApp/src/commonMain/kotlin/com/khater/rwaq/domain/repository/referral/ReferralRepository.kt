package com.khater.rwaq.domain.repository.referral

import com.khater.rwaq.domain.entities.referral.ReferralClaimResult
import com.khater.rwaq.domain.entities.referral.ReferralInfo
import com.khater.rwaq.domain.entities.referral.ReferralListItem

interface ReferralRepository {
    // Submit a referral code for the currently authenticated user. Must be called
    // AFTER login/signup (the request is authenticated; the server identifies the
    // referred user from the token, never from the request body).
    suspend fun claimReferral(referralCode: String, deviceId: String): ReferralClaimResult

    // The current user's referral code, share link and earned stats.
    suspend fun getMyReferralInfo(): ReferralInfo

    // Everyone the given user has referred.
    suspend fun getReferrals(userId: String): List<ReferralListItem>
}
