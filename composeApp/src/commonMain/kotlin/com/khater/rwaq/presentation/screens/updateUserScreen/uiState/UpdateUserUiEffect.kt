package com.khater.rwaq.presentation.screens.updateUserScreen.uiState

sealed interface UpdateUserUiEffect {
    data object NavigateBack : UpdateUserUiEffect
 }