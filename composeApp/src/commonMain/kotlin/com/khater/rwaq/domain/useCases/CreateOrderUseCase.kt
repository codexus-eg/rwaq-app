package com.khater.rwaq.domain.useCases

import com.khater.rwaq.data.dto.order.CreateOrderRequest
import com.khater.rwaq.domain.entities.order.OrderResponse
import com.khater.rwaq.domain.repository.order.OrderRepository

class CreateOrderUseCase(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(createOrderRequest: CreateOrderRequest): OrderResponse =
        orderRepository.makeOrder(createOrderRequest)

    suspend fun getAllOrders(page: Int,pageSize: Int) = orderRepository.getAllOrdersFromRemote(page = page,pageSize = pageSize)
}