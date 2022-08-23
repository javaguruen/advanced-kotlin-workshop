package no.advkotlin.exercises02.application.rest

import arrow.core.Invalid
import arrow.core.Valid
import arrow.core.Validated
import arrow.core.zip
import arrow.typeclasses.Semigroup
import no.advkotlin.exercises02.application.domain.Email
import no.advkotlin.exercises02.application.domain.User
import no.advkotlin.exercises02.application.domain.Username
import no.advkotlin.exercises02.application.domain.ValidationErrors
import no.advkotlin.exercises02.application.rest.User as ApiUser

object Api2DomainMapper {
    fun mapUser(apiUser: ApiUser): User {
        val validatedEmail = Email.newInstance(apiUser.email)
        val validatedUsername = Username.newInstance(apiUser.username)
        val validatedUser: Validated<ValidationErrors, User> = validatedEmail.zip(
            Semigroup.nonEmptyList(),
            validatedUsername) { email, username ->
            User(
                id = apiUser.id,
                username = username,
                email = email
            )
        }

        return when (validatedUser) {
            is Valid -> validatedUser.value
            is Invalid -> throw IllegalArgumentException(
                validatedUser.value
                    .map { it.message }
                    .joinToString(separator = ", ")
            )
        }
    }
}
