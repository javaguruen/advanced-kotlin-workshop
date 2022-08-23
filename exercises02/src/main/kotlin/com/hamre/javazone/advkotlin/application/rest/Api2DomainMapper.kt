package com.hamre.javazone.advkotlin.application.rest

import com.hamre.javazone.advkotlin.application.domain.User
import com.hamre.javazone.advkotlin.application.rest.User as ApiUser

object Api2DomainMapper {
    fun mapUser(apiUser: ApiUser): User = User(
        id = apiUser.id,
        username = apiUser.username,
        email = apiUser.email
    )
}
