package com.khater.rwaq.presentation.screens.rewardScreen.uiState

import com.khater.rwaq.domain.entities.product.Product
import com.khater.rwaq.presentation.model.SnackBarState
import com.khater.rwaq.presentation.screens.productScreen.uiState.ProductDetailsUiState
import com.khater.rwaq.presentation.screens.productScreen.uiState.ProductUiModel

data class RewardsUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val snackBar: SnackBarState = SnackBarState(),
    val serverPoints: Double = 0.0,
    val pendingRewardPoints: Double = 0.0,
    val points: Double = 0.0,
    val products: List<Product> = emptyList(),
    val uiProducts: List<ProductUiModel> = emptyList(),
    val selectedProductDetails: ProductDetailsUiState? = null,
    val isDetailsVisible: Boolean = false,
    val isGuest: Boolean = false,
    val showGuestDialog: Boolean = false,
    val addingProductId: String? = null,
)
