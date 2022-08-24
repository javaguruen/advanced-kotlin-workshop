package no.advkotlin.exercises02.application.domain

import java.util.*

data class User(
    val id: UUID? = null,
    val username: String,
    val email: String
) {

}
