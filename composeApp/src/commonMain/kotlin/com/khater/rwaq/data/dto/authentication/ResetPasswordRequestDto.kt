package com.khater.rwaq.data.dto.authentication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequestDto(
    @SerialName("phone")
    val phoneNumber: String,
    @SerialName("newPassword")
    val password: String,
    @SerialName("code")
    val otp: String
)