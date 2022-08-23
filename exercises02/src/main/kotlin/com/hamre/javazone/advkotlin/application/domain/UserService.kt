package com.hamre.javazone.advkotlin.application.domain

import org.springframework.stereotype.Service
import java.util.UUID

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

data class User(
    val id: UUID? = null,
    val username: String,
    val email: String
) {

}
