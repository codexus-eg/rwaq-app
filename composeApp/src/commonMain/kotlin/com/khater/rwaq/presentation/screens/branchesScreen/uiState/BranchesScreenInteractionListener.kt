package com.khater.rwaq.presentation.screens.branchesScreen.uiState

import androidx.compose.ui.graphics.Color
import com.khater.rwaq.presentation.screens.branchScreen.uiState.CarBrandUiState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.CarUiState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.LocationUiState

interface BranchesScreenInteractionListener {
    fun onBack()
    fun onRetry()
     fun onClickLocationButton(location: LocationUiState)
    fun onClickWorkTimeButton(branchId: String)
    fun onCloseWorkTimeBottomSheet()
}