package com.khater.rwaq.presentation.screens.productScreen.uiState

import com.khater.rwaq.presentation.model.SnackBarState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.CarUiState

data class ProductScreenUiState(
    val isLoading: Boolean = false,
    val products: List<ProductUiModel> = emptyList(),
    val categories: List<String> = emptyList(),
    val selectedCategory: String = "All",
    val groupedProducts: Map<String, List<ProductUiModel>> = emptyMap(),
    // When this is not null, show the BottomSheet/Dialog
    val isDetailsVisible: Boolean = false,
    val selectedProductDetails: ProductDetailsUiState? = null,
    // Search Bottom Sheet
    val isSearchVisible: Boolean = false,
    val searchQuery: String = "",
    val filteredProducts: List<ProductUiModel> = emptyList(),
    val errorMessage: String? = null,
    val snackBar: SnackBarState = SnackBarState(),

    val isPickupFormBranch: Boolean = false,
    val selectedCar: CarUiState = CarUiState(),
    val branchName: String = ""
)


// 2. The List Item Model
data class ProductUiModel(
    val id: String,
    val name: String,
    val imageUrl: String,
    val price: Double,
    val category: String,
    val formattedPrice: String = "$price SAR",
    val discount: String = ""
)

// 3. The Details/BottomSheet State (Handles Calculations)
data class ProductDetailsUiState(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val sizes: List<ProductSizeUiModel>,
    val extensions: List<ProductExtensionUiModel>,

    // User Selections
    val selectedSizeId: String?,
    val selectedExtensionIds: Set<String> = emptySet(),
    val productQuantity: Int = 1,

    // Calculated Fields
    val calculatedSingleUnitTestPrice: Double,
    val calculatedTotalPrice: Double,
    val hasCashback: Boolean,
    val cashBackAmount: Double,
    val isAddingToCart: Boolean = false
)

data class ProductSizeUiModel(
    val id: String,
    val name: String,
    val price: Double,
    val isSelected: Boolean
)

data class ProductExtensionUiModel(
    val id: String,
    val name: String,
    val price: Double,
    val isSelected: Boolean = false,
    val maxCount: Int, // From API
    val currentQty: Int = 0
)