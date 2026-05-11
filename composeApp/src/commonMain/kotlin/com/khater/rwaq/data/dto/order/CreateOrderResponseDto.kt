package com.khater.rwaq.data.dto.order

import com.khater.rwaq.domain.entities.order.OrderResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateOrderResponseDto(
    @SerialName("success")
    val success: Boolean,
    @SerialName("client_secret")
    val clientSecret: String? = null,
    @SerialName("public_key")
    val publicKey: String? = null
)

fun CreateOrderResponseDto.toDomain(): OrderResponse {
    return OrderResponse(
        success = success,
        clientSecret = clientSecret ?: "",
        publicKey = publicKey ?: ""
    )
}
