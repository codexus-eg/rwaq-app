package com.khater.rwaq.presentation.screens.homeScreen.uiStates

import com.khater.rwaq.domain.entities.product.Product
import com.khater.rwaq.presentation.model.SnackBarState
import com.khater.rwaq.presentation.screens.productScreen.uiState.ProductDetailsUiState
import com.khater.rwaq.presentation.screens.productScreen.uiState.ProductUiModel

data class HomeUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val snackBar: SnackBarState = SnackBarState(),
    val userName: String = "",

    // Dynamic Data
     val products: List<Product> = emptyList(), // This is the filtered list shown in UI,

    val isDetailsVisible: Boolean = false,
    val selectedProductDetails: ProductDetailsUiState? = null,
    // Search Bottom Sheet
    val isSearchVisible: Boolean = false,
    val searchQuery: String = "",
    val filteredProducts: List<ProductUiModel> = emptyList(),

    val categories: List<CategoryUi> = emptyList(), // Chips for Coffee
    val groupedProducts: Map<String, List<Product>> = emptyMap(),    // Maps Category ID to the Index in the LazyGrid (for scrolling)
    val categoryScrollIndices: Map<String, Int> = emptyMap(),
    // Order of category IDs to render them in the correct sequence
    val categoryDisplayOrder: List<String> = emptyList(),
    val selectedCategoryId: String = "",
    val coffeeProducts: List<Product> = emptyList(), // All Coffee cache
    val displayedCoffeeProducts: List<Product> = emptyList(), // Filtered by Chip

    // Section 2: Special Offers
    val specialOffers: List<Product> = emptyList(),

    // Section 3: Other Categories (e.g., Cake, Dessert)
    // Key = Category Name, Value = List of Products
    val otherSections: Map<String, List<Product>> = emptyMap(),

    // Section 4: Machines
    val machineProducts: List<Product> = emptyList(),
    val addingProductId: String? = null
)


data class CategoryUi(
    val id: String,
    val name: String
)
