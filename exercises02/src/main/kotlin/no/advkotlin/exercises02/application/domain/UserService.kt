package no.advkotlin.exercises02.application.domain

import org.springframework.stereotype.Service

@Service
class UserService
(
    private val userRepository: UserRepository
) {
    private val userValidator = UserValidator()

    fun createUser(userToCreate: User): User {
        userValidator.validateNewUser(userToCreate)
        return userRepository.saveUser(userToCreate)
    }
}

