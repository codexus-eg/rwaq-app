package com.khater.rwaq.presentation.screens.newCartScreen.uiStates

import com.khater.rwaq.domain.entities.branch.Branch
import com.khater.rwaq.domain.entities.car.Car
import com.khater.rwaq.domain.model.PickupType

interface NewCartInteractionListener {
    fun onIncreaseQuantity(itemId: String, currentQuantity: Int)
    fun onDecreaseQuantity(itemId: String, currentQuantity: Int)
    fun onRemoveItem(itemId: String)
    fun onIncreaseExtension(itemId: String, extensionId: String)
    fun onDecreaseExtension(itemId: String, extensionId: String)
    fun onRemoveExtension(itemId: String, extensionId: String)
    fun onCheckoutClicked()

    fun onBack()
    fun onOrderNotesChanged(notes: String)
    fun onPickupTypeChanged(pickupType: PickupType)
    fun onDeliveryAddressChanged(address: String)
    fun onRetryCurrentLocation()
    fun onBranchSelected(branch: Branch)
    fun onClickWorkTimeButton(branch: Branch)
    fun onClickLocationButton(branch: Branch)
    fun onOpenSelectCar()
    fun onCloseSelectCar()
    fun onCarSelected(car: Car)
    fun onDeleteCar(car: Car)
    fun onAddCar()
    fun onSaveChanges()
    fun onCloseWorkTimeBottomSheet()
    fun onAddMoreItems()
    fun onClickLogin()
    fun onDismissGuestDialog()
    fun onRetryGetCart()
    fun onRetryGetBranches()
    fun onPaymentMethodChanged(method: String)
    fun onApplePaySelectionChanged(isSelected: Boolean)
    fun onCloseCarDetails()
    fun onAddCarNextStep()
    fun onCarBrandSelected(brand: com.khater.rwaq.presentation.screens.branchScreen.uiState.CarBrandUiState)
    fun onCarColorSelected(color: com.khater.rwaq.presentation.screens.cartScreen.uiStates.CarColor)
    fun onCarNumberChanged(number: String)
}
