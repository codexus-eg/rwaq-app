package com.khater.rwaq.data.repository.cart

import com.khater.rwaq.data.dto.cart.*
import com.khater.rwaq.data.source.remote.cart.CartDataSource
import com.khater.rwaq.data.util.cartBadgeCount
import com.khater.rwaq.data.util.putCartBadgeCount
import com.khater.rwaq.data.util.putUserPoints
import com.khater.rwaq.domain.entities.cart.Cart
import com.khater.rwaq.domain.entities.cart.CheckoutResult
import com.khater.rwaq.domain.repository.cart.CartRepository
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@OptIn(ExperimentalSettingsApi::class)
class CartRepositoryImpl(
    private val cartDataSource: CartDataSource,
    private val settings: FlowSettings,
) : CartRepository {

    private val badgeMutex = Mutex()

    override fun observeCartBadgeCount(): Flow<Int> = settings.cartBadgeCount

    override suspend fun clearCartBadgeCount() {
        setCartBadgeCount(0)
    }

    override suspend fun getCart(): Cart =
        cartDataSource.getCart().toDomainAndCachePoints(syncBadgeCount = true)

    override suspend fun addItemToCart(request: AddToCartRequestDto): Cart =
        cartDataSource.addItemToCart(request).toDomainAndCachePoints().also {
            updateCartBadgeCount { count -> count + 1 }
        }

    override suspend fun updateCartItem(itemId: String, request: UpdateCartItemRequestDto): Cart =
        cartDataSource.updateCartItem(itemId, request).toDomainAndCachePoints()

    override suspend fun removeCartItem(itemId: String): Cart =
        cartDataSource.removeCartItem(itemId).toDomainAndCachePoints().also {
            updateCartBadgeCount { count -> count - 1 }
        }

    override suspend fun clearCart(): Cart =
        cartDataSource.clearCart().toDomainAndCachePoints().also {
            clearCartBadgeCount()
        }

    override suspend fun updateCartMetadata(request: UpdateCartMetadataRequestDto): Cart =
        cartDataSource.updateCartMetadata(request).toDomainAndCachePoints()

    override suspend fun applyCoupon(couponCode: String): Cart =
        cartDataSource.applyCoupon(couponCode).toDomainAndCachePoints()

    override suspend fun checkout(request: CheckoutRequestDto): CheckoutResult =
        cartDataSource.checkout(request).toDomain().also { result ->
            if (result.paymentInfo == null) clearCartBadgeCount()
        }

    private suspend fun CartResponse.toDomainAndCachePoints(syncBadgeCount: Boolean = false): Cart {
        userPoints?.let { settings.putUserPoints(it) }
        return toDomain().also { cart ->
            if (syncBadgeCount) setCartBadgeCount(cart.items.size)
        }
    }

    private suspend fun setCartBadgeCount(count: Int) {
        badgeMutex.withLock {
            settings.putCartBadgeCount(count)
        }
    }

    private suspend fun updateCartBadgeCount(transform: (Int) -> Int) {
        badgeMutex.withLock {
            val currentCount = settings.cartBadgeCount.first()
            settings.putCartBadgeCount(transform(currentCount))
        }
    }
}
