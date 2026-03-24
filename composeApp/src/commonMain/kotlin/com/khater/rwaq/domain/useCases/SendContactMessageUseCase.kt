package com.khater.rwaq.domain.useCases

import com.khater.rwaq.domain.repository.user.UserRepository


class SendContactMessageUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(name: String, phone: String, email: String, message: String) {
        return userRepository.sendContactMessage(name, phone, email, message)
    }
}