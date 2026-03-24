package com.khater.rwaq.presentation.screens.productScreen.uiState


interface ProductScreenUiEffect {
    data object NavigateToProductDetailsScreen : ProductScreenUiEffect
    data object NavigateBack : ProductScreenUiEffect
}