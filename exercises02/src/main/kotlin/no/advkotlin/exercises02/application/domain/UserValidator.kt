package no.advkotlin.exercises02.application.domain

class UserValidator {
    fun validateNewUser(userToCreate: User) {
        if (userToCreate.id != null) {
            throw java.lang.IllegalArgumentException("ID should be null for a new user object")
        }
    }
}
