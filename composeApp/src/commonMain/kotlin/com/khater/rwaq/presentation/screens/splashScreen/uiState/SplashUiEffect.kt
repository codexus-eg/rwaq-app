package com.khater.rwaq.presentation.screens.splashScreen.uiState

sealed interface SplashUiEffect {
    data object NavigateToHome: SplashUiEffect
    data object NavigateToOnBoarding: SplashUiEffect
}