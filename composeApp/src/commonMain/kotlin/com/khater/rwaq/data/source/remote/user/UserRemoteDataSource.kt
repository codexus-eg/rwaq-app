package com.khater.rwaq.data.source.remote.user

import com.khater.rwaq.data.dto.user.ContactUsRequest
import com.khater.rwaq.data.dto.user.UserResponseDto


interface UserRemoteDataSource {
    suspend fun sendContactUsMessage(request: ContactUsRequest)
    suspend fun getUser(): UserResponseDto
    suspend fun updateUserData(username: String,email: String)

}