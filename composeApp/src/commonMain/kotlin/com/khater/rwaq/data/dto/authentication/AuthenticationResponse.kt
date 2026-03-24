package com.khater.rwaq.data.dto.authentication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationResponse(
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String,
    @SerialName("user")
    val user: User
)

@Serializable
data class User(
    val id: String,
    val phone: String,
    val referCode: String,
    val points: Int,
    val username:String,
    val email: String
)