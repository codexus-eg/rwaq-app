package com.khater.rwaq.presentation.screens.resetPasswordScreen.uiState

sealed interface ResetPasswordUiEffect {
    data object NavigateToHome: ResetPasswordUiEffect
    data object NavigateBack: ResetPasswordUiEffect
}