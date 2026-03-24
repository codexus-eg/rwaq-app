package com.khater.rwaq.domain.useCases.auth

import com.khater.rwaq.domain.repository.user.UserRepository

class UpdateUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(username: String, email: String) =
        userRepository.updateUserData(username = username, email = email)
}