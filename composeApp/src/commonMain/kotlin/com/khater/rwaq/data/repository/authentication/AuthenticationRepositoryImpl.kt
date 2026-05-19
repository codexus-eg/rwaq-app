package com.khater.rwaq.data.repository.authentication

import co.touchlab.kermit.Logger
import com.khater.rwaq.data.dto.authentication.AuthenticationResponse
import com.khater.rwaq.data.dto.authentication.LoginRequestDto
import com.khater.rwaq.data.dto.authentication.OtpRequestDto
import com.khater.rwaq.data.dto.authentication.RefreshTokenRequestDto
import com.khater.rwaq.data.dto.authentication.RegisterRequestDto
import com.khater.rwaq.data.dto.authentication.ResetPasswordRequestDto
import com.khater.rwaq.data.dto.authentication.UpdateFcmTokenRequestDto
import com.khater.rwaq.data.dto.authentication.User
import com.khater.rwaq.data.dto.authentication.VerifyOtpRequestDto
import com.khater.rwaq.data.util.accessToken
import com.khater.rwaq.data.util.invalidateAuthTokens
import com.khater.rwaq.data.util.postEmpty
import com.khater.rwaq.data.util.postJson
import com.khater.rwaq.data.util.putAccessToken
import com.khater.rwaq.data.util.putCartBadgeCount
import com.khater.rwaq.data.util.putEmail
import com.khater.rwaq.data.util.putFcmToken
import com.khater.rwaq.data.util.putPhoneNumber
import com.khater.rwaq.data.util.putRefreshToken
import com.khater.rwaq.data.util.putUsername
import com.khater.rwaq.data.util.refreshToken
import com.khater.rwaq.data.util.safeWrapper
import com.khater.rwaq.domain.repository.authentication.AuthenticationRepository
import com.khater.rwaq.domain.useCases.auth.RequestOtpType
import com.khater.rwaq.domain.util.UnAuthorizedException
import com.khater.rwaq.presentation.util.emptyFcm
import com.mmk.kmpnotifier.notification.NotifierManager
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val message: String,
)

@OptIn(ExperimentalSettingsApi::class)
class AuthenticationRepositoryImpl(
    private val httpClient: HttpClient,
    private val settings: FlowSettings,
) : AuthenticationRepository {
    private var sessionId = ""

    override suspend fun login(phoneNumber: String) = safeWrapper {
        val loginResponse: LoginResponse =
            httpClient.postJson(
                requestDto = LoginRequestDto(phoneNumber = phoneNumber),
                path = REQUEST_OTP_ENDPOINT
            )
        //  saveAuthTokens(authenticationInfo = loginResponse)
    }

    override suspend fun register(
        phoneNumber: String,
        username: String,
        password: String,
    ) = safeWrapper {
        val fcmToken = NotifierManager.getPushNotifier().getToken()
        httpClient.postJson<RegisterRequestDto, Unit>(
            requestDto = RegisterRequestDto(
                phoneNumber = phoneNumber,
                username = username,
                password = password,
                fcmToken = fcmToken ?: String.emptyFcm
            ),
            path = REGISTER_ENDPOINT
        )
    }

    override suspend fun requestOTP(phoneNumber: String, otpType: RequestOtpType) = safeWrapper {
        val endpoint = when (otpType) {
            RequestOtpType.ForgetPassword -> REQUEST_OTP_ENDPOINT
            RequestOtpType.Register -> REQUEST_OTP_ENDPOINT
        }
        httpClient.postJson<OtpRequestDto, Unit>(
            requestDto = OtpRequestDto(phoneNumber = phoneNumber),
            path = endpoint
        )
    }

    override suspend fun resetPassword(phoneNumber: String, newPassword: String, otp: String) =
        safeWrapper {
            val loginResponse: AuthenticationResponse =
                httpClient.postJson(
                    requestDto = ResetPasswordRequestDto(
                        phoneNumber = phoneNumber,
                        password = newPassword,
                        otp = otp
                    ),
                    path = RESET_PASSWORD_ENDPOINT
                )
            saveAuthTokens(authenticationInfo = loginResponse)

        }

    override suspend fun verifyOTPCode(otpCode: String, phoneNumber: String) = safeWrapper {
        val fcmToken = NotifierManager.getPushNotifier().getToken()

        val otpResponse: AuthenticationResponse = httpClient.postJson(
            requestDto = VerifyOtpRequestDto(
                otp = otpCode,
                phoneNumber = phoneNumber,
                fcm = fcmToken
            ),
            path = VERIFY_OTP_ENDPOINT
        )
        saveAuthTokens(authenticationInfo = otpResponse)
        saveAuthCredential(
            username = otpResponse.user.username,
            phoneNumber = otpResponse.user.phone,
            email = otpResponse.user.email
        )
    }


    override suspend fun verifyOTPCodeForForgetPassword(otpCode: String, phoneNumber: String) {
        val otpResponse: AuthenticationResponse = safeWrapper {
            val fcmToken = NotifierManager.getPushNotifier().getToken()
            httpClient.postJson(
                requestDto = VerifyOtpRequestDto(
                    otp = otpCode,
                    phoneNumber = phoneNumber,
                    fcm = fcmToken
                ),
                path = VERIFY_OTP_FOR_FORGET_PASSWORD_ENDPOINT
            )
        }
        saveAuthTokens(authenticationInfo = otpResponse)
        saveAuthCredential(
            username = otpResponse.user.username,
            phoneNumber = otpResponse.user.phone,
            email = otpResponse.user.email
        )
        try {
            httpClient.invalidateAuthTokens()
        } catch (_: Exception) {
        }
    }

    override suspend fun logout() = safeWrapper {
        httpClient.postEmpty(LOGOUT_ENDPOINT)
        try {
            httpClient.invalidateAuthTokens()
        } catch (_: Exception) {
        }
        clearAuthTokens()
        clearAuthCredential()
    }


    override suspend fun refreshAccessToken(): String {

        val refreshResponse: AuthenticationResponse =
            try {
                safeWrapper {
                    httpClient.postJson(
                        requestDto = RefreshTokenRequestDto(settings.refreshToken.first()),
                        path = REFRESH_ACCESS_TOKEN_ENDPOINT
                    )
                }
            } catch (e: UnAuthorizedException) {
                Logger.i { "abcajbvle $e" }
                AuthenticationResponse("", "", User("", "", "", 0, "", ""))
            }

        Logger.i { "abcajbvle $refreshResponse" }
        Logger.i { "abcajbvle ${refreshResponse.accessToken.isNotBlank()}" }
        saveAuthTokens(authenticationInfo = refreshResponse)

        try {
            httpClient.invalidateAuthTokens()
        } catch (_: Exception) {
        }
        return settings.accessToken.first()
    }

    override suspend fun updateFcmToken(fcmToken: String) {
        safeWrapper<String> {
            httpClient.postJson(
                requestDto = UpdateFcmTokenRequestDto(
                    fcmToken = fcmToken
                ),
                path = UPDATE_FCM_TOKEN
            )
        }
    }


    override suspend fun saveFcmTokenLocally(fcmToken: String) {
        settings.putFcmToken(fcmToken)
    }

    override fun isUserLoggedIn(): Flow<Boolean> =
        settings.accessToken.combine(settings.refreshToken) { accessToken, refreshToken ->
            accessToken.isNotBlank() && refreshToken.isNotBlank()
        }

    private suspend fun saveAuthTokens(authenticationInfo: AuthenticationResponse) {
        settings.putAccessToken(authenticationInfo.accessToken)
        settings.putRefreshToken(authenticationInfo.refreshToken)
    }

    private suspend fun clearAuthTokens() {
        settings.putAccessToken("")
        settings.putRefreshToken("")
        settings.putCartBadgeCount(0)
    }

    private suspend fun saveAuthCredential(username: String, phoneNumber: String, email: String) {
        settings.putUsername(username)
        settings.putPhoneNumber(phoneNumber)
        settings.putEmail(email)
    }

    private suspend fun clearAuthCredential() {
        settings.putUsername("")
        settings.putPhoneNumber("")
        settings.putEmail("")
    }

    companion object {
        const val REQUEST_FORGET_PASSWORD_OTP_ENDPOINT = "api/Auth/request-password-reset"
        const val LOGIN_ENDPOINT = "Auth/login"
        const val REGISTER_ENDPOINT = "Auth/register"
        const val REQUEST_OTP_ENDPOINT = "api/auth/request-otp"
        const val RESET_PASSWORD_ENDPOINT = "Auth/reset_password"
        const val VERIFY_OTP_ENDPOINT = "api/auth/verify-otp"
        const val VERIFY_OTP_FOR_FORGET_PASSWORD_ENDPOINT = "api/auth/verify-otp"
        const val LOGOUT_ENDPOINT = "api/auth/logout"
        const val REFRESH_ACCESS_TOKEN_ENDPOINT = "api/auth/refresh-token"
        const val UPDATE_FCM_TOKEN = "api/auth/fcm-token"
    }
}
