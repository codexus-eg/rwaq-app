package com.khater.rwaq.domain.useCases.cart

import com.khater.rwaq.data.dto.cart.AddToCartRequestDto
import com.khater.rwaq.domain.entities.cart.Cart
import com.khater.rwaq.domain.repository.cart.CartRepository

class AddToCartUseCase(private val repository: CartRepository) {
    suspend operator fun invoke(request: AddToCartRequestDto): Cart =
        repository.addItemToCart(request)
}
