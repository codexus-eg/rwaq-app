// commonMain/presentation/payment/PaymobNativeBridge.kt

package com.khater.rwaq.presentation.payment
// commonMain/presentation/payment/PaymobNativeBridge.kt

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

// Separate callbacks interface avoids Kotlin lambda ↔ ObjC block headaches
// interface PaymobCallbacks {
//    fun onSuccess()
//    fun onFailure(message: String?)
//    fun onPending()
//}
//
// interface PaymobNativeLauncher {
//    fun start(clientSecret: String, publicKey: String, callbacks: PaymobCallbacks)
//}
//
//// Swift sets this once at startup — Kotlin reads it on every launch
// object PaymobRegistry {
//    var launcher: PaymobNativeLauncher? = null
//}