package com.khater.rwaq.presentation.payment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

// iosMain/payment/PaymobLauncher.ios.kt

@Composable
actual fun rememberPaymobLauncher(
    onSuccess: () -> Unit,
    onFailure: (String?) -> Unit,
    onPending: () -> Unit,
): PaymobLauncher = remember {


    object : PaymobLauncher {
        override fun launch(clientSecret: String, publicKey: String) {
            val launcher = PaymobRegistry.launcher
            if (launcher == null) {
                onFailure("PaymobRegistry.launcher not set — did you register in iOSApp.swift?")
                return
            }

            launcher.start(
                clientSecret = clientSecret,
                publicKey = publicKey,
                callbacks = object : PaymobCallbacks {
                    override fun onSuccess() = onSuccess()
                    override fun onFailure(message: String?) = onFailure(message)
                    override fun onPending() = onPending()
                }
            )
        }
    }
}


interface PaymobCallbacks {
    fun onSuccess()
    fun onFailure(message: String?)
    fun onPending()
}

interface PaymobNativeLauncher {
    fun start(clientSecret: String, publicKey: String, callbacks: PaymobCallbacks)
}

// Swift sets this once at startup — Kotlin reads it on every launch
object PaymobRegistry {
    var launcher: PaymobNativeLauncher? = null
}