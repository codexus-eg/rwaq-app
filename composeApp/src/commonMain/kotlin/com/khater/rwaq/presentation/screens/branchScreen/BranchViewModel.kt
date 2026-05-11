package com.khater.rwaq.presentation.screens.branchScreen

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Logger.Companion.v
import com.khater.rwaq.domain.entities.branch.Branch
import com.khater.rwaq.domain.entities.branch.Location
import com.khater.rwaq.domain.entities.car.Car
import com.khater.rwaq.domain.useCases.GetAllBranchesUseCase
import com.khater.rwaq.presentation.base.BaseViewModel
import com.khater.rwaq.presentation.mapper.toUi
import com.khater.rwaq.presentation.model.SnackBarState
import com.khater.rwaq.presentation.navigation.Screen
import com.khater.rwaq.presentation.screens.branchScreen.uiState.AddCarStep
import com.khater.rwaq.presentation.screens.branchScreen.uiState.BranchScreenInteractionListener
import com.khater.rwaq.presentation.screens.branchScreen.uiState.BranchScreenUiEffect
import com.khater.rwaq.presentation.screens.branchScreen.uiState.BranchScreenUiState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.CarBrandUiState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.CarUiState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.LocationUiState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.toUiState
import com.khater.rwaq.presentation.screens.cartScreen.uiStates.CarColor
import com.khater.rwaq.presentation.util.LoginConstants.SNACK_BAR_DELAY
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.choose_car
import rwaq.composeapp.generated.resources.please_choose_car
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class BranchViewModel(
    private val getAllBranchesUseCase: GetAllBranchesUseCase,
     savedStateHandle: SavedStateHandle,
) :
    BaseViewModel<BranchScreenUiState, BranchScreenUiEffect>(BranchScreenUiState()),
    BranchScreenInteractionListener {

    val args = savedStateHandle.toRoute<Screen.BranchScreen>()

    init {
        updateState { it.copy(isDriveThru = !args.isPickupFormBranch) }
        getAllBranches()
        collectAllCars()
    }

    private fun collectAllCars() {
        tryToCollect(
            collect = { getAllBranchesUseCase.getAllCars() },
            onCollect = ::onCarsCollected,
            onError = { Logger.e("Failed to collect cars: ${it.message}") }
        )
    }

    private fun onCarsCollected(cars: List<Car>) {
        val carsUi = cars.map { it.toUiState() }
        val currentSelectedId = state.value.selectedCar.id

        // Logic: Try to keep the currently selected car.
        // If it was deleted (or list was empty before), select the first available car.
        val newSelectedCar = carsUi.find { it.id == currentSelectedId }
            ?: carsUi.firstOrNull()
            ?: CarUiState() // Default empty state

        updateState {
            it.copy(
                cars = carsUi,
                selectedCar = newSelectedCar
            )
        }
    }
    override fun onBack() {
        sendNewEffect(
            BranchScreenUiEffect.NavigateBack
        )
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
        val newBranches = if (args.isPickupFormBranch) branches else
            branches.filter { branch -> branch.isBranchAvailableForDriveThru }
        updateState {
            it.copy(
                isLoading = false,
                errorMessage = null,
                branches = newBranches.map { branch ->
                    branch.toUi()
                },
                selectedBranch = newBranches.first().toUi(),
//                markers = newBranches.map { branch ->
//                    branch.toUi().branchLocation.toMarker(branch.branchName)
//                }
            )
        }
//        Logger.i { "branch ${state.value.markers}" }

    }

    private fun onGetAllBranchesError(throwable: Throwable) {
        updateState { it.copy(isLoading = false, errorMessage = throwable.message) }
    }

    override fun onClickPickUpFromBranch(branchId: String) {

        if (args.isPickupFormBranch) {
            sendNewEffect(
                BranchScreenUiEffect.NavigateToProductsScreen(branchId, true, branchName = currentState.selectedBranch.branchName)
            )
        } else {
            if (currentState.selectedCar.id.isNotEmpty()) {
                sendNewEffect(
                    BranchScreenUiEffect.NavigateToProductsScreen(
                        branchId,
                        false,
                        currentState.selectedCar,
                        branchName = currentState.selectedBranch.branchName
                    )
                )
            } else {
                viewModelScope.launch {
                    showSnackBar(
                        title = getString(Res.string.choose_car),
                        message = getString(Res.string.please_choose_car),
                        false
                    )
                }
            }
        }

    }

    override fun onSelectBranch(branchId: String) {
        updateState {
            it.copy(
                selectedBranch = it.branches.first { it.id == branchId },
                selectedLocation = it.selectedBranch.branchLocation
            )
        }
    }

    override fun onClickLocationButton(location: LocationUiState) {
        sendNewEffect(
            BranchScreenUiEffect.NavigateToExternalMap(location)
        )
    }

    override fun onClickWorkTimeButton(branchId: String) {
        val branch = state.value.branches.first { it.id == branchId }
        updateState { it.copy(isWorkTimeOverlayVisible = true, selectedWorkTime = branch.workTime) }
    }

    override fun onCloseWorkTimeBottomSheet() {
        updateState { it.copy(isWorkTimeOverlayVisible = false) }
    }

    override fun onOpenSelectCar() {
        updateState { it.copy(isSelectCarBottomSheetVisible = true) }
    }

    override fun onCloseSelectCar() {
        updateState { it.copy(isSelectCarBottomSheetVisible = false) }
    }

    override fun onCarSelected(car: CarUiState) {
        // Update the selected car immediately in the UI
        updateState { it.copy(selectedCar = car) }
    }
    override fun onDeleteCar(carId: String) {
        tryToExecute(
            callee = { getAllBranchesUseCase.deleteCarById(carId) },
            onSuccess = {
                // Do nothing!
                // The DB delete triggers the Flow -> collectAllCars -> onCarsCollected.
                // onCarsCollected will handle removing it from the list and updating selection.
            },
            onError = { showSnackBar(message = "Failed to delete car",isSuccess = false) }
        )
    }


    override fun onSaveChanges() {
        // Commit changes if necessary, then close sheet
        onCloseSelectCar()
    }

    override fun onAddCar() {
        // When clicking "Add New Car" in the first sheet:
        // 1. Close the selection sheet
        updateState { it.copy(isSelectCarBottomSheetVisible = false) }
        // 2. Open the details sheet
        onOpenCarDetails()
    }

    override fun onOpenCarDetails() {
        updateState {
            it.copy(
                isCarDetailsBottomSheetVisible = true,
                isSelectCarBottomSheetVisible = false,
                addCarStep = AddCarStep.SELECT_BRAND, // Always start at Brand
                newCarNumber = "",
                selectedCarBrand = null,
                selectedCarColor = null
            )
        }
    }

    override fun onAddCarNextStep() {
        val currentState = currentState

        when (currentState.addCarStep) {
            AddCarStep.SELECT_BRAND -> {
                if (currentState.selectedCarBrand != null) {
                    updateState { it.copy(addCarStep = AddCarStep.SELECT_COLOR) }
                }
            }

            AddCarStep.SELECT_COLOR -> {
                if (currentState.selectedCarColor != null) {
                    updateState { it.copy(addCarStep = AddCarStep.ENTER_NUMBER) }
                }
            }

            AddCarStep.ENTER_NUMBER -> {
                if (currentState.newCarNumber.isNotEmpty()) {
                    saveNewCar()
                }
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun saveNewCar() {
        val currentState = state.value
        val newId = Uuid.random().toString()

        val newCarDomain = Car(
            id = newId,
            name = currentState.selectedCarBrand?.name ?: "Car",
            carNumber = currentState.newCarNumber,
            imageUrl = currentState.selectedCarBrand?.logoUrl ?: "",
            color = currentState.selectedCarColor ?: Color.Black,
            colorName = ""
        )

        tryToExecute(
            callee = { getAllBranchesUseCase.insertCar(newCarDomain) },
            onSuccess = {
                // We manually set selectedCar here to give immediate feedback to the user.
                // The 'cars' list will be updated automatically by the Flow shortly after.
                val newCarUi = newCarDomain.toUiState()
                updateState {
                    it.copy(
                        selectedCar = newCarUi,
                        isCarDetailsBottomSheetVisible = false,
                        isSelectCarBottomSheetVisible = true
                    )
                }
            },
            onError = { showSnackBar(message = "Failed to save car",isSuccess = false) }
        )
    }

    private suspend fun showSnackBar(
        title: String? = null,
        message: String,
        isSuccess: Boolean,
        durationMillis: Long = SNACK_BAR_DELAY,
    ) {
        updateState { oldState ->
            oldState.copy(
                snackBar = SnackBarState(
                    isVisible = true,
                    title = title,
                    message = message,
                    isSuccess = isSuccess
                )
            )
        }

        delay(durationMillis)

        hideSnackBar()
    }

    private fun hideSnackBar() {
        updateState { oldState ->
            oldState.copy(snackBar = oldState.snackBar.copy(isVisible = false))
        }
    }

    override fun onCloseCarDetails() {
        updateState { it.copy(isCarDetailsBottomSheetVisible = false) }
    }

    override fun onCarBrandSelected(brand: CarBrandUiState) {
        updateState { it.copy(selectedCarBrand = brand) }
    }

    override fun onCarColorSelected(color: CarColor) {
        updateState { it.copy(selectedCarColor = color.color) }
    }

    override fun onCarNumberChanged(number: String) {
        updateState { it.copy(newCarNumber = number) }
    }

}