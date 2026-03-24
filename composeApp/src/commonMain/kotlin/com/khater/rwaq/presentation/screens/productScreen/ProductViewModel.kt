package com.khater.rwaq.presentation.screens.productScreen

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import co.touchlab.kermit.Logger
import com.khater.rwaq.data.dto.product.toDetailsUiState
import com.khater.rwaq.data.dto.product.toUiModel
import com.khater.rwaq.domain.entities.order.Order
import com.khater.rwaq.domain.entities.order.OrderExtension
import com.khater.rwaq.domain.entities.product.Product
import com.khater.rwaq.domain.model.PagedData
import com.khater.rwaq.domain.useCases.GetAllProductsUseCase
import com.khater.rwaq.domain.useCases.ManageCartUseCase
import com.khater.rwaq.presentation.base.BaseViewModel
import com.khater.rwaq.presentation.model.SnackBarState
import com.khater.rwaq.presentation.navigation.Screen
import com.khater.rwaq.presentation.screens.branchScreen.uiState.CarUiState
import com.khater.rwaq.presentation.screens.productScreen.uiState.*
import com.khater.rwaq.presentation.util.LoginConstants.SNACK_BAR_DELAY
import com.khater.rwaq.presentation.util.Paginator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jetbrains.compose.resources.getString
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.max_count
import rwaq.composeapp.generated.resources.you_have_reach_max_count_of_this_extension
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ProductViewModel(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val manageCartUseCase: ManageCartUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ProductScreenUiState, ProductScreenUiEffect>(ProductScreenUiState()),
    ProductScreenInteractionListener {

    val args = savedStateHandle.toRoute<Screen.ProductScreen>()


    private var cachedProducts: List<Product> = emptyList()


    private val productPaginator by lazy {
        Paginator(
            initialKey = INITIAL_PAGE,
            onLoadUpdated = { isLoading ->
                // Only show full screen loading for the first page
                // For subsequent pages, you might want a different loading indicator (pagination loader)
                if (state.value.products.isEmpty()) {
                    updateState { it.copy(isLoading = isLoading) }
                }
            },
            onRequest = ::getAllProducts,
            getNextKey = { currentPage, _ -> currentPage + 1 },
            onSuccess = { result, _ ->
                cachedProducts = (cachedProducts + result.data).distinctBy { it.id }
                handleProductSuccess(result)
            },
            endReached = { _, result -> result.isLastPage },
            onError = { error ->
                updateState { it.copy(isLoading = false) }
            }
        )
    }

    init {
        // Initialize State with Args
        updateState {
            it.copy(
                isPickupFormBranch = args.isPickupFromBranch,
                selectedCar = CarUiState(
                    name = args.carName,
                    carNumber = args.carNumber,
                    color = Color(args.carColor)
                ),
                branchName = args.branchName,
                isLoading = true // Ensure loading is true at start
            )
        }

        // Start collecting data changes


        // Trigger first load
        loadNextPage()
    }

    private fun loadNextPage() {
        viewModelScope.launch {
            productPaginator.loadNextItems()
        }
    }


    private suspend fun getAllProducts(page: Int): PagedData<Product> {
        val result = getAllProductsUseCase.invoke(
            page = page,
            pageSize = PAGE_SIZE,
            isReward = false

        )
        return result
    }



    private fun handleProductSuccess(result: PagedData<Product>) {
        val newUiProducts = result.data.map { it.toUiModel() }

        updateState { currentState ->
            // Combine existing UI products with new ones
            val combinedProducts = (currentState.products + newUiProducts)
                .distinctBy { it.id } // Avoid duplicates

            // Re-calculate grouping based on the full list
            val grouped = combinedProducts.groupBy { it.category }

            currentState.copy(
                isLoading = false,
                errorMessage = null,
                products = combinedProducts,
                groupedProducts = grouped
            )
        }
    }



    override fun onRetry() {
        updateState { it.copy(isLoading = true, errorMessage = null) }
        productPaginator.reset() // You might need a reset function in your Paginator to clear keys
        loadNextPage()
    }


    override fun onProductsScrolled() {
        loadNextPage()
    }
//    private fun getProducts() {
//        tryToExecute(
//            callee = { getAllProductsUseCase() },
//            onStart = {
//                updateState { it.copy(isLoading = true, errorMessage = null) }
//            },
//            onSuccess = { products ->
//                cachedProducts = products
//                val uiProducts = products.map { it.toUiModel() }
//                val grouped = uiProducts.groupBy { it.category }
//
//                updateState {
//                    it.copy(
//                        isLoading = false,
//                        errorMessage = null,
//                        products = uiProducts,
//                        groupedProducts = grouped
//                    )
//                }
//            },
//            onError = { throwable ->
//                updateState {
//                    it.copy(isLoading = false, errorMessage = throwable.message)
//                }
//            }
//        )
//    }

    override fun onOpenSearch() {
        updateState {
            it.copy(
                isSearchVisible = true,
                searchQuery = "",
                filteredProducts = it.products
            )
        }
    }

    override fun onDismissSearch() {
        updateState {
            it.copy(
                isSearchVisible = false,
                searchQuery = "",
                filteredProducts = emptyList()
            )
        }
    }

    override fun onSearchQueryChange(query: String) {
        val trimmedQuery = query.trim()

        val filtered = if (trimmedQuery.isEmpty()) {
            currentState.products
        } else {
            currentState.products.filter {
                it.name.contains(trimmedQuery, ignoreCase = true)
            }
        }

        updateState {
            it.copy(
                searchQuery = query,
                filteredProducts = filtered
            )
        }
    }


    override fun onProductClicked(productId: String) {
        cachedProducts.find { it.id == productId }?.let { product ->
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

    override fun onSelectSize(sizeId: String) {
        val currentDetails = state.value.selectedProductDetails ?: return

        val updatedSizes = currentDetails.sizes.map {
            it.copy(isSelected = it.id == sizeId)
        }

        val newDetails = currentDetails.copy(
            selectedSizeId = sizeId,
            sizes = updatedSizes
        )
        recalculatePrice(newDetails)
    }

    override fun onExtensionQuantityChange(extensionId: String, change: Int) {
        val currentDetails = state.value.selectedProductDetails ?: return

        // 1. Find the target extension first to check limits
        val targetExtension = currentDetails.extensions.find { it.id == extensionId } ?: return

        // 2. Check if we are trying to increase (+) AND we have already reached/exceeded the max count
        if (change > 0 && targetExtension.currentQty >= targetExtension.maxCount) {
            viewModelScope.launch {
                showSnackBar(
                    title = getString(Res.string.max_count),
                    message = getString(Res.string.you_have_reach_max_count_of_this_extension),
                    isSuccess = false
                )
            }
            return // Stop here, do not update the state
        }

        // 3. If valid, proceed with the update
        val updatedExtensions = currentDetails.extensions.map { ext ->
            if (ext.id == extensionId) {
                val newQty = (ext.currentQty + change).coerceIn(0, ext.maxCount)
                ext.copy(currentQty = newQty)
            } else {
                ext
            }
        }

        val newDetails = currentDetails.copy(extensions = updatedExtensions)
        recalculatePrice(newDetails)
    }

    override fun onQuantityChange(change: Int) {
        val currentDetails = state.value.selectedProductDetails ?: return
        val newQty = (currentDetails.productQuantity + change).coerceAtLeast(1)

        val newDetails = currentDetails.copy(productQuantity = newQty)
        recalculatePrice(newDetails)
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

    /**
     * Price Calculation Logic - DO NOT MODIFY
     * This correctly calculates:
     * 1. Base price from selected size or product default
     * 2. Total product price = base price × product quantity
     * 3. Total extensions price = sum(extension price × extension quantity)
     * 4. Final total = product total + extensions total
     */
    private fun recalculatePrice(details: ProductDetailsUiState) {
        // Get base price from selected size or product default
        val basePrice = details.sizes.find { it.id == details.selectedSizeId }?.price
            ?: cachedProducts.find { it.id == details.id }?.basePrice
            ?: 0.0

        // Calculate total product price (base × quantity)
        val totalProductPrice = basePrice * details.productQuantity

        // Calculate total extensions price (sum of each extension × its quantity)
        val totalExtensionsPrice = details.extensions.sumOf {
            it.price * it.currentQty
        }

        // Final total
        val finalTotal = totalProductPrice + totalExtensionsPrice

        updateState { state ->
            state.copy(
                selectedProductDetails = details.copy(
                    calculatedSingleUnitTestPrice = basePrice,
                    calculatedTotalPrice = finalTotal
                )
            )
        }
    }

    override fun onToggleExtension(extensionId: String) {
        // Not used with new quantity-based extensions
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun onAddToCart() {
        // TODO: Implement cart logic

        // here i need to insert Order
        val details = state.value.selectedProductDetails ?: return
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
            id = Uuid.random().toString(), // Generate unique ID for Local DB
            name = details.name,
            totalPrice = details.calculatedTotalPrice, // (Base + Exts) * Count
            itemPrice = details.calculatedSingleUnitTestPrice, // Base Price per unit
            count = details.productQuantity,
            size = selectedSizeName,
            extension = orderExtensions,

            // --- NEW DATA FROM NAVIGATION ARGS ---
            branchId = args.branchId, // Ensure branchId is inside Screen.ProductScreen
            branchName = args.branchName,
            isPickupFromBranch = args.isPickupFromBranch,
            // If it is pickup from branch, we might not need car info,
            // but we save it if provided.
            carName = args.carName,
            carNumber = args.carNumber,
            carColor = Color(args.carColor),
            imageUrl = details.imageUrl,
            productId = details.id,
            isReward = false
        )

        Logger.i("Aibviabvaaa$newOrder")

        viewModelScope.launch {
            manageCartUseCase.insertOrder(newOrder)

            // E. Close details and navigate back
            onDismissDetails()
            // sendNewEffect(ProductScreenUiEffect.NavigateBack)
        }
    }

    override fun onBack() {
        sendNewEffect(ProductScreenUiEffect.NavigateBack)
    }


    companion object {
        const val PAGE_SIZE = 40
        const val INITIAL_PAGE = 1
    }
}