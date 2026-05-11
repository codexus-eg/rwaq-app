package com.khater.rwaq.domain.entities.order

data class OrderResponse(
    val success: Boolean,
    val clientSecret: String,
    val publicKey: String
)
