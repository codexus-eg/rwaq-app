package com.khater.rwaq.domain.useCases.cart

import com.khater.rwaq.data.dto.cart.UpdateCartItemRequestDto
import com.khater.rwaq.domain.entities.cart.Cart
import com.khater.rwaq.domain.repository.cart.CartRepository

class UpdateCartItemUseCase(private val repository: CartRepository) {
    suspend operator fun invoke(itemId: String, request: UpdateCartItemRequestDto): Cart =
        repository.updateCartItem(itemId, request)
}
