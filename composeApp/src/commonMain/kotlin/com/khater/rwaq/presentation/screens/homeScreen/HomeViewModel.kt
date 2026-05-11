package com.khater.rwaq.presentation.screens.homeScreen

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.khater.rwaq.data.dto.product.toDetailsUiState
import com.khater.rwaq.data.dto.product.toUiModel
import com.khater.rwaq.data.util.username
import com.khater.rwaq.data.dto.cart.AddToCartRequestDto
import com.khater.rwaq.domain.entities.order.Order
import com.khater.rwaq.domain.entities.order.OrderExtension
import com.khater.rwaq.domain.entities.product.Product
import com.khater.rwaq.domain.repository.authentication.AuthenticationRepository
import com.khater.rwaq.domain.useCases.GetAllProductsUseCase
import com.khater.rwaq.domain.useCases.ManageCartUseCase
import com.khater.rwaq.domain.useCases.cart.AddToCartUseCase
import com.khater.rwaq.presentation.base.BaseViewModel
import com.khater.rwaq.presentation.mapper.handleDefaultException
import com.khater.rwaq.presentation.mapper.mapDefaultErrorToMessage
import com.khater.rwaq.presentation.model.SnackBarState
import com.khater.rwaq.presentation.screens.homeScreen.uiStates.CategoryUi
import com.khater.rwaq.presentation.screens.homeScreen.uiStates.HomeScreenInteractionListener
import com.khater.rwaq.presentation.screens.homeScreen.uiStates.HomeUiEffect
import com.khater.rwaq.presentation.screens.homeScreen.uiStates.HomeUiState
import com.khater.rwaq.presentation.screens.productScreen.uiState.ProductDetailsUiState
import com.khater.rwaq.presentation.util.LoginConstants.SNACK_BAR_DELAY
import com.khater.rwaq.presentation.util.isLoginRequiredError
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.add_to_cart_error_message
import rwaq.composeapp.generated.resources.added_to_cart_message
import rwaq.composeapp.generated.resources.added_to_cart_title
import rwaq.composeapp.generated.resources.all_coffee
import rwaq.composeapp.generated.resources.checkout_order
import rwaq.composeapp.generated.resources.error_title
import rwaq.composeapp.generated.resources.max_count
import rwaq.composeapp.generated.resources.out_of_stock_message
import rwaq.composeapp.generated.resources.out_of_stock_title
import rwaq.composeapp.generated.resources.please_login_first
import rwaq.composeapp.generated.resources.special_offers
import rwaq.composeapp.generated.resources.you_have_reach_max_count_of_this_extension
import kotlin.time.Clock.System
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalSettingsApi::class,ExperimentalTime::class)
class HomeViewModel(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val authenticationRepository: AuthenticationRepository,
    private val settings: FlowSettings,
) : BaseViewModel<HomeUiState, HomeUiEffect>(HomeUiState()),
    HomeScreenInteractionListener {
     private var cachedProducts: List<Product> = emptyList()
    private var lastScrollRequest: Long = 0
    private val scrollDebounceMs = 300L
    init {
        getUser()
        fetchHomeData()
    }

    private fun getUser() {
        viewModelScope.launch {
            val username = settings.username.first()
            updateState { it.copy(userName = username) }
        }
    }

//    private fun fetchHomeData() {
//        updateState { it.copy(isLoading = true) }
//
//        viewModelScope.launch {
//            try {
//                // 1. Fetch Raw Data
//                val response = getAllProductsUseCase(1, 50,false) // Fetch enough items
//                val allData = response.data
//                Logger.i { "labwlaiv $allData" }
//
//                cachedProducts = (cachedProducts + allData ).distinctBy { it.id }
//                // --- SEGMENTATION LOGIC ---
//
//                // A. Coffee Section (isCoffee = true)
//                val coffeeList = allData.filter { it.isCoffee }
//
//                //    Generate Coffee Chips
//                val coffeeCategories = coffeeList.map { it.category }.distinct()
//                    .map { CategoryUi(it, it) }
//                val allChip = CategoryUi("ALL", getString(Res.string.all_coffee))
//                val finalChips = listOf(allChip) + coffeeCategories
//
//
//                // B. Special Offers (isSpecialOffer = true)
//                val offersList = allData.filter { it.isSpecialOffer }
//
//
//                // C. Machines (isMachine = true)
//                val machineList = allData.filter { it.isMachine }
//
//
//                // D. "Other" Categories
//                // Logic: Not Coffee, Not Machine, Not Special Offer (optional: or keep offers in their cat too)
//                // Assuming "Other" implies things that didn't fit in the main groups:
//                val otherList = allData.filter {
//                    !it.isCoffee && !it.isMachine && !it.isSpecialOffer
//                }
//
//                // Group by Category Name: { "Cake" -> [p1, p2], "Dessert" -> [p3] }
//                val otherSectionsMap = otherList.groupBy { it.category }
//
//
//                // 3. Update State
//                updateState {
//                    it.copy(
//                        isLoading = false,
//                        errorMessage = null,
//                        // Coffee State
//                        categories = finalChips,
//                        coffeeProducts = coffeeList,
//                        displayedCoffeeProducts = coffeeList, // Initially show all coffee
//                        selectedCategoryId = "ALL",
//
//                        // Other Sections
//                        specialOffers = offersList,
//                        machineProducts = machineList,
//                        otherSections = otherSectionsMap,
//                        products = allData
//                    )
//                }
//
//            } catch (e: Throwable) {
//                val mappedErrorMessage = mapErrorMessage(e)
//                val errorMessage = getString(mappedErrorMessage)
//                updateState { it.copy(isLoading = false, errorMessage =errorMessage) }
//            }
//        }
//    }
private fun fetchHomeData() {
    updateState { it.copy(isLoading = true) }

    viewModelScope.launch {
        try {
            val response = getAllProductsUseCase(1, 50, false)
            val allData = response.data

            val specialOffersList = allData.filter { it.isSpecialOffer }
            val normalGroups = allData.groupBy { it.category }

            val displayMap = LinkedHashMap<String, List<Product>>()
            val specialOffersId = getString(Res.string.special_offers)

            if (specialOffersList.isNotEmpty()) {
                displayMap[specialOffersId] = specialOffersList
            }

            val sortedNormalKeys = normalGroups.keys.sortedBy { categoryName ->
                val product = normalGroups[categoryName]?.firstOrNull()
                when {
                    product?.isCoffee == true -> 2
                    product?.isMachine == false -> 3
                    product?.isMachine == true -> 4
                    else -> 5
                }
            }

            sortedNormalKeys.forEach { key ->
                displayMap[key] = normalGroups[key] ?: emptyList()
            }

            val allSectionIds = displayMap.keys.toList()
            val categoryChips = allSectionIds.map { sectionId ->
                CategoryUi(id = sectionId, name = sectionId)
            }

            // Calculate scroll indices with better precision
            val indicesMap = mutableMapOf<String, Int>()
            var currentIndex = 2 // Profile(0) + Search(1) + Chips(1) = 2

            allSectionIds.forEach { sectionId ->
                indicesMap[sectionId] = currentIndex
                val count = displayMap[sectionId]?.size ?: 0
                currentIndex += 1 + count // Header + Products
            }

            cachedProducts = allData

            updateState {
                it.copy(
                    isLoading = false,
                    errorMessage = null,
                    products = allData,
                    categories = categoryChips,
                    groupedProducts = displayMap,
                    categoryDisplayOrder = allSectionIds,
                    categoryScrollIndices = indicesMap,
                    selectedCategoryId = allSectionIds.firstOrNull() ?: ""
                )
            }

        } catch (e: Throwable) {
            val mappedErrorMessage = mapErrorMessage(e)
            val errorMessage = getString(mappedErrorMessage)
            updateState { it.copy(isLoading = false, errorMessage = errorMessage) }
        }
    }
}

    override fun onSelectCategory(categoryId: String) {
        val currentTime = System.now().toEpochMilliseconds()

        // Debounce rapid clicks
        if (currentTime - lastScrollRequest < scrollDebounceMs) return
        lastScrollRequest = currentTime

        // Update selected category
        updateState { it.copy(selectedCategoryId = categoryId) }

        // Trigger smooth scroll with slight delay for better UX
        viewModelScope.launch {
            delay(50) // Small delay to ensure state is updated

            val indexToScroll = state.value.categoryScrollIndices[categoryId]
            if (indexToScroll != null) {
                sendNewEffect(HomeUiEffect.ScrollToCategory(indexToScroll))
            }
        }
    }


    fun onManualScroll(newCategoryId: String) {
        // Debounce rapid updates
        val currentTime = System.now().toEpochMilliseconds()
        if (currentTime - lastScrollRequest < 10) return

        if (state.value.selectedCategoryId != newCategoryId) {
            updateState { it.copy(selectedCategoryId = newCategoryId) }
            lastScrollRequest = currentTime
        }
    }

    private fun mapErrorMessage(throwable: Throwable): StringResource {
        val errorState = handleDefaultException(throwable)
        return mapDefaultErrorToMessage(errorState)
    }
    // Filter Logic ONLY affects the Coffee Section
//    override fun onSelectCategory(categoryId: String) {
//        val filtered = if (categoryId == "ALL") {
//            state.value.coffeeProducts
//        } else {
//            state.value.coffeeProducts.filter { it.category == categoryId }
//        }
//        updateState { it.copy(selectedCategoryId = categoryId, displayedCoffeeProducts = filtered) }
//    }

//    override fun onSelectCategory(categoryId: String) {
//        // 1. Update the UI visually (highlight the chip)
//        updateState { it.copy(selectedCategoryId = categoryId) }
//
//        // 2. Trigger the Scroll ONLY on user interaction
//        val indexToScroll = state.value.categoryScrollIndices[categoryId]
//        if (indexToScroll != null) {
//            sendNewEffect(HomeUiEffect.ScrollToCategory(indexToScroll))
//        }
//    }
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

    override fun onToggleExtension(extensionId: String) {
     }

    override fun onQuantityChange(change: Int) {
        val currentDetails = state.value.selectedProductDetails ?: return
        val newQty = (currentDetails.productQuantity + change).coerceAtLeast(1)

        val newDetails = currentDetails.copy(productQuantity = newQty)
        recalculatePrice(newDetails)
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun onAddToCart() {
        val details = state.value.selectedProductDetails ?: return
        viewModelScope.launch {
            if (isGuestUser()) {
                showLoginRequiredSnackBar()
                return@launch
            }

            addSelectedProductToCart(details)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun onQuickAddToCart(productId: String) {
        val product = cachedProducts.find { it.id == productId } ?: return

        viewModelScope.launch {
            if (isGuestUser()) {
                showLoginRequiredSnackBar()
                return@launch
            }

            if (!product.isInStock) {
                showSnackBar(
                    title = getString(Res.string.out_of_stock_title),
                    message = getString(Res.string.out_of_stock_message, product.name),
                    isSuccess = false
                )
                return@launch
            }

            quickAddProductToCart(product)
        }
    }

    private fun addSelectedProductToCart(details: ProductDetailsUiState) {
        val orderExtensions = details.extensions
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
            size = selectedSize?.name ?: "Standard",
            sizeId = details.selectedSizeId ?: "",
            isRewardItem = false,
            extensions = orderExtensions
        )

        tryToExecute(
            callee = { addToCartUseCase(request) },
            onSuccess = {
                updateState { it.copy(selectedProductDetails = it.selectedProductDetails?.copy(isAddingToCart = false)) }
                onDismissDetails()
                showSnackBar(
                    title = getString(Res.string.added_to_cart_title),
                    message = getString(Res.string.added_to_cart_message, details.name),
                    isSuccess = true
                )
            },
            onError = { error ->
                updateState { it.copy(selectedProductDetails = it.selectedProductDetails?.copy(isAddingToCart = false)) }
                showAddToCartError(error)
            },
            onStart = {
                updateState {
                    it.copy(
                        selectedProductDetails = it.selectedProductDetails?.copy(
                            isAddingToCart = true
                        )
                    )
                }
            },
            onFinish = { }
        )
    }

    private fun quickAddProductToCart(product: Product) {
        val request = AddToCartRequestDto(
            productId = product.id,
            quantity = 1,
            size = "Standard",
            sizeId = "",
            isRewardItem = false,
            extensions = emptyList()
        )

        tryToExecute(
            callee = { addToCartUseCase(request) },
            onSuccess = {
                showSnackBar(
                    title = getString(Res.string.added_to_cart_title),
                    message = getString(Res.string.added_to_cart_message, product.name),
                    isSuccess = true,
                    durationMillis = 2000
                )
            },
            onError = { error ->
                updateState { it.copy(selectedProductDetails = it.selectedProductDetails?.copy(isAddingToCart = false)) }
                showAddToCartError(error)
            },
            onStart = { updateState { it.copy(addingProductId = product.id) } },
            onFinish = { updateState { it.copy(addingProductId = null) } }
        )
    }

    private suspend fun isGuestUser(): Boolean =
        !authenticationRepository.isUserLoggedIn().first()

    private suspend fun showLoginRequiredSnackBar() {
        showSnackBar(
            title = getString(Res.string.error_title),
            message = getString(Res.string.please_login_first),
            isSuccess = false
        )
    }

    private suspend fun showAddToCartError(error: Throwable) {
        showSnackBar(
            title = getString(Res.string.error_title),
            message = if (error.isLoginRequiredError()) {
                getString(Res.string.please_login_first)
            } else {
                error.message ?: getString(Res.string.add_to_cart_error_message)
            },
            isSuccess = false
        )
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

    override fun onProductsScrolled() {

    }

    override fun onOpenSearch() {
        updateState {
            it.copy(
                isSearchVisible = true,
                searchQuery = "",
                filteredProducts = it.products.map { it.toUiModel() }
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
                filteredProducts = filtered.map { it.toUiModel() }
            )
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


    private fun recalculatePrice(details: ProductDetailsUiState) {
        // 1. Find the original product to access the 'discount' field
        val originalProduct = cachedProducts.find { it.id == details.id }

        // 2. Get the RAW price (Either from selected size OR the product base price)
        val rawPrice = details.sizes.find { it.id == details.selectedSizeId }?.price
            ?: originalProduct?.basePrice
            ?: 0.0

        // 3. Get the discount amount
        val discountAmount = originalProduct?.discount ?: 0.0

        // 4. Calculate the Final Unit Price (Raw - Discount)
        // We use coerceAtLeast(0.0) to ensure price never goes negative
        val finalUnitPrice = (rawPrice - discountAmount).coerceAtLeast(0.0)

        // 5. Calculate Total Product Price (Final Unit Price × Quantity)
        val totalProductPrice = finalUnitPrice * details.productQuantity

        // 6. Calculate Extensions Price (remains the same)
        val totalExtensionsPrice = details.extensions.sumOf {
            it.price * it.currentQty
        }

        // 7. Final Sum
        val finalTotal = totalProductPrice + totalExtensionsPrice

        updateState { state ->
            state.copy(
                selectedProductDetails = details.copy(
                    calculatedSingleUnitTestPrice = finalUnitPrice, // Store the discounted unit price
                    calculatedTotalPrice = finalTotal
                )
            )
        }
    }
    override fun onClickPickUpFormBranch() {
        sendNewEffect(
            HomeUiEffect.NavigateToPickUpFromBranch
        )
    }

    override fun onClickPickUpFormCar() {
        sendNewEffect(
            HomeUiEffect.NavigateToPickUpFromCar
        )
    }

    override fun onRetry() {
        fetchHomeData()
    }
    companion object {
        const val PAGE_SIZE = 40
        const val INITIAL_PAGE = 1
    }
}
