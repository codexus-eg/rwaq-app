package com.khater.rwaq.presentation.screens.branchesScreen.uiState

import com.khater.rwaq.presentation.screens.branchScreen.uiState.LocationUiState

interface BranchesScreenUiEffect {
    data class NavigateToExternalMap(val location: LocationUiState) : BranchesScreenUiEffect
    data object NavigateBack : BranchesScreenUiEffect
}