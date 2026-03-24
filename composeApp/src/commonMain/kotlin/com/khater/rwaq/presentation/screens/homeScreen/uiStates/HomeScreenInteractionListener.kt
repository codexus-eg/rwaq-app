package com.khater.rwaq.presentation.screens.homeScreen.uiStates

interface HomeScreenInteractionListener {
    fun onClickPickUpFormBranch()
    fun onClickPickUpFormCar()
    fun onRetry()
    fun onSelectCategory(categoryId: String)
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


    fun onQuickAddToCart(productId: String)

}