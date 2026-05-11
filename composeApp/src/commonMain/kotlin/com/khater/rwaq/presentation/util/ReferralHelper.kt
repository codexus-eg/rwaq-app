package com.khater.rwaq.presentation.util

import co.touchlab.kermit.Logger
import com.khater.rwaq.data.source.local.setting.referralChecked
import com.russhwolf.settings.Settings

expect class ReferralHelper {
      fun fetchReferral(onResult: (String?) -> Unit)
}

// commonMain/ReferralManager.kt
class ReferralManager(
    private val helper: ReferralHelper,
   // private val api: YourApiClient,
    private val settings: Settings
) {
    fun checkAndSubmitReferral() : String{
        Logger.i { "rawData ${settings.referralChecked}" }
       // if (settings.referralChecked) return

        var userId = ""
        helper.fetchReferral { referrerId ->
             Logger.i { "rawData $referrerId" }
            print("rawData $referrerId")
            userId = referrerId ?: ""
            settings.referralChecked = true
            if (referrerId != null) {
        //        api.submitReferral(referrerId)
            }
        }
        return userId
     }
}