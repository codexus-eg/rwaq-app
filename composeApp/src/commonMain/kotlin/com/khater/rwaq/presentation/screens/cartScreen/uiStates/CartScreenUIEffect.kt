package com.khater.rwaq.presentation.screens.cartScreen.uiStates

import com.khater.rwaq.presentation.screens.branchScreen.uiState.LocationUiState

sealed interface CartScreenUIEffect {
    data object NavigateBack: CartScreenUIEffect
    data class NavigateToExternalMap(val location: LocationUiState) : CartScreenUIEffect
    data object NavigateToLogin : CartScreenUIEffect
    data class NavigateToPayment(val clientSecret: String, val publicKey: String) : CartScreenUIEffect
}