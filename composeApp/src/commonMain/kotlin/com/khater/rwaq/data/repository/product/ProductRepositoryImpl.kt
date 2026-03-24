package com.khater.rwaq.data.repository.product

import com.khater.rwaq.data.dto.product.toEntity
import com.khater.rwaq.data.source.remote.product.ProductDataSource
import com.khater.rwaq.domain.entities.product.Product
import com.khater.rwaq.domain.model.PagedData
import com.khater.rwaq.domain.repository.product.ProductRepository

class ProductRepositoryImpl(
    private val productDataSource: ProductDataSource
): ProductRepository {
    override suspend fun getAllProducts(page: Int, pageSize: Int,isReward: Boolean): PagedData<Product> {
        return productDataSource.getAllProduct(page = page,pageSize = pageSize,isReward = isReward)
    }
}