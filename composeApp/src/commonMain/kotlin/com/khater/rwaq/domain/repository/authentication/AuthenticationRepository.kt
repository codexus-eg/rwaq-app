package com.khater.rwaq.domain.repository.authentication

import com.khater.rwaq.domain.useCases.auth.RequestOtpType
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    suspend fun login(phoneNumber: String )
    suspend fun register(phoneNumber: String,username:String, password: String)
    suspend fun requestOTP(phoneNumber: String,otpType: RequestOtpType)
    suspend fun resetPassword(phoneNumber: String,newPassword: String,otp: String)
    suspend fun verifyOTPCode(otpCode: String,phoneNumber: String)
    suspend fun verifyOTPCodeForForgetPassword(otpCode: String,phoneNumber: String)
    suspend fun logout()
    suspend fun refreshAccessToken(): String
    suspend fun updateFcmToken(fcmToken: String)
    suspend fun saveFcmTokenLocally(fcmToken: String)
    fun isUserLoggedIn(): Flow<Boolean>
}