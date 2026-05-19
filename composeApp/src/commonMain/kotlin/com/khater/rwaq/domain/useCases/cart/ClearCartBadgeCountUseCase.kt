package com.khater.rwaq.domain.useCases.cart

import com.khater.rwaq.domain.repository.cart.CartRepository

class ClearCartBadgeCountUseCase(private val repository: CartRepository) {
    suspend operator fun invoke() {
        repository.clearCartBadgeCount()
    }
}
