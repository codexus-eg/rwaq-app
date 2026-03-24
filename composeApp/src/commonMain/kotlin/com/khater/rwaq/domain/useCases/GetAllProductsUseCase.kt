package com.khater.rwaq.domain.useCases

import com.khater.rwaq.domain.repository.product.ProductRepository

class GetAllProductsUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(page: Int, pageSize: Int,isReward: Boolean) = productRepository.getAllProducts(page=page,pageSize = pageSize,isReward = isReward)
}