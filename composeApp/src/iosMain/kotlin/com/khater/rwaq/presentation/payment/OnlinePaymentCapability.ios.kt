package com.khater.rwaq.presentation.payment

import platform.PassKit.PKPaymentAuthorizationController

actual object OnlinePaymentCapability {
    actual val shouldShowApplePayOption: Boolean = true
    actual val isApplePaySupported: Boolean
        get() = PKPaymentAuthorizationController.canMakePayments()
}
