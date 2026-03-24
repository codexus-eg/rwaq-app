package com.khater.rwaq.data.dto.authentication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    @SerialName("phone")
    val phoneNumber: String,

)