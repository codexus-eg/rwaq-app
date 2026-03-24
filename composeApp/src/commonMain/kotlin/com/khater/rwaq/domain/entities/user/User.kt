package com.khater.rwaq.domain.entities.user

data class User(
    val id: String,
    val phone: String,
    val points: Int,
    val isVipUser: Boolean,
    val referCode: String,
    val role: String,
    val username: String
)