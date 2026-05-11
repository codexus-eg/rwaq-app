package com.khater.rwaq.data.source.remote.cart

import com.khater.rwaq.data.dto.cart.*
import com.khater.rwaq.data.util.*
import com.khater.rwaq.domain.repository.service.LocalizationService
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.http.*

class CartRemoteDataSource(
    private val httpClient: HttpClient,
    private val localizationService: LocalizationService
) : CartDataSource {

    override suspend fun getCart(): CartResponse {
        return safeWrapper {
            httpClient.getJson<CartResponse>(
                path = CART_ENDPOINT,
                headerParams = mapOf(
                    ACCEPT_LANGUAGE to localizationService.getCurrentLanguage().iso
                )
            )
        }
    }

    override suspend fun addItemToCart(request: AddToCartRequestDto): CartResponse {
        return safeWrapper {
            httpClient.postJson<AddToCartRequestDto, CartResponse>(
                path = "$CART_ENDPOINT/add-item",
                requestDto = request,
                headerParams = mapOf(
                    ACCEPT_LANGUAGE to localizationService.getCurrentLanguage().iso
                )
            )
        }
    }

    override suspend fun updateCartItem(itemId: String, request: UpdateCartItemRequestDto): CartResponse {
        return safeWrapper {
            httpClient.putJson<UpdateCartItemRequestDto, CartResponse>(
                path = "$CART_ENDPOINT/item/$itemId",
                requestDto = request,
                headerParams = mapOf(
                    ACCEPT_LANGUAGE to localizationService.getCurrentLanguage().iso
                )
            )
        }
    }

    override suspend fun removeCartItem(itemId: String): CartResponse {
        return safeWrapper {
            httpClient.deleteJson<CartResponse>(
                path = "$CART_ENDPOINT/item/$itemId",
                headerParams = mapOf(
                    ACCEPT_LANGUAGE to localizationService.getCurrentLanguage().iso
                )
            )
        }
    }

    override suspend fun clearCart(): CartResponse {
        return safeWrapper {
            httpClient.deleteJson<CartResponse>(
                path = CART_ENDPOINT,
                headerParams = mapOf(
                    ACCEPT_LANGUAGE to localizationService.getCurrentLanguage().iso
                )
            )
        }
    }

    override suspend fun updateCartMetadata(request: UpdateCartMetadataRequestDto): CartResponse {
        return safeWrapper {
            httpClient.putJson<UpdateCartMetadataRequestDto, CartResponse>(
                path = "$CART_ENDPOINT/metadata",
                requestDto = request,
                headerParams = mapOf(
                    ACCEPT_LANGUAGE to localizationService.getCurrentLanguage().iso
                )
            )
        }
    }

    override suspend fun applyCoupon(couponCode: String): CartResponse {
        return safeWrapper {
            httpClient.postJson<ApplyCouponRequestDto, CartResponse>(
                path = "$CART_ENDPOINT/apply-coupon",
                requestDto = ApplyCouponRequestDto(couponCode),
                headerParams = mapOf(
                    ACCEPT_LANGUAGE to localizationService.getCurrentLanguage().iso
                )
            )
        }
    }

    override suspend fun checkout(request: CheckoutRequestDto): CheckoutResponse {
        return safeWrapper {
            httpClient.postJson<CheckoutRequestDto, CheckoutResponse>(
                path = "$CART_ENDPOINT/checkout",
                requestDto = request,
                headerParams = mapOf(
                    ACCEPT_LANGUAGE to localizationService.getCurrentLanguage().iso
                )
            )
        }
    }

    companion object {
        const val CART_ENDPOINT = "api/cart"
        const val ACCEPT_LANGUAGE = "Accept-Language"
    }
}
