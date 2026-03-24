package com.khater.rwaq.domain.useCases.auth

import com.khater.rwaq.domain.repository.authentication.AuthenticationRepository
import com.khater.rwaq.domain.util.InvalidPasswordException


class RegisterUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend fun register(phoneNumber: String,username:String, password: String) {
        if (!isPasswordValid(password)) throw InvalidPasswordException()
        authenticationRepository.register(
            phoneNumber = phoneNumber,
            username = username,
            password = password
        )

    }

    private fun isPasswordValid(password: String) = password.length >= PASSWORD_MIN_LENGTH

    private companion object {
        const val PASSWORD_MIN_LENGTH = 8
    }
}