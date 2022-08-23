package no.advkotlin.exercises02.application.domain

import arrow.core.NonEmptyList

data class ValidationError(val message: String)

typealias ValidationErrors = NonEmptyList<ValidationError>

