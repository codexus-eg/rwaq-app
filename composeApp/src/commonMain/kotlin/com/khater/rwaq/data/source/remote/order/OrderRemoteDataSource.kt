package com.khater.rwaq.data.source.remote.order

import com.khater.rwaq.data.dto.base.PagedDataDto
import com.khater.rwaq.data.dto.order.CreateOrderRequest
import com.khater.rwaq.data.dto.order.CreateOrderResponseDto
import com.khater.rwaq.data.dto.order.OrderDto
import com.khater.rwaq.data.dto.order.toPagedListOfOrders
import com.khater.rwaq.data.dto.product.ProductDto
import com.khater.rwaq.data.dto.product.toPagedListOfProducts
import com.khater.rwaq.data.source.remote.product.ProductRemoteDataSource.Companion.ACCEPT_LANGUAGE
import com.khater.rwaq.data.source.remote.product.ProductRemoteDataSource.Companion.PAGE_NUMBER_PARAMETER
import com.khater.rwaq.data.source.remote.product.ProductRemoteDataSource.Companion.PAGE_SIZE_PARAMETER
import com.khater.rwaq.data.source.remote.product.ProductRemoteDataSource.Companion.PRODUCTS_ENDPOINT
import com.khater.rwaq.data.util.getJson
import com.khater.rwaq.data.util.postJson
import com.khater.rwaq.data.util.safeWrapper
import com.khater.rwaq.domain.entities.order.NewOrder
import com.khater.rwaq.domain.model.PagedData
import com.khater.rwaq.domain.repository.service.LocalizationService
import io.ktor.client.HttpClient

class OrderRemoteDataSource(
    private val httpClient: HttpClient,
    private val localizationService: LocalizationService
) : OrderDataSource {

    override suspend fun makeOrder(requestOrderRequest: CreateOrderRequest): CreateOrderResponseDto {
        return safeWrapper {
            httpClient.postJson<CreateOrderRequest, CreateOrderResponseDto>(
                path = MAKE_ORDER_ENDPOINT,
                requestDto = requestOrderRequest,
            )
        }
    }

    override suspend fun getAllOrders(
        page: Int,
        pageSize: Int,
    ): PagedData<NewOrder> {
        val response = safeWrapper {
            httpClient.getJson<PagedDataDto<OrderDto>>(
                path = GET_ORDERS_ENDPOINT,
                headerParams = mapOf(
                    ACCEPT_LANGUAGE to localizationService.getCurrentLanguage().iso,
                ),
                queryParams = mapOf(
                    PAGE_NUMBER_PARAMETER to page.toString(),
                    PAGE_SIZE_PARAMETER to pageSize.toString(),
                )
            )
        }
        val page = response.toPagedListOfOrders()
        return page.copy(data = page.data)
    }

    companion object {
        const val MAKE_ORDER_ENDPOINT = "api/orders"
        const val GET_ORDERS_ENDPOINT = "api/orders"
        const val ACCEPT_LANGUAGE = "Accept-Language"
        const val PAGE_NUMBER_PARAMETER = "page"
        const val PAGE_SIZE_PARAMETER = "limit"
    }
}