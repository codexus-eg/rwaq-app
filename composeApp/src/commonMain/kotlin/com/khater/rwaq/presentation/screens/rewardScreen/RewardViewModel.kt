package com.khater.rwaq.presentation.screens.rewardScreen

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.khater.rwaq.data.dto.product.toDetailsUiState
import com.khater.rwaq.domain.entities.order.Order
import com.khater.rwaq.domain.entities.order.OrderExtension
import com.khater.rwaq.domain.repository.authentication.AuthenticationRepository
import com.khater.rwaq.domain.useCases.GetAllProductsUseCase
import com.khater.rwaq.domain.useCases.GetUserUseCase
import com.khater.rwaq.domain.useCases.ManageCartUseCase
import com.khater.rwaq.presentation.base.BaseViewModel
import com.khater.rwaq.presentation.mapper.handleDefaultException
import com.khater.rwaq.presentation.mapper.mapDefaultErrorToMessage
import com.khater.rwaq.presentation.model.SnackBarState
import com.khater.rwaq.presentation.screens.rewardScreen.uiState.RewardInteractionListener
import com.khater.rwaq.presentation.screens.rewardScreen.uiState.RewardsUiEffect
import com.khater.rwaq.presentation.screens.rewardScreen.uiState.RewardsUiState
import com.khater.rwaq.presentation.util.LoginConstants.SNACK_BAR_DELAY
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.error
import rwaq.composeapp.generated.resources.insufficient_points_message
import rwaq.composeapp.generated.resources.insufficient_points_title
import rwaq.composeapp.generated.resources.out_of_stock_message
import rwaq.composeapp.generated.resources.out_of_stock_title
import rwaq.composeapp.generated.resources.redeem_reward_failed_message
import rwaq.composeapp.generated.resources.reward_redeemed_message
import rwaq.composeapp.generated.resources.reward_redeemed_with_points_message
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class RewardViewModel(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val manageCartUseCase: ManageCartUseCase,
    private val authenticationRepository: AuthenticationRepository,
) : BaseViewModel<RewardsUiState, RewardsUiEffect>(RewardsUiState()),
    RewardInteractionListener {


    init {
        checkAuthenticationAndFetchData()
        fetchHomeData()
        subscribeToCart()
    }

    private fun checkAuthenticationAndFetchData() {
        viewModelScope.launch {
            val isLoggedIn = authenticationRepository.isUserLoggedIn().first()
            updateState { it.copy(isGuest = !isLoggedIn) }
            if (isLoggedIn) {
                getUser()
            }
        }
    }

    private fun subscribeToCart() {
        tryToCollect(
            collect = { manageCartUseCase() },
            onCollect = { orders ->
                // Sum up the points of all items in the cart that are rewards
                val pendingPoints = orders.filter { it.isReward }.sumOf { it.totalPrice }

                updateState { state ->
                    state.copy(
                        pendingRewardPoints = pendingPoints,
                        // Automatically subtract cart points from server points
                        points = (state.serverPoints - pendingPoints).coerceAtLeast(0.0)                    )
                }
            },
            onError = { Logger.e("Failed to collect cart", it) }
        )
    }

    private fun getUser() {
        if (currentState.isGuest) return
        tryToExecute(
            callee = { getUserUseCase() },
            onSuccess = { user ->
                updateState { state ->
                    val actualServerPoints = user.points.toDouble()
                    state.copy(
                        serverPoints = actualServerPoints, // Store the real backend value
                        points = (actualServerPoints - state.pendingRewardPoints).coerceAtLeast(0.0)                    )
                }
            },
            onError = {
                val mappedErrorMessage = mapErrorMessage(it)
                val errorMessage = getString(mappedErrorMessage)
                updateState { state -> state.copy(isLoading = false, errorMessage = errorMessage) }
                showSnackBar(
                    title = getString(Res.string.error),
                    message = getString(mapErrorMessage(it)),
                    isSuccess = false
                )
            }
        )
    }

    private fun fetchHomeData() {
        updateState { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                // 1. Fetch Raw Data
                val response = getAllProductsUseCase(1, 50, true) // Fetch enough items
                val allData = response.data
                Logger.i { "labwlaiv $allData" }

                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        products = allData,
                    )
                }

            } catch (e: Throwable) {
                val mappedErrorMessage = mapErrorMessage(e)
                val errorMessage = getString(mappedErrorMessage)
                updateState { it.copy(isLoading = false, errorMessage = errorMessage) }
            }
        }
    }

    private fun mapErrorMessage(throwable: Throwable): StringResource {
        val errorState = handleDefaultException(throwable)
        return mapDefaultErrorToMessage(errorState)
    }

    override fun onRefreshUserPoints() {
        if (!currentState.isGuest) {
            getUser()
        }
    }
    override fun onRetry() {
        if (!currentState.isGuest) {
            getUser()
        }
        fetchHomeData()
    }

    override fun onBack() {
        sendNewEffect(RewardsUiEffect.NavigateBack)
    }

    override fun onProductClicked(productId: String) {
        if (currentState.isGuest) {
            // Optional: Maybe show a snackbar or just do nothing as requested "don't show dialog"
            // The user didn't specify what to do if guest clicks a product in Reward Screen
            // Usually rewards require points which a guest won't have.
            return
        }
        currentState.products.find { it.id == productId }?.let { product ->
            updateState { state ->
                state.copy(
                    selectedProductDetails = product.toDetailsUiState(),
                    isDetailsVisible = true
                )
            }
        }
    }

    override fun onDismissDetails() {
        updateState { it.copy(isDetailsVisible = false) }
    }

    override fun onQuantityChange(change: Int) {
        val details = currentState.selectedProductDetails ?: return
        val currentQty = details.productQuantity
        val newQty = currentQty + change

        // 1. Prevent negative/zero quantity
        if (newQty < 1) return

        // 2. Calculate Unit Price (Base + Size + Extensions)
        // We use the existing calculated price logic from your details state
        val unitPrice = details.calculatedSingleUnitTestPrice

        // 3. Calculate Projected Total Price
        val projectedTotalPrice = unitPrice * newQty

        // 4. CHECK: Does user have enough points?
        if (projectedTotalPrice > currentState.points) {
            viewModelScope.launch {
                showSnackBar(
                    title = "Limit Reached",
                    message = "You only have ${currentState.points} points. Total cost would be $projectedTotalPrice.",
                    isSuccess = false
                )
            }
            return // Stop execution, do not update state
        }

        // 5. Update State if valid
        updateState { state ->
            state.copy(
                selectedProductDetails = details.copy(
                    productQuantity = newQty,
                    calculatedTotalPrice = projectedTotalPrice
                )
            )
        }
    }

    @OptIn(ExperimentalUuidApi::class, ExperimentalUuidApi::class)
    override fun onAddToCart() {
        val details = currentState.selectedProductDetails ?: return

        // Double check points before committing (safety check)
        if (details.calculatedTotalPrice > currentState.points) {
            viewModelScope.launch {
                showSnackBar(
                    title = getString(Res.string.insufficient_points_title),
                    message = getString(
                        Res.string.insufficient_points_message,
                        details.calculatedTotalPrice.toInt(),
                        currentState.points.toInt()
                    ), isSuccess = false
                )
            }
            return
        }

        val orderExtensions = details.extensions
            .filter { it.currentQty > 0 }
            .map { uiExt ->
                OrderExtension(
                    id = uiExt.id,
                    name = uiExt.name,
                    price = uiExt.price,
                    count = uiExt.currentQty,
                    maxCount = uiExt.maxCount
                )
            }

        val selectedSizeName = details.sizes
            .find { it.id == details.selectedSizeId }?.name
            ?: "Standard"

        val newOrder = Order(
            id = Uuid.random().toString(),
            name = details.name,
            totalPrice = details.calculatedTotalPrice,
            itemPrice = details.calculatedSingleUnitTestPrice,
            count = details.productQuantity,
            size = selectedSizeName,
            extension = orderExtensions,
            branchId = "",
            branchName = "",
            isPickupFromBranch = false,
            carName = "",
            carNumber = "",
            carColor = Color.White,
            imageUrl = details.imageUrl,
            productId = details.id,
            isReward = true
        )

        Logger.i("Adding Reward Order: $newOrder")

        viewModelScope.launch {
            try {
                // 1. Save to Local Cart
                manageCartUseCase.insertOrder(newOrder)

//                // 2. Deduct Points Locally
//                val usedPoints = details.calculatedTotalPrice
//                val newPointBalance = currentState.points - usedPoints
//
//                updateState { state ->
//                    state.copy(
//                         points = newPointBalance, // Update UI Points immediately
//                        isDetailsVisible = false  // Close the sheet
//                    )
//                }
                updateState { state ->
                    state.copy(
                        isDetailsVisible = false  // Close the sheet
                    )
                }

                showSnackBar(
                    message = getString(Res.string.reward_redeemed_message),
                    isSuccess = true
                )

            } catch (e: Exception) {
                Logger.e("Failed to add reward", e)
                showSnackBar(
                    message = getString(Res.string.redeem_reward_failed_message),
                    isSuccess = false
                )
            }
        }
    }

    // Add this method to your existing HomeViewModel class
    @OptIn(ExperimentalUuidApi::class)
    override fun onQuickAddToCart(productId: String) {
        val product = currentState.products.find { it.id == productId } ?: return

        // Don't allow adding out of stock items
        if (!product.isInStock) {
            viewModelScope.launch {
                showSnackBar(
                    title = getString(Res.string.out_of_stock_title),
                    message = getString(Res.string.out_of_stock_message, product.name),
                    isSuccess = false
                )
            }
            return
        }

        // Calculate final price (with discount applied)
        val finalPrice = (product.basePrice - product.discount).coerceAtLeast(0.0)

        // CRITICAL: Check if user has enough points
        if (finalPrice > currentState.points) {
            viewModelScope.launch {
                showSnackBar(
                    message = getString(
                        Res.string.insufficient_points_message,
                        finalPrice.toInt(),
                        currentState.points.toInt()
                    ),
                    isSuccess = false
                )
            }
            return
        }

        // Create reward order with default values
        val newOrder = Order(
            id = Uuid.random().toString(),
            name = product.name,
            totalPrice = finalPrice,
            itemPrice = finalPrice,
            count = 1, // Default quantity
            size = "Standard", // Default size
            extension = emptyList(), // No extensions
            branchId = "",
            branchName = "",
            isPickupFromBranch = false,
            carName = "",
            carNumber = "",
            carColor = Color.White,
            imageUrl = product.imageUrl,
            productId = product.id,
            isReward = true // IMPORTANT: Mark as reward order
        )

        Logger.i("Quick Adding Reward Order: $newOrder")

        viewModelScope.launch {
            try {
                // 1. Save to Local Cart
                manageCartUseCase.insertOrder(newOrder)

//                // 2. Deduct Points Locally
//                val newPointBalance = currentState.points - finalPrice
//
//                updateState { state ->
//                    state.copy(
//                        points = newPointBalance // Update UI Points immediately
//                    )
//                }

                // 3. Show success feedback with points info
                showSnackBar(
                    message = getString(
                        Res.string.reward_redeemed_with_points_message,
                        finalPrice.toInt(),
                        currentState.points.toInt()
                    ),
                    isSuccess = true
                )

            } catch (e: Exception) {
                Logger.e("QuickAddReward", e) { "Failed to redeem reward" }
                showSnackBar(
                    message = getString(Res.string.redeem_reward_failed_message),
                    isSuccess = false
                )
            }
        }
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

}
