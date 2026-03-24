package com.khater.rwaq.presentation.screens.cartScreen.uiStates

import com.khater.rwaq.presentation.screens.branchScreen.uiState.BranchScreenUiEffect
import com.khater.rwaq.presentation.screens.branchScreen.uiState.LocationUiState

interface CartScreenUIEffect {
    data object NavigateBack: CartScreenUIEffect
    data class NavigateToExternalMap(val location: LocationUiState) : CartScreenUIEffect

}