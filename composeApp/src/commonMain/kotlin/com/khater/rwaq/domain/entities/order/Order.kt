package com.khater.rwaq.domain.entities.order

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

data class Order(
    val id: String,
    val productId: String,
    val name: String,
    val totalPrice: Double,
    val itemPrice: Double,
    val count: Int,
    val size: String,
    val extension: List<OrderExtension>,

    // --- NEW FIELDS ---
    val branchId: String,
    val branchName: String,
    val isPickupFromBranch: Boolean,
    val carName: String?,
    val carNumber: String?,
    val carColor: Color?,
    val imageUrl: String,
    val isReward: Boolean
)
@Serializable
data class OrderExtension(
    val id: String,
    val name: String,
    val price: Double,
    val count: Int,
    val maxCount: Int
)
