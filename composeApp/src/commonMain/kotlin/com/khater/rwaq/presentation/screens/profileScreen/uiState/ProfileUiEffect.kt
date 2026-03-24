package com.khater.rwaq.presentation.screens.profileScreen.uiState

sealed interface ProfileUiEffect {
    data object NavigateToSplash: ProfileUiEffect
    data object NavigateBack: ProfileUiEffect
    data object NavigateToBranches : ProfileUiEffect
    data object NavigateToPrivacyPolicy : ProfileUiEffect
    data object NavigateToContactUs : ProfileUiEffect
    data object NavigateToOrders : ProfileUiEffect
    data object NavigateToUpdateUser : ProfileUiEffect
    data object ShareApp : ProfileUiEffect

}