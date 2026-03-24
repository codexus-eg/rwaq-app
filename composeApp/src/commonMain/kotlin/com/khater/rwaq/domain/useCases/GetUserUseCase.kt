package com.khater.rwaq.domain.useCases

 import com.khater.rwaq.domain.entities.user.User
 import com.khater.rwaq.domain.repository.user.UserRepository

class GetUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() = userRepository.getUser()
}