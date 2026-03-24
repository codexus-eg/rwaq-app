package com.khater.rwaq.data.dto.authentication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class VerifyOtpRequestDto(
    @SerialName("otp")
    val otp: String,
    @SerialName("phone")
    val phoneNumber: String,
    @SerialName("fcm")
    val fcm: String?
)