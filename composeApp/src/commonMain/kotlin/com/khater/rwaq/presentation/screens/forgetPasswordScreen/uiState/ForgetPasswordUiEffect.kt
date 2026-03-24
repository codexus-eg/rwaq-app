package com.khater.rwaq.presentation.screens.forgetPasswordScreen.uiState

sealed interface ForgetPasswordUiEffect {
  data object NavigateBack : ForgetPasswordUiEffect
  data class NavigateToOtpScreen(val phoneNumber: String) : ForgetPasswordUiEffect
}