package com.khater.rwaq.presentation.screens.orderScreen.uiState

sealed interface OrderUiEffect {
    data object NavigateBack : OrderUiEffect
}