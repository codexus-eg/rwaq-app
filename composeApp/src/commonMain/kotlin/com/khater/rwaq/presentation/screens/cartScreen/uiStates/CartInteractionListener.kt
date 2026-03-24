package com.khater.rwaq.presentation.screens.cartScreen.uiStates

import androidx.compose.ui.graphics.Color
import com.khater.rwaq.domain.entities.order.Order
import com.khater.rwaq.domain.entities.order.OrderExtension
import com.khater.rwaq.presentation.screens.branchScreen.uiState.CarBrandUiState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.CarUiState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.LocationUiState

interface CartInteractionListener {
    // Cart Actions
    fun onBack()
    fun onIncreaseOrderCount(order: Order)
    fun onDecreaseOrderCount(order: Order)
    fun onDeleteOrder(orderId: String)
    fun onIncreaseExtension(order: Order, extension: OrderExtension)
    fun onDecreaseExtension(order: Order, extension: OrderExtension)
    fun onPlaceOrder() // Now acts as "Submit"
    fun onAddMoreItems()
    fun onOrderNotesChanged(notes: String)
    fun onPromoCodeChanged(code: String)
    fun onApplyPromoCode()
    fun onPaymentMethodChanged(method: PaymentMethod)

    fun onRetryGetBranches()

    // Receiving Method Actions
    fun onOrderTypeChanged(isDriveThru: Boolean)
    fun onBranchSelected(branchId: String)

    // Car Selection Actions
    fun onOpenSelectCar() // Opens the sheet
    fun onCloseSelectCar()
    fun onCarSelected(car: CarUiState)
    fun onDeleteCar(carId: String)
    fun onAddCar()
    fun onSaveChanges() // Confirms Car Selection inside sheet

    // Add Car Wizard Actions
    fun onCloseCarDetails()
    fun onCarBrandSelected(brand: CarBrandUiState)
    fun onCarColorSelected(color: CarColor)
    fun onCarNumberChanged(number: String)
    fun onAddCarNextStep()

    fun onClickLocationButton(location: LocationUiState)
    fun onClickWorkTimeButton(branchId: String)
    fun onCloseWorkTimeBottomSheet()

}