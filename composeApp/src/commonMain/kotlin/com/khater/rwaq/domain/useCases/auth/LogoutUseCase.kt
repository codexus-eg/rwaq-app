package com.khater.rwaq.domain.useCases.auth

 import com.khater.rwaq.domain.repository.authentication.AuthenticationRepository
 import com.russhwolf.settings.ExperimentalSettingsApi

@OptIn(ExperimentalSettingsApi::class)
class LogoutUseCase(
    private val authenticationRepository: AuthenticationRepository,
    ) {

    suspend fun logout() = authenticationRepository.logout()

}