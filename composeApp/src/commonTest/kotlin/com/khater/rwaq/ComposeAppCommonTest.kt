package com.khater.rwaq

import com.khater.rwaq.data.dto.base.PagedDataDto
import com.khater.rwaq.data.dto.cart.CartResponse
import com.khater.rwaq.data.dto.cart.UpdateCartItemRequestDto
import com.khater.rwaq.data.dto.cart.toDomain
import com.khater.rwaq.data.dto.product.ProductDto
import com.khater.rwaq.data.dto.product.toPagedListOfProducts
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class ComposeAppCommonTest {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Test
    fun example() {
        assertEquals(3, 1 + 2)
    }

    @Test
    fun rewardsProductsResponseMapsUserPoints() {
        val response = json.decodeFromString<PagedDataDto<ProductDto>>(
            """
            {
              "success": true,
              "count": 10,
              "total": 20,
              "page": 1,
              "pages": 2,
              "userPoints": 20,
              "data": []
            }
            """.trimIndent()
        )

        val pagedProducts = response.toPagedListOfProducts()

        assertEquals(20, pagedProducts.userPoints)
    }

    @Test
    fun cartResponseMapsUserPoints() {
        val response = json.decodeFromString<CartResponse>(
            """
            {
              "success": true,
              "userPoints": 8,
              "message": "Item added to cart",
              "data": {
                "_id": "cart-id",
                "items": []
              }
            }
            """.trimIndent()
        )

        assertEquals(8, response.toDomain().userPoints)
    }

    @Test
    fun updateCartItemRequestOnlySerializesQuantity() {
        val requestJson = json.encodeToString(UpdateCartItemRequestDto(quantity = 2))

        assertEquals("""{"quantity":2}""", requestJson)
    }
}
