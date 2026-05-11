package com.khater.rwaq.presentation.screens.rewardScreen.uiState

sealed interface RewardsUiEffect {

    data class NavigateToProductDetails(val productId: String): RewardsUiEffect
    data object NavigateBack: RewardsUiEffect
    data object NavigateToLogin: RewardsUiEffect
}