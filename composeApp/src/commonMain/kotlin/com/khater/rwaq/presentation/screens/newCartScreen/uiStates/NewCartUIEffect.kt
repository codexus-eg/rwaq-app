package com.khater.rwaq.presentation.screens.newCartScreen.uiStates

import com.khater.rwaq.domain.entities.cart.CheckoutResult
import com.khater.rwaq.presentation.screens.branchScreen.uiState.LocationUiState
import com.khater.rwaq.presentation.screens.cartScreen.uiStates.CartScreenUIEffect

sealed interface NewCartUIEffect {
    data class NavigateToCheckout(val checkoutResult: CheckoutResult) : NewCartUIEffect
    data class ShowError(val message: String) : NewCartUIEffect
    data class NavigateToPayment(val clientSecret: String, val publicKey: String) : NewCartUIEffect
    data object NavigateBack : NewCartUIEffect
    data object NavigateToOrders : NewCartUIEffect
    data object NavigateToLogin : NewCartUIEffect
    data class NavigateToExternalMap(val location: LocationUiState) : NewCartUIEffect
    data object OpenLocationSettings : NewCartUIEffect

}
