package com.khater.rwaq.data.repository.cart

import com.khater.rwaq.data.dto.cart.*
import com.khater.rwaq.data.source.remote.cart.CartDataSource
import com.khater.rwaq.data.util.putUserPoints
import com.khater.rwaq.domain.entities.cart.Cart
import com.khater.rwaq.domain.entities.cart.CheckoutResult
import com.khater.rwaq.domain.repository.cart.CartRepository
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings

@OptIn(ExperimentalSettingsApi::class)
class CartRepositoryImpl(
    private val cartDataSource: CartDataSource,
    private val settings: FlowSettings,
) : CartRepository {

    override suspend fun getCart(): Cart = cartDataSource.getCart().toDomainAndCachePoints()

    override suspend fun addItemToCart(request: AddToCartRequestDto): Cart =
        cartDataSource.addItemToCart(request).toDomainAndCachePoints()

    override suspend fun updateCartItem(itemId: String, request: UpdateCartItemRequestDto): Cart =
        cartDataSource.updateCartItem(itemId, request).toDomainAndCachePoints()

    override suspend fun removeCartItem(itemId: String): Cart =
        cartDataSource.removeCartItem(itemId).toDomainAndCachePoints()

    override suspend fun clearCart(): Cart =
        cartDataSource.clearCart().toDomainAndCachePoints()

    override suspend fun updateCartMetadata(request: UpdateCartMetadataRequestDto): Cart =
        cartDataSource.updateCartMetadata(request).toDomainAndCachePoints()

    override suspend fun applyCoupon(couponCode: String): Cart =
        cartDataSource.applyCoupon(couponCode).toDomainAndCachePoints()

    override suspend fun checkout(request: CheckoutRequestDto): CheckoutResult =
        cartDataSource.checkout(request).toDomain()

    private suspend fun CartResponse.toDomainAndCachePoints(): Cart {
        userPoints?.let { settings.putUserPoints(it) }
        return toDomain()
    }
}
