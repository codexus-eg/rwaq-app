package com.khater.rwaq.presentation.screens.loginScreen.uiState

sealed interface LoginUiEffect {
    data object NavigateBack: LoginUiEffect
    data class NavigateToOtpScreen(val phoneNumber: String) : LoginUiEffect
    data object NavigateToForgotPassword : LoginUiEffect
   // data class ShowSnackBarError(val errorStringResource: StringResource) : LoginUiEffect
}