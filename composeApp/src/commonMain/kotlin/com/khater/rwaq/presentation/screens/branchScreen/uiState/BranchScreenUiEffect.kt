package com.khater.rwaq.presentation.screens.branchScreen.uiState

import androidx.compose.ui.graphics.Color

interface BranchScreenUiEffect {
    data class NavigateToProductsScreen(val branchId: String,val isPickupFromBranch: Boolean, val selectedCar: CarUiState?=null,val branchName: String ) : BranchScreenUiEffect
    data class NavigateToExternalMap(val location: LocationUiState) : BranchScreenUiEffect
    data object NavigateBack: BranchScreenUiEffect
}