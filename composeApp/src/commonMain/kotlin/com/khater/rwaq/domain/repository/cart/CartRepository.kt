package com.khater.rwaq.domain.repository.cart

import com.khater.rwaq.data.dto.cart.*
import com.khater.rwaq.domain.entities.cart.Cart
import com.khater.rwaq.domain.entities.cart.CheckoutResult
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun observeCartBadgeCount(): Flow<Int>
    suspend fun clearCartBadgeCount()
    suspend fun getCart(): Cart
    suspend fun addItemToCart(request: AddToCartRequestDto): Cart
    suspend fun updateCartItem(itemId: String, request: UpdateCartItemRequestDto): Cart
    suspend fun removeCartItem(itemId: String): Cart
    suspend fun clearCart(): Cart
    suspend fun updateCartMetadata(request: UpdateCartMetadataRequestDto): Cart
    suspend fun applyCoupon(couponCode: String): Cart
    suspend fun checkout(request: CheckoutRequestDto): CheckoutResult
}
