package com.khater.rwaq.domain.entities.order


 data class NewOrder(
     val id: String, // Mapping _id to id
     val items: List<OrderItem>,
     val notes: String? = null,
     val couponCode: String? = null,
     val paymentMethod: String,
     val totalAmount: Double, // JSON has Int, but Money usually Double
     val status: String,
     val createdAt: String,
     val updatedAt: String
)

data class OrderItem(
    val productName: String,
    val quantity: Int,
    val size: String,
    val unitPrice: Double,
    val totalPrice: Double,
    val imageUrl: String,
    val extensions: List<OrderItemExtension>,
    
    // Fulfillment Details
    val branchName: String,
    val isPickupFromBranch: Boolean,
    
    // Car Details (Nullable: Clean domain objects usually use null instead of empty strings)
    val carName: String?,
    val carNumber: String?,
    val carColor: Int? 
)

data class OrderItemExtension(
    val name: String,
    val price: Double,
    val quantity: Int
)