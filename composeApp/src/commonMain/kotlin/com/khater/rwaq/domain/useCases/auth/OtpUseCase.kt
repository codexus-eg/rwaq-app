package com.khater.rwaq.domain.useCases.auth

import com.khater.rwaq.domain.repository.authentication.AuthenticationRepository


class OtpUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend fun requestOtp(phoneNumber: String,otpType: RequestOtpType) = authenticationRepository.requestOTP(phoneNumber=phoneNumber,otpType = otpType)
    suspend fun verifyOtp(otpCode: String,phoneNumber: String) = authenticationRepository.verifyOTPCode(otpCode = otpCode,phoneNumber = phoneNumber)
    suspend fun verifyOtpForForgetPassword(otpCode: String,phoneNumber: String) = authenticationRepository.verifyOTPCodeForForgetPassword(otpCode = otpCode,phoneNumber = phoneNumber)
}

enum class RequestOtpType {
    ForgetPassword,Register
}