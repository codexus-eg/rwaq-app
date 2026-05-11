package com.khater.rwaq.presentation.screens.cartScreen

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.khater.rwaq.data.dto.order.CreateOrderRequest
import com.khater.rwaq.data.dto.order.OrderExtensionRequest
import com.khater.rwaq.data.dto.order.OrderItemRequest
import com.khater.rwaq.domain.entities.branch.Branch
import com.khater.rwaq.domain.entities.car.Car
import com.khater.rwaq.domain.entities.order.Order
import com.khater.rwaq.domain.entities.order.OrderExtension
import com.khater.rwaq.domain.repository.authentication.AuthenticationRepository
import com.khater.rwaq.domain.useCases.CreateOrderUseCase
import com.khater.rwaq.domain.useCases.GetAllBranchesUseCase
import com.khater.rwaq.domain.useCases.ManageCartUseCase
import com.khater.rwaq.presentation.base.BaseViewModel
import com.khater.rwaq.presentation.mapper.toUi
import com.khater.rwaq.presentation.model.SnackBarState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.AddCarStep
import com.khater.rwaq.presentation.screens.branchScreen.uiState.BranchScreenUiEffect
import com.khater.rwaq.presentation.screens.branchScreen.uiState.BranchUiState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.CarBrandUiState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.CarUiState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.LocationUiState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.toUiState
import com.khater.rwaq.presentation.screens.cartScreen.uiStates.CarColor
import com.khater.rwaq.presentation.screens.cartScreen.uiStates.CartInteractionListener
import com.khater.rwaq.presentation.screens.cartScreen.uiStates.CartScreenUIEffect
import com.khater.rwaq.presentation.screens.cartScreen.uiStates.CartUiState
import com.khater.rwaq.presentation.screens.cartScreen.uiStates.OrderLocation
import com.khater.rwaq.presentation.screens.cartScreen.uiStates.PaymentMethod
import com.khater.rwaq.presentation.util.LoginConstants.SNACK_BAR_DELAY
import com.khater.rwaq.presentation.util.isOnlinePaymentCheckoutError
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.mobile
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.choose_car
import rwaq.composeapp.generated.resources.failed_checkout_online_payment
import rwaq.composeapp.generated.resources.gps_required
import rwaq.composeapp.generated.resources.max_count
import rwaq.composeapp.generated.resources.no_branches_to_display
import rwaq.composeapp.generated.resources.please_choose_car
import rwaq.composeapp.generated.resources.please_enable_gps_to_use_drive_thru
import rwaq.composeapp.generated.resources.you_have_reach_max_count_of_this_extension
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class CartViewModel(
    private val manageCartUseCase: ManageCartUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val getAllBranchesUseCase: GetAllBranchesUseCase,
    private val authenticationRepository: AuthenticationRepository,
) : BaseViewModel<CartUiState, CartScreenUIEffect>(CartUiState()),
    CartInteractionListener {
    private val geolocator = Geolocator.mobile()

    init {
        checkAuthentication()
        subscribeToOrders()
        getAllBranches()
        collectAllCars()
        getUserLocation()
    }

    fun checkAuthentication() {
        viewModelScope.launch {
            val isLoggedIn = authenticationRepository.isUserLoggedIn().first()
            Logger.i { "alwbvlaw $isLoggedIn" }
            updateState { it.copy(isGuest = !isLoggedIn, showGuestDialog = !isLoggedIn) }
        }
    }

    private fun getUserLocation() {
        tryToExecute(
            callee = { geolocator.current() },
            onSuccess = { result ->
                when (result) {
                    is GeolocatorResult.Success -> {
                        val coordinates = result.data.coordinates
                        val locationString = "${coordinates.latitude}, ${coordinates.longitude}"

                        updateState {
                            it.copy(
                                orderLocation = OrderLocation(
                                    latitude = coordinates.latitude,
                                    longitude = coordinates.longitude
                                ),
                                isLoading = false
                            )
                        }
                        Logger.i { "locationaubvaooabbba $locationString" }
                    }

                    is GeolocatorResult.Error -> {
                        Logger.i { "location ${result.message}" }
                        updateState { it.copy(orderLocation = OrderLocation(), isLoading = false) }
                    }
                }
            },
            onError = { error ->
                Logger.i { "location ${error.message}" }
                updateState { it.copy(orderLocation = OrderLocation(), isLoading = false) }
            }
        )
    }

    fun enableGps() {
        getUserLocation()
    }

    // ... [Existing Cart Methods omitted for brevity - no changes] ...
    private fun subscribeToOrders() {
        tryToCollect(
            onStart = { updateState { it.copy(isLoading = true) } },
            collect = { manageCartUseCase() },
            onCollect = { orders ->
                val grossTotal = orders.sumOf { it.totalPrice }
                val rewardAmount = orders.filter { it.isReward }.sumOf { it.totalPrice }
                val netPayable = grossTotal - rewardAmount
                updateState {
                    it.copy(
                        orders = orders,
                        // cartTotal = orders.sumOf { o -> o.totalPrice },
                        // Update the breakdown fields
                        subTotal = grossTotal,
                        rewardDiscount = rewardAmount,
                        cartTotal = netPayable,
                        isLoading = false
                    )
                }
            },
            onError = { e -> updateState { it.copy(errorMessage = e.message, isLoading = false) } }
        )
    }

    override fun onIncreaseOrderCount(order: Order) {
        if (!order.isReward)
            tryToExecute({ manageCartUseCase.increaseOrderCount(order) }, {}, {})
    }

    override fun onDecreaseOrderCount(order: Order) {
        viewModelScope.launch { manageCartUseCase.decreaseOrderCount(order) }
    }

    override fun onDeleteOrder(orderId: String) {
        viewModelScope.launch { manageCartUseCase.deleteOrder(orderId) }
    }

    override fun onIncreaseExtension(order: Order, extension: OrderExtension) {
        if (extension.count < extension.maxCount) {
            viewModelScope.launch { manageCartUseCase.increaseExtensionCount(order, extension.id) }
        } else {
            viewModelScope.launch {
                showSnackBar(
                    title = getString(Res.string.max_count),
                    message = getString(Res.string.you_have_reach_max_count_of_this_extension),
                    isSuccess = false
                )
            }
        }
    }

    override fun onDecreaseExtension(order: Order, extension: OrderExtension) {
        tryToExecute({ manageCartUseCase.decreaseExtensionCount(order, extension.id) }, {}, {})
    }

    override fun onAddMoreItems() {
        sendNewEffect(CartScreenUIEffect.NavigateBack)
    }

    override fun onOrderNotesChanged(notes: String) {
        updateState { it.copy(orderNotes = notes) }
    }

    override fun onPromoCodeChanged(code: String) {
        updateState { it.copy(promoCode = code) }
    }

    override fun onApplyPromoCode() {}
    override fun onPaymentMethodChanged(method: PaymentMethod) {
        updateState { it.copy(selectedPaymentMethod = method) }
    }

    // =========================================================================
    // INLINE SELECTION LOGIC
    // =========================================================================

    private fun getAllBranches() {
        tryToExecute(
            callee = { getAllBranchesUseCase() },
            onStart = {
                updateState { it.copy(isBranchesLoading = true, branchesErrorMessage = null) }
            },
            onSuccess = ::onGetAllBranchesSuccess,
            onError = { error ->
                // Catch the error so we can display the retry button!
                updateState {
                    it.copy(
                        isBranchesLoading = false,
                        branchesErrorMessage = error.message ?: "Failed to load branches"
                    )
                }
            }
        )
    }

    override fun onRetryGetBranches() {
        getAllBranches()
    }

    private fun onGetAllBranchesSuccess(branches: List<Branch>) {
        updateState { it.copy(isBranchesLoading = false) }
        val uiBranches = branches.map { it.toUi() }
        filterBranchesAndMaintainSelection(uiBranches)
    }

    override fun onOrderTypeChanged(isDriveThru: Boolean) {
        // If switching types, we might need to clear selected branch if it doesn't support the new type
        // But simplified: update flag, re-filter list
        updateState { it.copy(isDriveThru = isDriveThru) }

        if (isDriveThru) {
            enableGps()
            // Opens the system dialog/settings to enable GPS
            getUserLocation() // Attempts to fetch the coordinates
        }
        // We need the full list to re-filter. In a real app, store 'allBranches' in state.
        // Here we call the usecase again or assume state.branches contained relevant ones.
        // Best practice: call getAllBranches again to be safe and clean.
        filterBranchesAndMaintainSelection(currentState.allBranches)
    }

    private fun filterBranchesAndMaintainSelection(fetchedBranches: List<BranchUiState>) {
        // 1. Create the two lists
        val pickupList = fetchedBranches
        val driveThruList = fetchedBranches.filter { it.isBranchAvailableForDriveThru }

        // 2. Determine Default Selections
        // Logic: Try to find the currently selected ID in the new list.
        // If not found (or first run), default to the first available branch.

        val currentPickupId = state.value.selectedPickupBranch?.id
        val newPickupSelection =
            pickupList.find { it.id == currentPickupId } ?: pickupList.firstOrNull()

        val currentDriveThruId = state.value.selectedDriveThruBranch?.id
        val newDriveThruSelection =
            driveThruList.find { it.id == currentDriveThruId } ?: driveThruList.firstOrNull()

        updateState {
            it.copy(
                allBranches = pickupList,
                driveThruBranches = driveThruList,
                // USE THE CALCULATED VARIABLES HERE
                selectedPickupBranch = newPickupSelection,
                selectedDriveThruBranch = newDriveThruSelection
            )
        }
    }

    override fun onClickLocationButton(location: LocationUiState) {
        sendNewEffect(
            CartScreenUIEffect.NavigateToExternalMap(location)
        )
    }

    override fun onCloseWorkTimeBottomSheet() {
        updateState { it.copy(isWorkTimeOverlayVisible = false) }
    }

    override fun onClickWorkTimeButton(branchId: String) {
        val branch = currentState.allBranches.first { it.id == branchId }
        updateState { it.copy(isWorkTimeOverlayVisible = true, selectedWorkTime = branch.workTime) }
    }

    override fun onBranchSelected(branchId: String) {
        val currentState = state.value

        if (currentState.isDriveThru) {
            // User is in Drive Thru mode -> Update Drive Thru Selection
            val branch = currentState.driveThruBranches.find { it.id == branchId }
            updateState { it.copy(selectedDriveThruBranch = branch) }
        } else {
            // User is in Pickup mode -> Update Pickup Selection
            val branch = currentState.allBranches.find { it.id == branchId }
            updateState { it.copy(selectedPickupBranch = branch) }
        }
    }
    // =========================================================================
    // CAR SELECTION
    // =========================================================================

    override fun onOpenSelectCar() {
        updateState { it.copy(isSelectCarBottomSheetVisible = true) }
    }

    private fun collectAllCars() {
        tryToCollect(
            collect = { getAllBranchesUseCase.getAllCars() },
            onCollect = ::onCarsCollected,
            onError = { }
        )
    }

    private fun onCarsCollected(cars: List<Car>) {
        val carsUi = cars.map { it.toUiState() }
        val currentSelectedId = state.value.selectedCar.id
        val newSelectedCar =
            carsUi.find { it.id == currentSelectedId } ?: carsUi.firstOrNull() ?: CarUiState()
        updateState { it.copy(cars = carsUi, selectedCar = newSelectedCar) }
    }

    override fun onCarSelected(car: CarUiState) {
        updateState { it.copy(selectedCar = car) }
    }

    override fun onCloseSelectCar() {
        updateState { it.copy(isSelectCarBottomSheetVisible = false) }
    }

    override fun onSaveChanges() {
        // Just close the sheet, selection updates in real-time via onCarSelected
        onCloseSelectCar()
    }

    // ... [Add Car Wizard methods (same as before)] ...
    override fun onAddCar() {
        updateState { it.copy(isSelectCarBottomSheetVisible = false) }; onOpenCarDetails()
    }

    private fun onOpenCarDetails() {
        updateState {
            it.copy(
                isCarDetailsBottomSheetVisible = true,
                addCarStep = AddCarStep.SELECT_BRAND,
                newCarNumber = "",
                selectedCarBrand = null,
                selectedCarColor = null,
                selectedCarColorName = null,

                )
        }
    }

    override fun onCloseCarDetails() {
        updateState { it.copy(isCarDetailsBottomSheetVisible = false) }
    }

    override fun onCarBrandSelected(brand: CarBrandUiState) {
        updateState { it.copy(selectedCarBrand = brand) }
    }

    override fun onCarColorSelected(color: CarColor) {
        updateState { it.copy(selectedCarColor = color.color, selectedCarColorName = color.name) }
    }

    override fun onCarNumberChanged(number: String) {
        updateState { it.copy(newCarNumber = number) }
    }

    override fun onAddCarNextStep() {
        val currentState = state.value
        when (currentState.addCarStep) {
            AddCarStep.SELECT_BRAND -> if (currentState.selectedCarBrand != null) updateState {
                it.copy(
                    addCarStep = AddCarStep.SELECT_COLOR
                )
            }

            AddCarStep.SELECT_COLOR -> if (currentState.selectedCarColor != null) updateState {
                it.copy(
                    addCarStep = AddCarStep.ENTER_NUMBER
                )
            }

            AddCarStep.ENTER_NUMBER -> if (currentState.newCarNumber.isNotEmpty()) saveNewCar()
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
            colorName = currentState.selectedCarColorName ?: "اسود"
        )
        tryToExecute(
            { getAllBranchesUseCase.insertCar(newCarDomain) },
            {
                val newCarUi = newCarDomain.toUiState(); updateState {
                it.copy(
                    selectedCar = newCarUi,
                    isCarDetailsBottomSheetVisible = false,
                    isSelectCarBottomSheetVisible = true
                )
            }
            },
            { showSnackBar(message = "Failed to save car", isSuccess = false) })
    }

    override fun onDeleteCar(carId: String) {
        tryToExecute(
            { getAllBranchesUseCase.deleteCarById(carId) },
            {},
            { showSnackBar(message = "Failed to delete car", isSuccess = false) })
    }

    // =========================================================================
    // PLACE ORDER WITH VALIDATION
    // =========================================================================

    override fun onPlaceOrder() {
        val currentState = state.value
        if (currentState.orders.isEmpty()) return

        Logger.i { "locationavekjbjela ${currentState.orderLocation}" }
        // 1. GPS CHECK FOR DRIVE THRU
        if (currentState.isDriveThru && (currentState.orderLocation.latitude == 0.0 || currentState.orderLocation.longitude == 0.0)) {
            enableGps() // Request system GPS
            getUserLocation() // Attempt to fetch coordinates to update state


            return // Stop execution until GPS is provided
        }


        // DETERMINE EFFECTIVE BRANCH
        val effectiveBranch = if (currentState.isDriveThru) {
            currentState.selectedDriveThruBranch
        } else {
            currentState.selectedPickupBranch
        }

        // 1. MANDATORY BRANCH CHECK
        if (effectiveBranch == null) {
            viewModelScope.launch {
                showSnackBar(
                    message = getString(Res.string.no_branches_to_display),
                    isSuccess = false
                )
            }
            return
        }

        // 2. MANDATORY CAR CHECK (If Drive Thru)
        if (currentState.isDriveThru) {
            if (currentState.selectedCar.id.isEmpty()) {
                viewModelScope.launch {
                    showSnackBar(
                        title = getString(Res.string.choose_car),
                        message = getString(Res.string.please_choose_car),
                        isSuccess = false
                    )
                }
                return
            }
        }

        // 3. SUBMIT
        submitOrder(
            effectiveBranch,
            if (currentState.isDriveThru) currentState.selectedCar else null
        )
    }

    private fun submitOrder(branch: BranchUiState, car: CarUiState?) {
        val currentState = state.value
        val orderItems = currentState.orders.map { order ->
            OrderItemRequest(
                productId = order.productId,
                productName = order.name,
                quantity = order.count,
                size = order.size,
                imageUrl = order.imageUrl,
                unitPrice = order.itemPrice,
                totalPrice = order.totalPrice,
                extensions = order.extension.map { ext ->
                    OrderExtensionRequest(
                        ext.id,
                        ext.name,
                        ext.price,
                        ext.count
                    )
                },
                branchId = branch.id,
                branchName = branch.branchName,
                isPickupFromBranch = !currentState.isDriveThru,
                carName = car?.name,
                carNumber = car?.carNumber,
                carColor = car?.color?.toArgb(),
                // new color name here
                carColorName = car?.colorName,
                isRewarded = order.isReward
            )
        }
        val requestBody = CreateOrderRequest(
            items = orderItems,
            notes = currentState.orderNotes,
            couponCode = currentState.promoCode.ifBlank { null },
            paymentMethod = currentState.selectedPaymentMethod.name,
            totalAmount = currentState.cartTotal,
            orderLocation = currentState.orderLocation
        )
        Logger.i("aavxxxve ${requestBody}")

        tryToExecute(
            onStart = { updateState { it.copy(isSendingOrderLoading = true) } },
            callee = { createOrderUseCase(requestBody) },
            onSuccess = { response ->
                Logger.i("response: $response")
                val isOnline = requestBody.paymentMethod == PaymentMethod.ONLINE.name

                if (isOnline && response.clientSecret.isNotEmpty()) {
                    // ✅ Do NOT clear cart yet — wait for payment gateway result
                    sendNewEffect(
                        CartScreenUIEffect.NavigateToPayment(
                            clientSecret = response.clientSecret,
                            publicKey = response.publicKey
                        )
                    )
                } else {
                    // ✅ CASH — order is confirmed server-side, safe to clear now
                    viewModelScope.launch { manageCartUseCase.clearCart() }
                    sendNewEffect(CartScreenUIEffect.NavigateBack)
                }
//                manageCartUseCase.clearCart()
//                updateState { it.copy(isSendingOrderLoading = false) }
//                if (requestBody.paymentMethod == PaymentMethod.ONLINE.name && response.clientSecret.isNotEmpty()) {
//                    sendNewEffect(
//                        CartScreenUIEffect.NavigateToPayment(
//                            clientSecret = response.clientSecret,
//                            publicKey = response.publicKey
//                        )
//                    )
//                } else {
//                    sendNewEffect(CartScreenUIEffect.NavigateBack)
//                }
            },
            onError = { e ->
                val errorMessage = if (
                    requestBody.paymentMethod == PaymentMethod.ONLINE.name &&
                    e.isOnlinePaymentCheckoutError()
                ) {
                    getString(Res.string.failed_checkout_online_payment)
                } else {
                    e.message
                }
                updateState {
                    it.copy(
                        isSendingOrderLoading = false,
                        sendingOrderError = errorMessage
                    )
                }
                showSnackBar(
                    message = errorMessage ?: getString(Res.string.failed_checkout_online_payment),
                    isSuccess = false
                )
            }
        )
    }

    fun onPaymentFinished() {
        viewModelScope.launch {
            manageCartUseCase.clearCart()
            sendNewEffect(CartScreenUIEffect.NavigateBack)
        }
    }
    
    private suspend fun showSnackBar(title: String? = null, message: String, isSuccess: Boolean) {
        updateState { it.copy(snackBar = SnackBarState(true, title, message, isSuccess)) }
        delay(SNACK_BAR_DELAY)
        updateState { it.copy(snackBar = it.snackBar.copy(isVisible = false)) }
    }

    private fun hideSnackBar() {
        updateState { it.copy(snackBar = it.snackBar.copy(isVisible = false)) }
    }

    override fun onBack() {
        sendNewEffect(CartScreenUIEffect.NavigateBack)
    }

    override fun onClickLogin() {
        updateState { it.copy(showGuestDialog = false) }
        sendNewEffect(CartScreenUIEffect.NavigateToLogin)
    }

    override fun onDismissGuestDialog() {
        updateState { it.copy(showGuestDialog = false) }
        sendNewEffect(CartScreenUIEffect.NavigateBack)
    }
}
