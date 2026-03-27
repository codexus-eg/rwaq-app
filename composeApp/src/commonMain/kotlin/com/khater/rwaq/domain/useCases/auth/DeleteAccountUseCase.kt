package com.khater.rwaq.domain.useCases.auth

 import com.khater.rwaq.domain.repository.authentication.AuthenticationRepository
 import com.khater.rwaq.domain.repository.user.UserRepository
 import com.russhwolf.settings.ExperimentalSettingsApi

@OptIn(ExperimentalSettingsApi::class)
class DeleteAccountUseCase(
    private val userRepository: UserRepository,
    ) {

    suspend fun deleteAccount() = userRepository.deleteAccount()

}