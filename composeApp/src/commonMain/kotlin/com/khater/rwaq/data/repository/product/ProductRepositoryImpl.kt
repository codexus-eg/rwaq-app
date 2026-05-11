package com.khater.rwaq.data.repository.product

import com.khater.rwaq.data.util.putUserPoints
import com.khater.rwaq.data.source.remote.product.ProductDataSource
import com.khater.rwaq.domain.entities.product.Product
import com.khater.rwaq.domain.model.PagedData
import com.khater.rwaq.domain.repository.product.ProductRepository
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings

@OptIn(ExperimentalSettingsApi::class)
class ProductRepositoryImpl(
    private val productDataSource: ProductDataSource,
    private val settings: FlowSettings,
): ProductRepository {
    override suspend fun getAllProducts(page: Int, pageSize: Int,isReward: Boolean): PagedData<Product> {
        val products = productDataSource.getAllProduct(page = page,pageSize = pageSize,isReward = isReward)
        products.userPoints?.let { settings.putUserPoints(it) }
        return products
    }
}
