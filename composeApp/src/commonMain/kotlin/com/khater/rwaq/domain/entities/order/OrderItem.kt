package com.khater.rwaq.domain.entities.order


data class NewOrder(
     val id: String, // Mapping _id to id
     val items: List<OrderItem>,
     val notes: String? = null,
     val couponCode: String? = null,
     val paymentMethod: String,
     val paymentStatus: String,
     val paymobTransactionId: String? = null,
     val paymobOrderId: String? = null,
     val totalAmount: Double, // JSON has Int, but Money usually Double
     val deliveryFee: Double = 0.0,
	     val status: String,
	     val pickupType: String? = null,
	     val pickupTypeLabel: String? = null,
	     val orderLocation: OrderGeoLocation? = null,
     val customerName: String? = null,
     val customerEmail: String? = null,
     val customerPhone: String? = null,
     val orderAddress: String? = null,
     val assignedDeliveryDriver: String? = null,
     val deliveryStatus: String,
     val deliveryAssignedAt: String? = null,
     val deliveredAt: String? = null,
     val totalCashback: Double,
     val cashbackStatus: String? = null,
     val pointsDeducted: Boolean,
     val carName: String? = null,
     val carNumber: String? = null,
     val carColor: String? = null,
     val carColorName: String? = null,
     val createdAt: String,
     val updatedAt: String
)

data class OrderGeoLocation(
    val latitude: Double,
    val longitude: Double
)

data class OrderItem(
    val productName: String,
    val productId: String?,
    val quantity: Int,
    val size: String,
    val unitPrice: Double,
    val totalPrice: Double,
    val cashbackAmount: Double,
    val pointsCost: Int,
    val isRewardItem: Boolean,
    val imageUrl: String,
    val extensions: List<OrderItemExtension>,
    
    // Fulfillment Details
    val branchName: String,
    val isPickupFromBranch: Boolean,
    
    // Car Details (Nullable: Clean domain objects usually use null instead of empty strings)
    val carName: String?,
    val carNumber: String?,
    val carColor: String?,
    val carColorName: String?
)

data class OrderItemExtension(
    val name: String,
    val price: Double,
    val quantity: Int
)
