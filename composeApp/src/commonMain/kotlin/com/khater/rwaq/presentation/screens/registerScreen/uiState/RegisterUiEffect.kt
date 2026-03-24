package com.khater.rwaq.presentation.screens.registerScreen.uiState

sealed interface RegisterUiEffect {
    data class NavigateToOTP(val phoneNumber: String): RegisterUiEffect
    data object NavigateBack: RegisterUiEffect
}