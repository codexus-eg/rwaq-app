package com.khater.rwaq.data.repository.user


import com.khater.rwaq.data.dto.user.ContactUsRequest
import com.khater.rwaq.data.dto.user.toDomain
import com.khater.rwaq.data.source.remote.user.UserRemoteDataSource
import com.khater.rwaq.domain.entities.user.User
import com.khater.rwaq.domain.repository.user.UserRepository

class UserRepositoryImpl(
    private val remoteDataSource: UserRemoteDataSource
) : UserRepository {

    override suspend fun sendContactMessage(name: String, phone: String, email: String, message: String) {
        val request = ContactUsRequest(
            name = name,
            phone = phone,
            email = email,
            message = message
        )
        remoteDataSource.sendContactUsMessage(request)
    }
    override suspend fun getUser(): User {
        val response = remoteDataSource.getUser()

        return response.data?.toDomain()
            ?: throw Exception("Server returned success but user data was missing.")

    }

    override suspend fun updateUserData(username: String, email: String) {
        remoteDataSource.updateUserData(username = username , email = email)
    }
}