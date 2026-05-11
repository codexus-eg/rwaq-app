package com.khater.rwaq.domain.entities.cart

data class Cart(
    val id: String,
    val items: List<CartItem>,
    val subtotal: Double,
    val totalQuantity: Int,
    val notes: String,
    val pickupType: String,
    val branchId: String,
    val branchName: String,
    val carName: String,
    val carNumber: String,
    val carColor: String,
    val couponCode: String?,
    val userPoints: Int? = null
)

data class CartItem(
    val id: String,
    val productId: String,
    val productName: String,
    val productNameAr: String,
    val quantity: Int,
    val size: String,
    val sizeAr: String,
    val unitPrice: Double,
    val totalPrice: Double,
    val imageUrl: String,
    val extensions: List<CartExtension>,
    val isRewardItem: Boolean,
    val pointsCost: Int,
    val cashbackAmount: Double
)

data class CartExtension(
    val extensionId: String,
    val name: String,
    val nameAr: String,
    val price: Double,
    val quantity: Int
)

data class CheckoutResult(
    val orderId: String,
    val totalAmount: Double,
    val paymentStatus: String,
    val paymentInfo: PaymentInfo?
)

data class PaymentInfo(
    val clientSecret: String,
    val publicKey: String,
    val paymobOrderId: String,
    val paymentUrl: String
)
