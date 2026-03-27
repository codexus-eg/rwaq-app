package com.khater.rwaq.domain.repository.user

import com.khater.rwaq.domain.entities.user.User

interface UserRepository {
    suspend fun sendContactMessage(name: String, phone: String, email: String, message: String)
    suspend fun getUser(): User
    suspend fun deleteAccount()
    suspend fun updateUserData(username: String,email: String)

}