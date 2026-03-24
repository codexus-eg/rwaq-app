package com.khater.rwaq.data.dto.user

import com.khater.rwaq.domain.entities.user.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponseDto(
    @SerialName("success") val success: Boolean? = null,
    @SerialName("data") val data: UserDto? = null,
)

@Serializable
data class UserDto(
    @SerialName("_id") val id: String? = null,
    @SerialName("phone") val phone: String? = null,
    @SerialName("points") val points: Int? = null,
    @SerialName("isVipUser") val isVipUser: Boolean? = null,
    @SerialName("referCode") val referCode: String? = null,
    @SerialName("role") val role: String? = null,
    @SerialName("username") val username: String? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("updatedAt") val updatedAt: String? = null,
)


fun UserDto.toDomain(): User {
    return User(
        id = id ?: "",
        phone = phone ?: "",
        points = points ?: 0,
        isVipUser = isVipUser == true,
        referCode = referCode ?: "",
        role = role ?: "",
        username = username ?: "" // Handle null username from JSON if necessary
    )
}