package com.khater.rwaq.domain.useCases

import com.khater.rwaq.domain.entities.order.Order
import com.khater.rwaq.domain.entities.order.OrderExtension
import com.khater.rwaq.domain.repository.order.OrderRepository
import kotlinx.coroutines.flow.Flow

class ManageCartUseCase(
    private val orderRepository: OrderRepository
) {
    operator fun invoke(): Flow<List<Order>> = orderRepository.getAllOrders()

    suspend fun insertOrder(order: Order) = orderRepository.insertOrder(order)

    suspend fun deleteOrder(id: String) = orderRepository.deleteOrderById(id)

    suspend fun clearCart() = orderRepository.clearCart()

    // -----------------------------------------------------------
    //  UPDATED LOGIC START
    // -----------------------------------------------------------

    suspend fun increaseOrderCount(order: Order) {
        val newCount = order.count + 1
        // Logic: (ItemPrice * NewCount) + ExtensionsTotal
        val newTotal = calculateTotal(order.itemPrice, newCount, order.extension)
        orderRepository.updateOrder(order.copy(count = newCount, totalPrice = newTotal))
    }

    suspend fun decreaseOrderCount(order: Order) {
        // Fix 1: Stop at 1. Do not delete.
        if (order.count > 1) {
            val newCount = order.count - 1
            // Logic: (ItemPrice * NewCount) + ExtensionsTotal
            val newTotal = calculateTotal(order.itemPrice, newCount, order.extension)
            orderRepository.updateOrder(order.copy(count = newCount, totalPrice = newTotal))
        }
        // If count is 1, do nothing. User must click the Trash/Delete icon to remove.
    }

    suspend fun increaseExtensionCount(order: Order, extensionId: String) {
        val updatedExtensions = order.extension.map {
            if (it.id == extensionId) it.copy(count = it.count + 1) else it
        }
        // Recalculate using current order count and new extension list
        val newTotal = calculateTotal(order.itemPrice, order.count, updatedExtensions)
        orderRepository.updateOrder(order.copy(extension = updatedExtensions, totalPrice = newTotal))
    }

    suspend fun decreaseExtensionCount(order: Order, extensionId: String) {
        val updatedExtensions = order.extension.mapNotNull {
            if (it.id == extensionId) {
                // If extension count is 1, removing it deletes the extension from the list
                if (it.count > 1) it.copy(count = it.count - 1) else null
            } else {
                it
            }
        }
        val newTotal = calculateTotal(order.itemPrice, order.count, updatedExtensions)
        orderRepository.updateOrder(order.copy(extension = updatedExtensions, totalPrice = newTotal))
    }

    /**
     * Fix 2: Price Calculation Formula
     * Previous (Wrong): (ItemPrice + ExtPrice) * ItemCount
     * New (Correct):    (ItemPrice * ItemCount) + ExtPrice
     */
    private fun calculateTotal(itemPrice: Double, itemCount: Int, extensions: List<OrderExtension>): Double {
        // 1. Calculate total price of all extensions independent of item count
        // Example: Water(10) * 3 = 30, Cookies(5) * 3 = 15. Total Ext = 45.
        val extensionsTotal = extensions.sumOf { it.price * it.count }

        // 2. Calculate item total
        // Example: Item(20) * 2 = 40.
        val itemsTotal = itemPrice * itemCount

        // 3. Sum them up
        // Total = 40 + 45 = 85.
        return itemsTotal + extensionsTotal
    }
}