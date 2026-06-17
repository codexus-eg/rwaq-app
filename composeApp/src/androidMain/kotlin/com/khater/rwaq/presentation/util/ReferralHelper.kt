package com.khater.rwaq.presentation.util

import android.content.Context
import co.touchlab.kermit.Logger
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual class ReferralHelper(private val context: Context) {

    actual fun fetchReferral(onResult: (String?) -> Unit) {
        val client = InstallReferrerClient.newBuilder(context).build()

        client.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                if (responseCode == InstallReferrerClient.InstallReferrerResponse.OK) {
                    val response: ReferrerDetails = client.installReferrer
                    // Raw install referrer set by the Play Store, e.g. "ref=ABC123"
                    // (or the legacy "userId=..."). The common ReferralManager
                    // parses the actual code out of it via parseReferralCode().
                    val raw = response.installReferrer
                    Logger.i { "[Referral] install referrer raw=$raw" }
                    onResult(raw)
                } else {
                    onResult(null)
                }
                client.endConnection()
            }
            override fun onInstallReferrerServiceDisconnected() {
                onResult(null)
            }
        })
    }
}

val referralAndroidModule = module {
    factory { ReferralHelper(context = androidContext()) }
}