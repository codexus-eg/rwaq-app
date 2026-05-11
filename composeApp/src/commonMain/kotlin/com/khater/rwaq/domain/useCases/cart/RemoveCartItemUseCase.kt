package com.khater.rwaq.domain.useCases.cart

import com.khater.rwaq.domain.entities.cart.Cart
import com.khater.rwaq.domain.repository.cart.CartRepository

class RemoveCartItemUseCase(private val repository: CartRepository) {
    suspend operator fun invoke(itemId: String): Cart = repository.removeCartItem(itemId)
}
