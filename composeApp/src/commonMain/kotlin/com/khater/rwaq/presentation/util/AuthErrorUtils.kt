package com.khater.rwaq.presentation.util

import com.khater.rwaq.domain.util.UnAuthorizedException

fun Throwable.isLoginRequiredError(): Boolean {
    val rawMessage = message.orEmpty()
    return this is UnAuthorizedException ||
        rawMessage.contains("Refresh token required", ignoreCase = true) ||
        rawMessage.contains("User has no access", ignoreCase = true) ||
        rawMessage.contains("Unauthorized", ignoreCase = true)
}

fun Throwable.isOnlinePaymentCheckoutError(): Boolean {
    val rawMessage = message.orEmpty()
    return rawMessage.contains("Failed to create payment intention", ignoreCase = true) ||
        rawMessage.contains("payment intention", ignoreCase = true)
}
