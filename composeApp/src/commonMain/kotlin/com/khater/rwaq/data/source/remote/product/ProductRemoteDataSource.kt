package com.khater.rwaq.data.source.remote.product

import co.touchlab.kermit.Logger
import com.khater.rwaq.data.dto.base.PagedDataDto
import com.khater.rwaq.data.dto.product.ProductDto
import com.khater.rwaq.data.dto.product.toPagedListOfProducts
import com.khater.rwaq.data.util.getJson
import com.khater.rwaq.data.util.safeWrapper
import com.khater.rwaq.domain.entities.product.Product
import com.khater.rwaq.domain.model.PagedData
import com.khater.rwaq.domain.repository.service.LocalizationService
import io.ktor.client.HttpClient

class ProductRemoteDataSource(
    private val httpClient: HttpClient,
    private val localizationService: LocalizationService
) : ProductDataSource {
    override suspend fun getAllProduct(page: Int, pageSize: Int,isReward: Boolean): PagedData<Product> {


        val response = safeWrapper {
            httpClient.getJson<PagedDataDto<ProductDto>>(
                path = if (isReward) REWARDS_PRODUCTS_ENDPOINT else  PRODUCTS_ENDPOINT,
                headerParams = mapOf(
                    ACCEPT_LANGUAGE to localizationService.getCurrentLanguage().iso,
                    ),
                queryParams = mapOf(
                    PAGE_NUMBER_PARAMETER to page.toString(),
                    PAGE_SIZE_PARAMETER to "500",
                )
            )
        }
        Logger.i { "aivbaevb $response" }
        val page = response.toPagedListOfProducts()
        return page.copy(data = page.data)
    }

    companion object {
        const val ACCEPT_LANGUAGE = "Accept-Language"
        const val PAGE_NUMBER_PARAMETER = "page"
        const val PAGE_SIZE_PARAMETER = "limit"
        const val PRODUCTS_ENDPOINT = "api/products"
        const val REWARDS_PRODUCTS_ENDPOINT = "api/products/rewards"
    }
}