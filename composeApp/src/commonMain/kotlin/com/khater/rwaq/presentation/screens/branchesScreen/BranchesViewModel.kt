package com.khater.rwaq.presentation.screens.branchesScreen

import co.touchlab.kermit.Logger
import com.khater.rwaq.domain.entities.branch.Branch
import com.khater.rwaq.domain.useCases.GetAllBranchesUseCase
import com.khater.rwaq.presentation.base.BaseViewModel
import com.khater.rwaq.presentation.mapper.toUi
import com.khater.rwaq.presentation.screens.branchScreen.uiState.LocationUiState
import com.khater.rwaq.presentation.screens.branchesScreen.uiState.BranchesScreenInteractionListener
import com.khater.rwaq.presentation.screens.branchesScreen.uiState.BranchesScreenUiEffect
import com.khater.rwaq.presentation.screens.branchesScreen.uiState.BranchesUiState

class BranchesViewModel(
    private val getAllBranchesUseCase: GetAllBranchesUseCase,
    )
    : BaseViewModel<BranchesUiState, BranchesScreenUiEffect>(BranchesUiState()),BranchesScreenInteractionListener {


        init {
            getAllBranches()
        }
    override fun onBack() {
        sendNewEffect(newEffect = BranchesScreenUiEffect.NavigateBack)
    }

    override fun onRetry() {
        getAllBranches()
    }


    private fun getAllBranches() {
        tryToExecute(
            callee = { getAllBranchesUseCase() },
            onSuccess = ::onGetAllBranchesSuccess,
            onError = ::onGetAllBranchesError,
            onStart = {
                updateState { it.copy(isLoading = true, errorMessage = null) }
            }
        )
    }
    private fun onGetAllBranchesSuccess(branches: List<Branch>) {
        Logger.i { "aevaevnaev $branches" }
        updateState {
            it.copy(
                isLoading = false,
                errorMessage = null,
                branches = branches.map { branch ->
                    branch.toUi()
                },

            )
        }
    }

    private fun onGetAllBranchesError(throwable: Throwable) {
        updateState { it.copy(isLoading = false, errorMessage = throwable.message) }
    }
    override fun onClickLocationButton(location: LocationUiState) {
        sendNewEffect(
            BranchesScreenUiEffect.NavigateToExternalMap(location)
        )
    }

    override fun onClickWorkTimeButton(branchId: String) {
        val branch = state.value.branches.first { it.id == branchId }
        updateState { it.copy(isWorkTimeOverlayVisible = true, selectedWorkTime = branch.workTime) }    }

    override fun onCloseWorkTimeBottomSheet() {
        updateState { it.copy(isWorkTimeOverlayVisible = false) }

    }
}