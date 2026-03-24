package com.khater.rwaq.domain.useCases.auth

 import com.khater.rwaq.domain.repository.authentication.AuthenticationRepository
 import kotlinx.coroutines.flow.first

class UpdateFcmTokenUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {

    suspend operator fun invoke(fcmToken: String) {
        authenticationRepository.saveFcmTokenLocally(fcmToken)
        val isLoggedIn = authenticationRepository.isUserLoggedIn().first()
        if (isLoggedIn) {
            try {
                authenticationRepository.updateFcmToken(fcmToken)
            } catch (e: Exception) {
                println("Failed to sync FCM token to server: ${e.message}")
            }
        }
    }
}