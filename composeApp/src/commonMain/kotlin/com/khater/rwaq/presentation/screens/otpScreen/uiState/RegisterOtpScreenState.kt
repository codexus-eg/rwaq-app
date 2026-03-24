package com.khater.rwaq.presentation.screens.otpScreen.uiState

import com.khater.rwaq.presentation.model.SnackBarState


data class RegisterOtpScreenState(
    val otpValue: String = "",
    val isVerifyEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val isResendEnabled: Boolean = true,
    val phoneNumber: String = "",
    val snackBar: SnackBarState = SnackBarState(),
    val timer: String = "",
)
