package com.khater.rwaq.presentation.screens.otpScreen.uiState

sealed interface RegisterOtpUiEffect {
    data object NavigateBack: RegisterOtpUiEffect
    data object NavigateToHome: RegisterOtpUiEffect
    data class NavigateToResetPassword(val phoneNumber: String,val otp: String): RegisterOtpUiEffect
}