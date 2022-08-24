package no.advkotlin.exercises02.application.rest

import no.advkotlin.exercises02.application.domain.User
import no.advkotlin.exercises02.application.rest.User as ApiUser

object Api2DomainMapper {
    fun mapUser(apiUser: ApiUser): User = User(
        id = apiUser.id,
        username = apiUser.username,
        email = apiUser.email
    )
}
