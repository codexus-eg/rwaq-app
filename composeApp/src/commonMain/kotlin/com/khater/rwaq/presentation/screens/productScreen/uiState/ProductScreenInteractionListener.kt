package com.khater.rwaq.presentation.screens.productScreen.uiState

interface ProductScreenInteractionListener {
    fun onBack()
    fun onRetry()

    fun onProductClicked(productId: String)
    fun onDismissDetails()
    fun onSelectSize(sizeId: String)
    fun onToggleExtension(extensionId: String)
    fun onQuantityChange(change: Int)
    fun onAddToCart()
    fun onExtensionQuantityChange(extensionId: String, change: Int)
    fun onProductsScrolled()

    fun onOpenSearch()
    fun onDismissSearch()
    fun onSearchQueryChange(query: String)
}