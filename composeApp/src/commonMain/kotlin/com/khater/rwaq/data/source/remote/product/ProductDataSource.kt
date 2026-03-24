package com.khater.rwaq.data.source.remote.product

import com.khater.rwaq.data.dto.base.PagedDataDto
import com.khater.rwaq.data.dto.product.ProductDto
import com.khater.rwaq.domain.entities.product.Product
import com.khater.rwaq.domain.model.PagedData

interface ProductDataSource {
    suspend fun getAllProduct(page: Int, pageSize: Int,isReward: Boolean): PagedData<Product>
}