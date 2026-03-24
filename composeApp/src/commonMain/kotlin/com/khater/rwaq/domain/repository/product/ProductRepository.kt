package com.khater.rwaq.domain.repository.product

import com.khater.rwaq.domain.entities.product.Product
import com.khater.rwaq.domain.model.PagedData

interface ProductRepository {
    suspend fun getAllProducts(page: Int, pageSize: Int,isReward: Boolean): PagedData<Product>
}