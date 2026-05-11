package com.khater.rwaq.data.source.remote.cart

import com.khater.rwaq.data.dto.cart.*

interface CartDataSource {
    suspend fun getCart(): CartResponse
    suspend fun addItemToCart(request: AddToCartRequestDto): CartResponse
    suspend fun updateCartItem(itemId: String, request: UpdateCartItemRequestDto): CartResponse
    suspend fun removeCartItem(itemId: String): CartResponse
    suspend fun clearCart(): CartResponse
    suspend fun updateCartMetadata(request: UpdateCartMetadataRequestDto): CartResponse
    suspend fun applyCoupon(couponCode: String): CartResponse
    suspend fun checkout(request: CheckoutRequestDto): CheckoutResponse
}
