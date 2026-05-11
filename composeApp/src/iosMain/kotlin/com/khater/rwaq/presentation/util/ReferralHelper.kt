package com.khater.rwaq.presentation.util

import co.touchlab.kermit.Logger
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

actual class ReferralHelper {

    actual fun fetchReferral(onResult: (String?) -> Unit) {
        val userDefaults = NSUserDefaults.standardUserDefaults
        val key = "pending_referral_id"

        // Read the value saved by the Swift AppDelegate
        val userId = userDefaults.stringForKey(key)

        Logger.i { "userId $userId" }
        print( "userId $userId")
        if (userId != null && userId.startsWith("refer")) {
            // Send the result back
            onResult(userId)

            // Clear the referral ID so it isn't triggered again next time the app opens
            userDefaults.removeObjectForKey(key)
        } else {
            onResult(null)
        }
    }
}
val referralIosModule = module {
    factory { ReferralHelper() }
}