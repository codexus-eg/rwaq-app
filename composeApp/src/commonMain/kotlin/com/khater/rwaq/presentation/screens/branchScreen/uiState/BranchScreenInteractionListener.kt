package com.khater.rwaq.presentation.screens.branchScreen.uiState

import androidx.compose.ui.graphics.Color
import com.khater.rwaq.domain.entities.branch.Location
import com.khater.rwaq.presentation.navigation.Screen
import com.khater.rwaq.presentation.screens.cartScreen.uiStates.CarColor

interface BranchScreenInteractionListener {
    fun onBack()
    fun onRetry()
    fun onClickPickUpFromBranch(branchId: String)
    fun onSelectBranch(branchId: String)
    fun onClickLocationButton(location: LocationUiState)
    fun onClickWorkTimeButton(branchId: String)
    fun onCloseWorkTimeBottomSheet()
    fun onOpenSelectCar()
    fun onCloseSelectCar()
    fun onCarSelected(car: CarUiState)
    fun onDeleteCar(carId: String)
    fun onAddCar()
    fun onSaveChanges()

    fun onOpenCarDetails()
    fun onCloseCarDetails()
    fun onCarBrandSelected(brand: CarBrandUiState)
    fun onCarColorSelected(color: CarColor)
    fun onCarNumberChanged(number: String)


    fun onAddCarNextStep()
}