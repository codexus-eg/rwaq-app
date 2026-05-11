package com.khater.rwaq.domain.useCases.cart

import com.khater.rwaq.domain.entities.cart.Cart
import com.khater.rwaq.domain.repository.cart.CartRepository

class GetCartUseCase(private val repository: CartRepository) {
    suspend operator fun invoke(): Cart = repository.getCart()
}
