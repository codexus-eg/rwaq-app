package com.khater.rwaq.presentation.screens.rewardScreen

import androidx.lifecycle.viewModelScope
import com.khater.rwaq.data.dto.cart.AddToCartRequestDto
import com.khater.rwaq.data.dto.product.toDetailsUiState
import com.khater.rwaq.domain.repository.authentication.AuthenticationRepository
import com.khater.rwaq.domain.useCases.GetAllProductsUseCase
import com.khater.rwaq.domain.useCases.cart.AddToCartUseCase
import com.khater.rwaq.domain.util.InsufficientPointsException
import com.khater.rwaq.presentation.base.BaseViewModel
import com.khater.rwaq.presentation.mapper.handleDefaultException
import com.khater.rwaq.presentation.mapper.mapDefaultErrorToMessage
import com.khater.rwaq.presentation.model.SnackBarState
import com.khater.rwaq.presentation.screens.rewardScreen.uiState.RewardInteractionListener
import com.khater.rwaq.presentation.screens.rewardScreen.uiState.RewardsUiEffect
import com.khater.rwaq.presentation.screens.rewardScreen.uiState.RewardsUiState
import com.khater.rwaq.presentation.util.LoginConstants.SNACK_BAR_DELAY
import com.khater.rwaq.presentation.util.isLoginRequiredError
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
import rwaq.composeapp.generated.resources.please_login_first
import rwaq.composeapp.generated.resources.redeem_reward_failed_message
import rwaq.composeapp.generated.resources.reward_redeemed_message
import kotlin.math.ceil
import kotlin.uuid.ExperimentalUuidApi

class NewRewardViewModel(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val authenticationRepository: AuthenticationRepository,
) : BaseViewModel<RewardsUiState, RewardsUiEffect>(RewardsUiState()),
    RewardInteractionListener {

    fun checkAuthenticationAndFetchData() {
        tryToExecute(
            callee = {
                val isLoggedIn = authenticationRepository.isUserLoggedIn().first()
                if (isLoggedIn) getAllProductsUseCase(1, 50, true) else null
            },
            onSuccess = { productsResponse ->
                updateState { state ->
                    val isLoggedIn = productsResponse != null
                    val actualServerPoints = productsResponse?.userPoints?.toDouble() ?: 0.0
                    state.copy(
                        isGuest = !isLoggedIn,
                        showGuestDialog = !isLoggedIn,
                        serverPoints = actualServerPoints,
                        points = actualServerPoints,
                        products = productsResponse?.data.orEmpty(),
                        errorMessage = null
                    )
                }
            },
            onError = {
                val mappedErrorMessage = mapErrorMessage(it)
                val errorMessage = getString(mappedErrorMessage)
                updateState { state ->
                    state.copy(errorMessage = if (state.products.isEmpty()) errorMessage else null)
                }
                showSnackBar(
                    title = getString(Res.string.error),
                    message = errorMessage,
                    isSuccess = false
                )
            },
            onStart = { updateState { it.copy(isLoading = it.products.isEmpty(), errorMessage = null) } },
            onFinish = { updateState { it.copy(isLoading = false) } }
        )
    }

    private fun mapErrorMessage(throwable: Throwable): StringResource {
        val errorState = handleDefaultException(throwable)
        return mapDefaultErrorToMessage(errorState)
    }

    override fun onRefreshUserPoints() {
        checkAuthenticationAndFetchData()
    }

    override fun onRetry() {
        checkAuthenticationAndFetchData()
    }

    override fun onBack() {
        sendNewEffect(RewardsUiEffect.NavigateBack)
    }

    override fun onClickLogin() {
        updateState { it.copy(showGuestDialog = false) }
        sendNewEffect(RewardsUiEffect.NavigateToLogin)
    }

    override fun onDismissGuestDialog() {
        updateState { it.copy(showGuestDialog = false) }
        sendNewEffect(RewardsUiEffect.NavigateBack)
    }

    override fun onProductClicked(productId: String) {
        if (blockGuestAction()) return
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

        if (newQty < 1) return

        val unitPrice = details.calculatedSingleUnitTestPrice
        val projectedTotalPrice = unitPrice * newQty

        if (change > 0 && projectedTotalPrice > currentState.points) {
            viewModelScope.launch {
                showInsufficientPointsError(
                    requiredPoints = projectedTotalPrice.toPoints(),
                    availablePoints = currentState.points.toPoints()
                )
            }
            return
        }

        updateState { state ->
            state.copy(
                selectedProductDetails = details.copy(
                    productQuantity = newQty,
                    calculatedTotalPrice = projectedTotalPrice
                )
            )
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun onAddToCart() {
        if (blockGuestAction()) return

        val details = currentState.selectedProductDetails ?: return

        if (details.calculatedTotalPrice > currentState.points) {
            viewModelScope.launch {
                showInsufficientPointsError(
                    requiredPoints = details.calculatedTotalPrice.toPoints(),
                    availablePoints = currentState.points.toPoints()
                )
            }
            return
        }

        val cartExtensions = details.extensions
            .filter { it.currentQty > 0 }
            .map { uiExt ->
                AddToCartRequestDto.CartExtensionDto(
                    extensionId = uiExt.id,
                    name = uiExt.name,
                    nameAr = uiExt.name,
                    price = uiExt.price,
                    quantity = uiExt.currentQty
                )
            }

        val selectedSize = details.sizes.find { it.id == details.selectedSizeId }

        val request = AddToCartRequestDto(
            productId = details.id,
            quantity = details.productQuantity,
            size = selectedSize?.name ?: "Default",
            sizeId = details.selectedSizeId ?: "",
            isRewardItem = true,
            extensions = cartExtensions
        )

        tryToExecute(
            callee = { addToCartUseCase(request) },
            onSuccess = { cart ->
                updateState {
                    val updatedPoints = cart.userPoints?.toDouble()
                    it.copy(
                        isDetailsVisible = false,
                        serverPoints = updatedPoints ?: it.serverPoints,
                        points = updatedPoints ?: it.points
                    )
                }
                viewModelScope.launch {
                    showSnackBar(
                        message = getString(Res.string.reward_redeemed_message),
                        isSuccess = true
                    )
                }
            },
            onError = { error ->
                showRewardError(error)
            },
            onStart = {
                updateState {
                    it.copy(
                        selectedProductDetails = it.selectedProductDetails?.copy(isAddingToCart = true)
                    )
                }
            },
            onFinish = {
                updateState {
                    it.copy(
                        selectedProductDetails = it.selectedProductDetails?.copy(isAddingToCart = false)
                    )
                }
            }
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun onQuickAddToCart(productId: String) {
        if (blockGuestAction()) return

        val product = currentState.products.find { it.id == productId } ?: return

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

        val selectedSize = product.sizes.firstOrNull()
        val rewardCost = ((selectedSize?.price ?: product.basePrice) - product.discount).coerceAtLeast(0.0)
        if (rewardCost > currentState.points) {
            viewModelScope.launch {
                showInsufficientPointsError(
                    requiredPoints = rewardCost.toPoints(),
                    availablePoints = currentState.points.toPoints()
                )
            }
            return
        }

        val request = AddToCartRequestDto(
            productId = product.id,
            quantity = 1,
            size = selectedSize?.name ?: "Default",
            sizeId = selectedSize?.id ?: "",
            isRewardItem = true,
            extensions = emptyList()
        )

        tryToExecute(
            callee = { addToCartUseCase(request) },
            onSuccess = { cart ->
                updateState {
                    val updatedPoints = cart.userPoints?.toDouble()
                    it.copy(
                        serverPoints = updatedPoints ?: it.serverPoints,
                        points = updatedPoints ?: it.points
                    )
                }
                viewModelScope.launch {
                    showSnackBar(
                        message = getString(Res.string.reward_redeemed_message),
                        isSuccess = true
                    )
                }
            },
            onError = { error ->
                showRewardError(error)
            },
            onStart = { updateState { it.copy(addingProductId = productId) } },
            onFinish = { updateState { it.copy(addingProductId = null) } }
        )
    }

    private suspend fun showRewardError(error: Throwable) {
        if (error.isLoginRequiredError()) {
            updateState {
                it.copy(
                    isGuest = true,
                    showGuestDialog = true,
                    addingProductId = null,
                    selectedProductDetails = it.selectedProductDetails?.copy(isAddingToCart = false)
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
                updateState {
                    it.copy(
                        serverPoints = points.toDouble(),
                        points = points.toDouble()
                    )
                }
            }
            showInsufficientPointsError(
                requiredPoints = error.requiredPoints,
                availablePoints = error.availablePoints,
                fallbackMessage = error.message
            )
            return
        }

        showSnackBar(
            message = error.message ?: getString(Res.string.redeem_reward_failed_message),
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
            fallbackMessage ?: getString(Res.string.redeem_reward_failed_message)
        }
        showSnackBar(
            title = getString(Res.string.insufficient_points_title),
            message = message,
            isSuccess = false
        )
    }

    private fun Double.toPoints(): Int = ceil(this).toInt()

    private fun blockGuestAction(): Boolean {
        if (!currentState.isGuest) return false
        updateState { it.copy(showGuestDialog = true) }
        viewModelScope.launch {
            showSnackBar(
                message = getString(Res.string.please_login_first),
                isSuccess = false
            )
        }
        return true
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
