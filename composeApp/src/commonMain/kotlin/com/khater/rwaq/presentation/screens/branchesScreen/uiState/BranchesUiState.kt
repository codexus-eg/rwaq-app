package com.khater.rwaq.presentation.screens.branchesScreen.uiState

import com.khater.rwaq.presentation.model.SnackBarState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.BranchUiState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.BranchWorkTimeUiState

data class BranchesUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val snackBar: SnackBarState = SnackBarState(),
    val branches: List<BranchUiState> = emptyList(),
    val isWorkTimeOverlayVisible: Boolean = false,
    val selectedWorkTime: List<BranchWorkTimeUiState> = emptyList(),

    )
