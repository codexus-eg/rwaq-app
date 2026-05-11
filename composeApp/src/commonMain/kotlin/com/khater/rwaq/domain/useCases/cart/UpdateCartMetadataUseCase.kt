package com.khater.rwaq.domain.useCases.cart

import com.khater.rwaq.data.dto.cart.UpdateCartMetadataRequestDto
import com.khater.rwaq.domain.repository.cart.CartRepository

class UpdateCartMetadataUseCase(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(request: UpdateCartMetadataRequestDto) =
        cartRepository.updateCartMetadata(request)
}
