package com.khater.rwaq.data.repository.order

import com.khater.rwaq.data.dto.order.CreateOrderRequest
import com.khater.rwaq.data.source.local.database.order.OrderDao
import com.khater.rwaq.data.source.local.database.order.toDomain
import com.khater.rwaq.data.dto.order.toDomain
import com.khater.rwaq.data.source.local.database.order.toLocalDto
import com.khater.rwaq.data.source.remote.order.OrderDataSource
import com.khater.rwaq.domain.entities.order.NewOrder
import com.khater.rwaq.domain.entities.order.Order
import com.khater.rwaq.domain.entities.order.OrderResponse
import com.khater.rwaq.domain.model.PagedData
import com.khater.rwaq.domain.repository.order.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OrderRepositoryImpl(
    private val orderDao: OrderDao,
    private val orderDataSource: OrderDataSource
) : OrderRepository {

    override suspend fun insertOrder(order: Order) {
        orderDao.insertOrder(order.toLocalDto())
    }

    override suspend fun updateOrder(order: Order) {
        orderDao.updateOrder(order.toLocalDto())
    }

    override suspend fun deleteOrderById(id: String) {
        orderDao.deleteOrderById(id)
    }

    override suspend fun clearCart() {
        orderDao.clearCart()
    }

    override suspend fun makeOrder(requestOrderRequest: CreateOrderRequest): OrderResponse {
        return orderDataSource.makeOrder(requestOrderRequest).toDomain()
    }

    override fun getAllOrders(): Flow<List<Order>> {
        return orderDao.getAllOrders().map { list ->
            list.toDomain()
        }
    }

    override suspend fun getAllOrdersFromRemote(
        page: Int,
        pageSize: Int,
    ): PagedData<NewOrder> {
        return orderDataSource.getAllOrders(page = page,pageSize = pageSize)
    }
}