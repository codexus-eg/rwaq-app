package com.khater.rwaq.presentation.screens.rewardScreen.uiState

interface RewardInteractionListener {
    fun onRetry()
    fun onRefreshUserPoints()
    fun onBack()
    fun onProductClicked(productId: String)
    fun onDismissDetails()
    fun onQuantityChange(change: Int)
    fun onAddToCart()

    fun onQuickAddToCart(productId: String)

}