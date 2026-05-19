package com.khater.rwaq.presentation.screens.newCartScreen

import com.khater.rwaq.data.dto.cart.CheckoutRequestDto
import com.khater.rwaq.data.dto.cart.UpdateCartItemRequestDto
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.khater.rwaq.data.util.putUserPoints
import com.khater.rwaq.data.util.userPoints
import com.khater.rwaq.domain.entities.branch.Branch
import com.khater.rwaq.domain.entities.car.Car
import com.khater.rwaq.domain.entities.cart.Cart
import com.khater.rwaq.domain.entities.cart.CartItem
import com.khater.rwaq.domain.location.NativeLocationResult
import com.khater.rwaq.domain.location.NativeLocationService
import com.khater.rwaq.domain.model.PickupType
import com.khater.rwaq.domain.useCases.GetAllBranchesUseCase
import com.khater.rwaq.domain.useCases.cart.*
import com.khater.rwaq.domain.util.InsufficientPointsException
import com.khater.rwaq.presentation.base.BaseViewModel
import com.khater.rwaq.presentation.mapper.handleDefaultException
import com.khater.rwaq.presentation.mapper.mapDefaultErrorToMessage
import com.khater.rwaq.presentation.mapper.toUi
import com.khater.rwaq.presentation.model.SnackBarState
import com.khater.rwaq.presentation.payment.OnlinePaymentCapability
import com.khater.rwaq.presentation.screens.branchScreen.uiState.BranchWorkTimeUiState
import com.khater.rwaq.presentation.screens.cartScreen.uiStates.OrderLocation
import com.khater.rwaq.presentation.screens.newCartScreen.uiStates.*
import com.khater.rwaq.presentation.util.LoginConstants.SNACK_BAR_DELAY
import com.khater.rwaq.presentation.util.isLoginRequiredError
import com.khater.rwaq.presentation.util.isOnlinePaymentCheckoutError
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.getString
import rwaq.composeapp.generated.resources.*
import kotlin.math.ceil
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalSettingsApi::class)
class NewCartViewModel(
    private val getCartUseCase: GetCartUseCase,
    private val updateCartItemUseCase: UpdateCartItemUseCase,
    private val removeCartItemUseCase: RemoveCartItemUseCase,
    private val checkoutUseCase: CheckoutUseCase,
    private val clearCartBadgeCountUseCase: ClearCartBadgeCountUseCase,
    private val getAllBranchesUseCase: GetAllBranchesUseCase,
//    private val clearCartUseCase: ClearCartUseCase,
    private val authenticationRepository: com.khater.rwaq.domain.repository.authentication.AuthenticationRepository,
    private val settings: FlowSettings,
) : BaseViewModel<NewCartUiState, NewCartUIEffect>(NewCartUiState()), NewCartInteractionListener {

    private val locationService = NativeLocationService()
    private val locationMutex = Mutex()
    private var hasLoadedProtectedCartData = false
    private var hasHandledFirstScreenOpen = false
    private var authObserverJob: Job? = null
    private var locationRequestJob: Job? = null

    init {
        checkAuthentication()
        observeUserPoints()
        syncInitialLocationState()
        syncOnlinePaymentCapabilities()
    }

    private fun observeUserPoints() {
        tryToCollect(
            collect = { settings.userPoints },
            onCollect = { points -> updateState { it.copy(userPoints = points) } },
            onError = { }
        )
    }

    private fun syncOnlinePaymentCapabilities() {
        val showApplePayOption = OnlinePaymentCapability.shouldShowApplePayOption &&
            OnlinePaymentCapability.isApplePaySupported
        updateState {
            it.copy(
                showApplePayOption = showApplePayOption,
                isApplePaySupported = OnlinePaymentCapability.isApplePaySupported,
                isApplePaySelected = showApplePayOption
            )
        }
    }

    private fun syncInitialLocationState() {
        viewModelScope.launch {
            refreshLocationAvailability()
            if (hasLocationPermission()) {
                locationMutex.withLock {
                    updateCurrentLocation(showError = false, openSettingsOnPermanentDenial = false)
                }
            }
        }
    }

    private fun requestCurrentPickupLocation(showError: Boolean = true) {
        if (locationRequestJob?.isActive == true) return
        locationRequestJob = viewModelScope.launch {
            ensureCurrentPickupLocation(showError = showError)
        }
    }

    private suspend fun ensureCurrentPickupLocation(
        showError: Boolean,
    ): Boolean = locationMutex.withLock {
        updateCurrentLocation(showError = showError, openSettingsOnPermanentDenial = true)
    }

    private suspend fun refreshLocationAvailability(): Boolean {
        val isAvailable = withContext(Dispatchers.IO) {
            runCatching { locationService.isLocationEnabled() }.getOrDefault(false)
        }
        updateState { it.copy(isLocationEnabled = isAvailable) }
        return isAvailable
    }

    private fun hasLocationPermission(): Boolean =
        runCatching { locationService.hasLocationPermission() }.getOrDefault(false)

    private suspend fun updateCurrentLocation(
        showError: Boolean,
        openSettingsOnPermanentDenial: Boolean,
    ): Boolean {
        return when (val result = locationService.getCurrentLocation()) {
            is NativeLocationResult.Success -> {
                val coordinates = result.coordinates
                val orderLocation = OrderLocation(
                    latitude = coordinates.latitude,
                    longitude = coordinates.longitude
                )
                val hasCoordinates = orderLocation.hasCoordinates()
                updateState {
                    it.copy(
                        orderLocation = orderLocation,
                        isLocationEnabled = true,
                        isLocationObtained = hasCoordinates
                    )
                }
                Logger.i { "Location : $coordinates" }

                if (!hasCoordinates && showError) {
                    showSnackBar(
                        title = getString(Res.string.add_location),
                        message = getString(Res.string.add_location),
                        isSuccess = false
                    )
                }

                hasCoordinates
            }

            else -> handleLocationError(
                error = result,
                showError = showError,
                openSettingsOnPermanentDenial = openSettingsOnPermanentDenial
            )
        }
    }

    private suspend fun handleLocationError(
        error: NativeLocationResult,
        showError: Boolean,
        openSettingsOnPermanentDenial: Boolean,
    ): Boolean {
        updateState {
            it.copy(
                orderLocation = OrderLocation(),
                isLocationEnabled = error !is NativeLocationResult.ServicesDisabled,
                isLocationObtained = false
            )
        }
        Logger.i { "Location error: $error" }

        if (showError) {
            when (error) {
                NativeLocationResult.ServicesDisabled -> showLocationServicesRequiredError()
                is NativeLocationResult.PermissionDenied -> {
                    val message = if (error.forever) {
                        getString(Res.string.location_permission_denied_forever_for_checkout_location)
                    } else {
                        getString(Res.string.location_permission_required_for_checkout_location)
                    }
                    showSnackBar(
                        title = getString(Res.string.gps_required),
                        message = message,
                        isSuccess = false
                    )
                    if (error.forever && openSettingsOnPermanentDenial) {
                        sendNewEffect(NewCartUIEffect.OpenLocationSettings)
                    }
                }

                is NativeLocationResult.Unavailable -> showSnackBar(
                    title = getString(Res.string.gps_required),
                    message = getString(Res.string.location_unavailable_try_again),
                    isSuccess = false
                )

                is NativeLocationResult.Success -> Unit
            }
        }
        return false
    }

    private suspend fun showLocationServicesRequiredError() {
        showSnackBar(
            title = getString(Res.string.gps_required),
            message = getString(Res.string.please_enable_gps_to_use_checkout_location),
            isSuccess = false
        )
    }

    fun checkAuthentication() {
        if (authObserverJob != null) {
            refreshProtectedDataOnOpen()
            return
        }

        authObserverJob = tryToCollect(
            collect = { authenticationRepository.isUserLoggedIn() },
            onCollect = { isLoggedIn ->
                if (isLoggedIn) {
                    updateState { it.copy(isGuest = false, showGuestDialog = false, error = null) }
                    loadProtectedCartData()
                } else {
                    markProtectedDataNeedsReload()
                    updateState {
                        it.copy(
                            isGuest = true,
                            showGuestDialog = true,
                            isLoading = false,
                            isCheckoutLoading = false,
                            cart = null,
                            error = null,
                            allBranches = emptyList(),
                            driveThruBranches = emptyList(),
                            cars = emptyList()
                        )
                    }
                }
            },
            onError = { }
        )
    }

    fun onScreenOpened() {
        if (!hasHandledFirstScreenOpen) {
            hasHandledFirstScreenOpen = true
            return
        }
        refreshCartOnOpen()
    }

    private fun loadProtectedCartData() {
        if (!hasLoadedProtectedCartData) {
            hasLoadedProtectedCartData = true
            fetchCars()
        }
        fetchCart()
        fetchBranches()
    }

    private fun refreshCartOnOpen() {
        if (authObserverJob == null) {
            checkAuthentication()
            return
        }
        if (!hasLoadedProtectedCartData || currentState.isGuest) return
        fetchCart()
    }

    private fun refreshProtectedDataOnOpen() {
        if (!hasLoadedProtectedCartData || currentState.isGuest) return
        fetchCart()
        fetchBranches()
    }

    override fun onRetryGetCart() {
        fetchCart()
    }

    private suspend fun mapCartErrorMessage(error: Throwable): String {
        val errorState = handleDefaultException(error)
        return getString(mapDefaultErrorToMessage(errorState))
    }

    private fun markProtectedDataNeedsReload() {
        hasLoadedProtectedCartData = false
    }

    private fun blockGuestAccess(): Boolean {
        if (!currentState.isGuest) return false
        updateState {
            it.copy(
                showGuestDialog = true,
                isLoading = false,
                isCheckoutLoading = false
            )
        }
        return true
    }

    fun fetchCart() {
        if (blockGuestAccess()) return

        tryToExecute(
            callee = { getCartUseCase() },
            onSuccess = { cart ->
                applyCart(cart, isLoading = false)
            },
            onError = { error ->
                if (error.isLoginRequiredError()) {
                    markProtectedDataNeedsReload()
                    updateState {
                        it.copy(
                            isGuest = true,
                            showGuestDialog = true,
                            cart = null,
                            error = null,
                            isLoading = false
                        )
                    }
                } else {
                    val errorMessage = mapCartErrorMessage(error)
                    updateState {
                        it.copy(
                            error = errorMessage,
                            isLoading = false
                        )
                    }
                }
            },
            onStart = {
                updateState {
                    it.copy(
                        isLoading = it.cart == null,
                        error = null
                    )
                }
            }
        )
    }

    private fun fetchBranches() {
        if (blockGuestAccess()) return

        tryToExecute(
            callee = { getAllBranchesUseCase() },
            onSuccess = { branches ->
                updateState {
                    val driveThruBranches = branches.filter { b -> b.isBranchAvailableForDriveThru }
                    it.copy(
                        branchesErrorMessage = null,
                        allBranches = branches,
                        driveThruBranches = driveThruBranches,
                        selectedPickupBranch = it.selectedPickupBranch ?: branches.firstOrNull(),
                        selectedDriveThruBranch = it.selectedDriveThruBranch
                            ?: driveThruBranches.firstOrNull()
                    )
                }
            },
            onError = { error -> updateState { it.copy(branchesErrorMessage = error.message) } },
            onStart = { updateState { it.copy(branchesErrorMessage = null) } }
        )
    }

    private fun fetchCars() {
        if (blockGuestAccess()) return

        tryToCollect(
            collect = { getAllBranchesUseCase.getAllCars() },
            onCollect = { cars -> updateState { it.copy(cars = cars) } },
            onError = { /* Handle error */ }
        )
    }

    override fun onIncreaseQuantity(itemId: String, currentQuantity: Int) {
        if (blockGuestAccess()) return

        val item = state.value.cart?.items?.firstOrNull { it.id == itemId }
        val availablePoints = state.value.userPoints
        if (item?.isRewardItem == true && availablePoints != null) {
            val requiredPoints = item.rewardPointsForOneMore()
            if (requiredPoints > 0 && availablePoints < requiredPoints) {
                viewModelScope.launch {
                    showInsufficientPointsError(requiredPoints, availablePoints)
                }
                return
            }
        }

        val request = UpdateCartItemRequestDto(quantity = currentQuantity + 1)
        updateItem(itemId, request)
    }

    override fun onDecreaseQuantity(itemId: String, currentQuantity: Int) {
        if (blockGuestAccess()) return

        if (currentQuantity > 1) {
            val request = UpdateCartItemRequestDto(quantity = currentQuantity - 1)
            updateItem(itemId, request)
        } else {
            onRemoveItem(itemId)
        }
    }

    private fun updateItem(itemId: String, request: UpdateCartItemRequestDto) {
        if (blockGuestAccess()) return

        tryToExecute(
            callee = { updateCartItemUseCase(itemId, request) },
            onSuccess = { cart ->
                applyCart(cart)
            },
            onError = { error ->
                handleCartMutationError(error)
            },
            onStart = { updateState { it.copy(updatingItemId = itemId) } },
            onFinish = { updateState { it.copy(updatingItemId = null) } }
        )
    }

    override fun onRemoveItem(itemId: String) {
        if (blockGuestAccess()) return

        tryToExecute(
            callee = { removeCartItemUseCase(itemId) },
            onSuccess = { cart ->
                applyCart(cart)
            },
            onError = { error ->
                handleCartMutationError(error)
            },
            onStart = { updateState { it.copy(updatingItemId = itemId) } },
            onFinish = { updateState { it.copy(updatingItemId = null) } }
        )
    }

    override fun onCheckoutClicked() {
        if (blockGuestAccess()) return

        val currentState = state.value

        if (currentState.pickupType.requiresLocation()) {
            viewModelScope.launch {
                updateState { it.copy(isCheckoutLoading = true) }
                val hasLocation = ensureCurrentPickupLocation(
                    showError = true
                )
                if (!hasLocation) {
                    updateState { it.copy(isCheckoutLoading = false) }
                    return@launch
                }
                proceedToCheckout()
            }
        } else {
            proceedToCheckout()
        }
    }

    private fun proceedToCheckout() {
        if (blockGuestAccess()) return

        val currentState = state.value

        if (currentState.pickupType.requiresLocation() && !currentState.orderLocation.hasCoordinates()) {
            viewModelScope.launch {
                updateState { it.copy(isCheckoutLoading = false, isLocationObtained = false) }
                showSnackBar(
                    title = getString(Res.string.add_location),
                    message = getString(Res.string.add_location),
                    isSuccess = false
                )
            }
            return
        }

        if (currentState.isDelivery && currentState.deliveryAddress.isBlank()) {
            viewModelScope.launch {
                updateState { it.copy(isCheckoutLoading = false) }
                showSnackBar(
                    title = getString(Res.string.delivery_address),
                    message = getString(Res.string.please_enter_delivery_address),
                    isSuccess = false
                )
            }
            return
        }

        if (currentState.isDriveThru && currentState.selectedCar.id.isEmpty()) {
            viewModelScope.launch {
                updateState { it.copy(isCheckoutLoading = false) }
                showSnackBar(
                    title = getString(Res.string.choose_car),
                    message = getString(Res.string.please_choose_car),
                    isSuccess = false
                )
            }
            return
        }

        val branch = when (currentState.pickupType) {
            PickupType.BRANCH -> currentState.selectedPickupBranch
            PickupType.DRIVE_THRU -> currentState.selectedDriveThruBranch
            PickupType.DELIVERY -> null
        }

        if (!currentState.isDelivery && branch == null) {
            viewModelScope.launch {
                updateState { it.copy(isCheckoutLoading = false) }
                showSnackBar(
                    message = getString(Res.string.no_branches_to_display),
                    isSuccess = false
                )
            }
            return
        }

        viewModelScope.launch {
            updateState { it.copy(isCheckoutLoading = true) }
            try {
                val checkoutRequest = currentState.toCheckoutRequest(branch)
                Logger.i { "Checkout Request: $checkoutRequest" }
                val result = checkoutUseCase(checkoutRequest)
                if (result.paymentInfo != null) {
                    sendNewEffect(
                        NewCartUIEffect.NavigateToPayment(
                            result.paymentInfo.clientSecret,
                            result.paymentInfo.publicKey
                        )
                    )
                } else {
                    sendNewEffect(NewCartUIEffect.NavigateBack)
                }
            } catch (e: Exception) {
                if (e.isLoginRequiredError()) {
                    markProtectedDataNeedsReload()
                    updateState {
                        it.copy(
                            isGuest = true,
                            showGuestDialog = true,
                            isLoading = false
                        )
                    }
                    showSnackBar(
                        message = getString(Res.string.please_login_first),
                        isSuccess = false
                    )
                } else if (
                    currentState.selectedPaymentMethod == ONLINE_PAYMENT_METHOD &&
                    e.isOnlinePaymentCheckoutError()
                ) {
                    showSnackBar(
                        message = getString(Res.string.failed_checkout_online_payment),
                        isSuccess = false
                    )
                } else {
                    showSnackBar(message = e.message ?: "", isSuccess = false)
                }
            } finally {
                updateState { it.copy(isCheckoutLoading = false) }
            }
        }
    }

    override fun onBack() {
        sendNewEffect(NewCartUIEffect.NavigateBack)
    }

    override fun onOrderNotesChanged(notes: String) {
        updateState { it.copy(orderNotes = notes) }
    }

    override fun onPickupTypeChanged(pickupType: PickupType) {
        updateState {
            it.copy(
                pickupType = pickupType,
                isLocationObtained = if (pickupType.requiresLocation()) it.isLocationObtained else false
            )
        }
        if (!pickupType.requiresLocation()) return
        requestCurrentPickupLocation()
    }

    override fun onDeliveryAddressChanged(address: String) {
        updateState { it.copy(deliveryAddress = address) }
    }

    override fun onRetryCurrentLocation() {
        requestCurrentPickupLocation()
    }

    override fun onBranchSelected(branch: Branch) {
        updateState {
            if (it.isDriveThru) it.copy(selectedDriveThruBranch = branch)
            else it.copy(selectedPickupBranch = branch)
        }
    }

    override fun onClickWorkTimeButton(branch: Branch) {
        updateState {
            it.copy(
                isWorkTimeOverlayVisible = true,
                selectedWorkTime = branch.workTime.map { wt ->
                    BranchWorkTimeUiState(
                        day = wt.day,
                        startTime = wt.startTime,
                        endTime = wt.endTime,
                        isAvailableAllDay = wt.isAvailableAllDay
                    )
                }
            )
        }
    }

    override fun onClickLocationButton(branch: Branch) {
        sendNewEffect(
            NewCartUIEffect.NavigateToExternalMap(branch.branchLocation.toUi())
        )
    }

    override fun onOpenSelectCar() {
        updateState { it.copy(isSelectCarBottomSheetVisible = true) }
    }

    override fun onCloseSelectCar() {
        updateState { it.copy(isSelectCarBottomSheetVisible = false) }
    }

    override fun onCarSelected(car: Car) {
        updateState { it.copy(selectedCar = car, isSelectCarBottomSheetVisible = false) }
    }

    override fun onDeleteCar(car: Car) {
        tryToExecute(
            callee = { getAllBranchesUseCase.deleteCarById(car.id) },
            onSuccess = { _ -> },
            onError = { /* Error */ }
        )
    }

    override fun onAddCar() {
        updateState {
            it.copy(
                isCarDetailsBottomSheetVisible = true,
                isSelectCarBottomSheetVisible = false,
                addCarStep = 1
            )
        }
    }

    override fun onSaveChanges() {
        onCloseSelectCar()
    }

    override fun onCloseWorkTimeBottomSheet() {
        updateState { it.copy(isWorkTimeOverlayVisible = false) }
    }

    override fun onAddMoreItems() {
        sendNewEffect(NewCartUIEffect.NavigateBack)
    }

    override fun onClickLogin() {
        updateState { it.copy(showGuestDialog = false) }
        sendNewEffect(NewCartUIEffect.NavigateToLogin)
    }

    override fun onDismissGuestDialog() {
        updateState { it.copy(showGuestDialog = false) }
        sendNewEffect(NewCartUIEffect.NavigateBack)
    }

    override fun onRetryGetBranches() {
        if (blockGuestAccess()) return
        fetchBranches()
    }

    override fun onPaymentMethodChanged(method: String) {
        updateState {
            it.copy(
                selectedPaymentMethod = method,
                isApplePaySelected = method == ONLINE_PAYMENT_METHOD && it.showApplePayOption
            )
        }
    }

    override fun onApplePaySelectionChanged(isSelected: Boolean) {
        updateState {
            it.copy(
                isApplePaySelected = isSelected && it.showApplePayOption,
                selectedPaymentMethod = if (isSelected) ONLINE_PAYMENT_METHOD else it.selectedPaymentMethod
            )
        }
    }

    fun onPaymentSuccess() {
        viewModelScope.launch {
            clearCartBadgeCountUseCase()
        }
        updateState { it.copy(isPaymentSuccessDialogVisible = true) }
    }

    fun onPaymentFailure(message: String? = null) {
        Logger.i { "Payment failure: $message" }
        viewModelScope.launch {
            showSnackBar(
                title = getString(Res.string.payment_failed_title),
                message = getString(Res.string.payment_failed_message),
                isSuccess = false
            )
        }
    }

    fun onPaymentPending() {
        viewModelScope.launch {
            showSnackBar(
                title = getString(Res.string.payment_pending_title),
                message = getString(Res.string.payment_pending_message),
                isSuccess = false
            )
        }
    }

    fun onPaymentResultDialogAction() {
        updateState { it.copy(isPaymentSuccessDialogVisible = false) }
        sendNewEffect(NewCartUIEffect.NavigateToOrders)
    }

    override fun onCloseCarDetails() {
        updateState { it.copy(isCarDetailsBottomSheetVisible = false) }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun onAddCarNextStep() {
        val currentStep = state.value.addCarStep
        if (currentStep < 3) {
            updateState { it.copy(addCarStep = currentStep + 1) }
        } else {
            val currentState = state.value
            val newId = Uuid.random().toString()
            val newCar = Car(
                id = newId,
                name = currentState.selectedCarBrand?.name ?: "",
                imageUrl = currentState.selectedCarBrand?.logoUrl ?: "",
                color = currentState.selectedCarColor?.color
                    ?: androidx.compose.ui.graphics.Color.Black,
                colorName = currentState.selectedCarColor?.name ?: "",
                carNumber = currentState.newCarNumber
            )
            tryToExecute(
                callee = { getAllBranchesUseCase.insertCar(newCar) },
                onSuccess = { _ ->
                    updateState {
                        it.copy(
                            selectedCar = newCar,
                            isCarDetailsBottomSheetVisible = false,
                            isSelectCarBottomSheetVisible = true
                        )
                    }
                },
                onError = { error ->
                    viewModelScope.launch {
                        showSnackBar(
                            message = error.message ?: "Failed to save car",
                            isSuccess = false
                        )
                    }
                }
            )
        }
    }

    private suspend fun showSnackBar(title: String? = null, message: String, isSuccess: Boolean) {
        updateState {
            it.copy(
                snackBar = SnackBarState(
                    isVisible = true,
                    title = title,
                    message = message,
                    isSuccess = isSuccess
                )
            )
        }
        delay(SNACK_BAR_DELAY)
        updateState { it.copy(snackBar = it.snackBar.copy(isVisible = false)) }
    }

    private fun applyCart(cart: Cart, isLoading: Boolean? = null) {
        val grossTotal = cart.items.sumOf { it.totalPrice }
        val rewardAmount = cart.items.filter { it.isRewardItem }.sumOf { it.totalPrice }
        val netPayable = grossTotal - rewardAmount
        val pickupType = PickupType.from(cart.pickupType)
        val cartLocation = if (cart.latitude != null && cart.longitude != null) {
            OrderLocation(latitude = cart.latitude, longitude = cart.longitude)
        } else {
            null
        }

        updateState { state ->
            state.copy(
                cart = cart,
                error = null,
                isLoading = isLoading ?: state.isLoading,
                orderNotes = cart.notes,
                pickupType = pickupType,
                deliveryAddress = cart.orderAddress.ifBlank { state.deliveryAddress },
                orderLocation = cartLocation ?: state.orderLocation,
                isLocationObtained = cartLocation?.hasCoordinates() ?: state.isLocationObtained,
                subTotal = grossTotal,
                rewardDiscount = rewardAmount,
                cartTotal = netPayable,
                userPoints = cart.userPoints ?: state.userPoints
            )
        }
    }

    private suspend fun handleCartMutationError(error: Throwable) {
        if (error.isLoginRequiredError()) {
            markProtectedDataNeedsReload()
            updateState {
                it.copy(
                    isGuest = true,
                    showGuestDialog = true,
                    isLoading = false,
                    isCheckoutLoading = false
                )
            }
            showSnackBar(
                message = getString(Res.string.please_login_first),
                isSuccess = false
            )
            return
        }

        if (error is InsufficientPointsException) {
            error.availablePoints?.let { points ->
                settings.putUserPoints(points)
                updateState { it.copy(userPoints = points) }
            }
            showInsufficientPointsError(
                requiredPoints = error.requiredPoints,
                availablePoints = error.availablePoints,
                fallbackMessage = error.message
            )
            return
        }

        showSnackBar(
            message = error.message ?: getString(Res.string.error),
            isSuccess = false
        )
    }

    private suspend fun showInsufficientPointsError(
        requiredPoints: Int?,
        availablePoints: Int?,
        fallbackMessage: String? = null,
    ) {
        val message = if (requiredPoints != null && availablePoints != null) {
            getString(Res.string.insufficient_points_message, requiredPoints, availablePoints)
        } else {
            fallbackMessage ?: getString(Res.string.error)
        }
        showSnackBar(
            title = getString(Res.string.insufficient_points_title),
            message = message,
            isSuccess = false
        )
    }

    private fun CartItem.rewardPointsForOneMore(): Int {
        val pointsFromUnitPrice = unitPrice.takeIf { it > 0.0 }
        val pointsFromTotalCost = if (pointsCost > 0 && quantity > 0) {
            pointsCost.toDouble() / quantity
        } else {
            0.0
        }
        return ceil(pointsFromUnitPrice ?: pointsFromTotalCost).toInt()
    }

    private fun OrderLocation.hasCoordinates(): Boolean =
        latitude != 0.0 || longitude != 0.0

    private fun PickupType.requiresLocation(): Boolean =
        this == PickupType.DRIVE_THRU || this == PickupType.DELIVERY

    private fun NewCartUiState.toCheckoutRequest(branch: Branch?): CheckoutRequestDto =
        when (pickupType) {
            PickupType.BRANCH -> CheckoutRequestDto(
                paymentMethod = selectedPaymentMethod,
                onlinePaymentMethod = onlinePaymentMethodForCheckout(),
                pickupType = PickupType.BRANCH.name,
                branchId = branch?.id,
                notes = orderNotes
            )

            PickupType.DRIVE_THRU -> CheckoutRequestDto(
                paymentMethod = selectedPaymentMethod,
                onlinePaymentMethod = onlinePaymentMethodForCheckout(),
                pickupType = PickupType.DRIVE_THRU.name,
                branchId = branch?.id,
                carNumber = selectedCar.carNumber,
                carColor = selectedCar.colorName,
                carColorName = selectedCar.colorName,
                carName = selectedCar.name,
                notes = orderNotes,
                latitude = orderLocation.latitude,
                longitude = orderLocation.longitude
            )

            PickupType.DELIVERY -> CheckoutRequestDto(
                paymentMethod = selectedPaymentMethod,
                onlinePaymentMethod = onlinePaymentMethodForCheckout(),
                pickupType = PickupType.DELIVERY.name,
                orderAddress = deliveryAddress.trim(),
                notes = orderNotes,
                latitude = orderLocation.latitude,
                longitude = orderLocation.longitude
            )
        }

    private fun NewCartUiState.onlinePaymentMethodForCheckout(): String? {
        if (selectedPaymentMethod != ONLINE_PAYMENT_METHOD) return null

        return if (showApplePayOption && isApplePaySupported && isApplePaySelected) {
            APPLE_PAY_ONLINE_PAYMENT_METHOD
        } else {
            CARD_ONLINE_PAYMENT_METHOD
        }
    }

    override fun onCarBrandSelected(brand: com.khater.rwaq.presentation.screens.branchScreen.uiState.CarBrandUiState) {
        updateState { it.copy(selectedCarBrand = brand) }
    }

    override fun onCarColorSelected(color: com.khater.rwaq.presentation.screens.cartScreen.uiStates.CarColor) {
        updateState { it.copy(selectedCarColor = color) }
    }

    override fun onCarNumberChanged(number: String) {
        updateState { it.copy(newCarNumber = number) }
    }
}

private const val ONLINE_PAYMENT_METHOD = "ONLINE"
private const val CARD_ONLINE_PAYMENT_METHOD = "CARD"
private const val APPLE_PAY_ONLINE_PAYMENT_METHOD = "APPLE_PAY"
