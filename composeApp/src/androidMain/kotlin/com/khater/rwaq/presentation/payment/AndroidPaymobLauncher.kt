package com.khater.rwaq.presentation.payment

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import co.touchlab.kermit.Logger
import com.khater.rwaq.R
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.paymob.paymob_sdk.PaymobSdk
import com.paymob.paymob_sdk.ui.PaymobSdkListener
import com.paymob.paymob_sdk.ui.embedded.PaymobSdkListenerCallback

// androidMain/payment/PaymobLauncher.android.kt

private class AndroidPaymobLauncher(
    private val activity: ComponentActivity,
    private val onPaymentSuccess: () -> Unit,
    private val onPaymentFailure: (String?) -> Unit,
    private val onPaymentPending: () -> Unit,
) : PaymobLauncher, PaymobSdkListener {

    private var sdk: PaymobSdk? = null

    override fun launch(clientSecret: String, publicKey: String) {
        sdk = PaymobSdk.Builder(
            context = activity,
            clientSecret = clientSecret,
            publicKey = publicKey,
            paymobSdkListener = this,
        )
            .setButtonBackgroundColor(Color(0xFFDBAB6D).toArgb())
            .setAppName("Rwaq")
            .setAppLogo(R.drawable.rwaq_logo)
            .showSaveCard(true)
            .saveCardByDefault(true)
            .build()

        sdk?.start()
    }

    override fun onSuccess(payResponse: HashMap<String, String?>) = onPaymentSuccess()
    override fun onFailure(msg: String?) = onPaymentFailure(msg)
    override fun onPending() = onPaymentPending()

}

@Composable
actual fun rememberPaymobLauncher(
    onSuccess: () -> Unit,
    onFailure: (String?) -> Unit,
    onPending: () -> Unit,
): PaymobLauncher {
    val activity = LocalActivity.current as ComponentActivity
    return remember(activity) {
        AndroidPaymobLauncher(activity, onSuccess, onFailure, onPending)
    }
}