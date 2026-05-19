package com.khater.rwaq.presentation.screens.homeScreen.uiStates

sealed interface HomeUiEffect {
    data object NavigateToPickUpFromBranch: HomeUiEffect
    data object NavigateToPickUpFromCar: HomeUiEffect
    data class NavigateToProductDetails(val productId: String): HomeUiEffect

    data class ScrollToCategory(val index: Int) : HomeUiEffect
}
