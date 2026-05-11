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
                    val raw = response.installReferrer  // "userId=123"
                    // here i will check for start with refer and get the userId and i'll update the backend
                    Logger.i { "rawData $raw" }
                    val userId = raw
                        ?.split("&")
                        ?.firstOrNull { it.startsWith("userId=") }
                        ?.removePrefix("userId=")
                    onResult(userId)
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