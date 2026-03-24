package com.khater.rwaq.data.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class ContactUsRequest(
    val name: String,
    val phone: String,
    val email: String,
    val message: String
)