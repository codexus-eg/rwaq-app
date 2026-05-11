package com.khater.rwaq.presentation.payment

import androidx.compose.runtime.Composable

interface PaymobLauncher {
    fun launch(clientSecret: String, publicKey: String)
}

@Composable
expect fun rememberPaymobLauncher(
    onSuccess: () -> Unit,
    onFailure: (String?) -> Unit,
    onPending: () -> Unit,
): PaymobLauncher