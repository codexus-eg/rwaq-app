package com.khater.rwaq.presentation.screens.contactUsScreen.uiState

sealed interface ContactUsUiEffect {
    data object NavigateBack : ContactUsUiEffect
 }