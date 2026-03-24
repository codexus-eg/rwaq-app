package com.khater.rwaq.data.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRequestDto(
    val username:String,
    val email: String
)
