package com.khater.rwaq.data.source.remote.order

import com.khater.rwaq.data.dto.base.PagedDataDto
import com.khater.rwaq.data.dto.order.CreateOrderRequest
import com.khater.rwaq.data.dto.order.OrderDto
import com.khater.rwaq.data.dto.order.CreateOrderResponseDto
import com.khater.rwaq.domain.entities.order.NewOrder
import com.khater.rwaq.domain.entities.order.OrderResponse
import com.khater.rwaq.domain.entities.product.Product
import com.khater.rwaq.domain.model.PagedData

interface OrderDataSource{
    suspend fun makeOrder(requestOrderRequest: CreateOrderRequest): CreateOrderResponseDto
    suspend fun getAllOrders(page: Int, pageSize: Int): PagedData<NewOrder>

}