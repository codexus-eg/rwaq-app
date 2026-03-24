package com.khater.rwaq.domain.useCases.auth

import co.touchlab.kermit.Logger
import com.khater.rwaq.data.util.fcmToken
import com.khater.rwaq.domain.repository.authentication.AuthenticationRepository
import com.khater.rwaq.domain.util.InvalidPasswordException
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalSettingsApi::class)
class LoginUseCase (
    private val authenticationRepository: AuthenticationRepository,
    private val settings: FlowSettings,
    private val externalScope: CoroutineScope
    ) {

    suspend fun login(phoneNumber: String ) {
        //if (!isPasswordValid(password)) throw InvalidPasswordException()
        authenticationRepository.login(
            phoneNumber = phoneNumber,
           // password = password
        )
//        externalScope.launch {
//            try {
//                val savedToken = settings.fcmToken.first()
//                if (savedToken.isNotBlank()) {
//                    authenticationRepository.updateFcmToken(savedToken)
//                }
//            } catch (e: Exception) {
//                Logger.e { "Background FCM sync failed: ${e.message}" }
//            }
//        }
//         try {
//            val savedToken = settings.fcmToken.first()
//            if (savedToken.isNotBlank()) {
//                authenticationRepository.updateFcmToken(savedToken)
//            }
//        } catch (e: Exception) {
//
//            co.touchlab.kermit.Logger.e("LoginUseCase") {
//                "User logged in, but FCM token sync failed: ${e.message}"
//            }
//        }
    }

    private fun isPasswordValid(password: String) = password.length >= PASSWORD_MIN_LENGTH

    private companion object {
        const val PASSWORD_MIN_LENGTH = 8
    }
}