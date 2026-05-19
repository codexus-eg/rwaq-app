package com.khater.rwaq.domain.useCases.cart

import com.khater.rwaq.domain.repository.cart.CartRepository
import kotlinx.coroutines.flow.Flow

class ObserveCartBadgeCountUseCase(private val repository: CartRepository) {
    operator fun invoke(): Flow<Int> = repository.observeCartBadgeCount()
}
