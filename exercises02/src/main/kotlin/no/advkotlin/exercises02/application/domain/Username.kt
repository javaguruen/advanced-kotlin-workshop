package no.advkotlin.exercises02.application.domain

import arrow.core.Validated
import arrow.core.invalidNel
import arrow.core.valid

@JvmInline
value class Username private constructor(val value: String) {
    companion object Factory {

        fun newInstance(candidate: String): Validated<ValidationErrors, Username> {
            return try {
                require(candidate.length <= 100){"Username too long"}
                Username(candidate.lowercase()).valid()
            } catch (e: Throwable) {
                ValidationError(e.message?:"").invalidNel()
            }
        }
    }
}
