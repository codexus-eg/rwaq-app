package com.khater.rwaq.domain.useCases.auth

import com.khater.rwaq.domain.repository.authentication.AuthenticationRepository


class ResetPasswordUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend fun resetPassword(phoneNumber: String, newPassword: String,otp:String) =
        authenticationRepository.resetPassword(
            phoneNumber = phoneNumber,
            newPassword = newPassword,
            otp = otp
        )
}