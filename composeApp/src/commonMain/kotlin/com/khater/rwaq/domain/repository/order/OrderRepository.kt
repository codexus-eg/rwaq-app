package com.khater.rwaq.domain.repository.order

import com.khater.rwaq.data.dto.order.CreateOrderRequest
import com.khater.rwaq.domain.entities.order.NewOrder
import com.khater.rwaq.domain.entities.order.Order
import com.khater.rwaq.domain.entities.product.Product
import com.khater.rwaq.domain.model.PagedData
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun insertOrder(order: Order)
    suspend fun updateOrder(order: Order)
    suspend fun deleteOrderById(id: String)
    suspend fun clearCart()
    suspend fun makeOrder(requestOrderRequest:CreateOrderRequest)
    fun getAllOrders(): Flow<List<Order>>
    suspend fun getAllOrdersFromRemote(page: Int, pageSize: Int): PagedData<NewOrder>
}