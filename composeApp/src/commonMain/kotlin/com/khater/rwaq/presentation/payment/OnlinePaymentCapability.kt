package com.khater.rwaq.presentation.payment

expect object OnlinePaymentCapability {
    val shouldShowApplePayOption: Boolean
    val isApplePaySupported: Boolean
}
