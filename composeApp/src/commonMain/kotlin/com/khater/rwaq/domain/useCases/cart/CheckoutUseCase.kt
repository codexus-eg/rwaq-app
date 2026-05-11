package com.khater.rwaq.domain.useCases.cart

import com.khater.rwaq.data.dto.cart.CheckoutRequestDto
import com.khater.rwaq.domain.entities.cart.CheckoutResult
import com.khater.rwaq.domain.repository.cart.CartRepository

class CheckoutUseCase(private val repository: CartRepository) {
    suspend operator fun invoke(request: CheckoutRequestDto): CheckoutResult =
        repository.checkout(request)
}
