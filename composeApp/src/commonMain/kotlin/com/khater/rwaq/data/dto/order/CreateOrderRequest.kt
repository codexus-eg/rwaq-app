package com.khater.rwaq.data.dto.order

import com.khater.rwaq.presentation.screens.cartScreen.uiStates.OrderLocation
import kotlinx.serialization.Serializable

@Serializable
data class CreateOrderRequest(
    val items: List<OrderItemRequest>,
    val notes: String,
    val couponCode: String?,
    val paymentMethod: String, // "CASH" or "ONLINE"
    val totalAmount: Double,
    val orderLocation: OrderLocation
)

@Serializable
data class OrderItemRequest(
    val productId: String,
    val productName: String, // Ideally, add 'productId' to your Order entity for this
    val quantity: Int,
    val size: String,
    val imageUrl: String,
    val unitPrice: Double,
    val totalPrice: Double,
    val extensions: List<OrderExtensionRequest>,
    // --- NEW FIELDS ---
    val branchId: String,
    val branchName: String,
    val isPickupFromBranch: Boolean, // true = pickup, false = drive thru
    val carName: String?,
    val carNumber: String?,
    val carColor: Int?,
    val carColorName: String?,
    val isRewarded: Boolean = false
)

@Serializable
data class OrderExtensionRequest(
    val extensionId: String,
    val name: String,
    val price: Double,
    val quantity: Int
)