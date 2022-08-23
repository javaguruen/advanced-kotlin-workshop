package com.hamre.javazone.advkotlin.application.domain

class UserValidator {


    fun validateNewUser(userToCreate: User) {
        if (userToCreate.id != null){
            throw java.lang.IllegalArgumentException("ID should be null for a new user object")
        }
        validateEmail(userToCreate.email)
    }

    internal fun validateEmail(email: String) {
        require(email.length <= 100){"Email too long"}
        if (!emailPattern.matches(email)){
            throw java.lang.IllegalArgumentException("Invalid format on email address")
        }
    }


    companion object{
        private val emailPattern = """(?:[a-z0-9!#${'$'}%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#${'$'}%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)])""".toRegex()

    }
}
