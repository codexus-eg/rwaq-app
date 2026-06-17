package com.khater.rwaq.presentation.util

import co.touchlab.kermit.Logger
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

actual class ReferralHelper {

    actual fun fetchReferral(onResult: (String?) -> Unit) {
        val userDefaults = NSUserDefaults.standardUserDefaults

        // Current key written by the Swift AppDelegate ("ref=CODE" / "CODE"),
        // with a fallback to the legacy key for older installs.
        val newKey = "pending_referral_code"
        val legacyKey = "pending_referral_id"

        val raw = userDefaults.stringForKey(newKey) ?: userDefaults.stringForKey(legacyKey)

        Logger.i { "[Referral] iOS pending referral raw=$raw" }
        if (raw != null) {
            onResult(raw)
            // Clear both so it isn't processed again next launch.
            userDefaults.removeObjectForKey(newKey)
            userDefaults.removeObjectForKey(legacyKey)
        } else {
            onResult(null)
        }
    }
}
val referralIosModule = module {
    factory { ReferralHelper() }
}