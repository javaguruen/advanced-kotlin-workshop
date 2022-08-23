package no.advkotlin.exercises02.application.domain

import arrow.core.*

@JvmInline
value class Email private constructor(val value: String) {
    companion object Factory {
        private val emailPattern = """(?:[a-z0-9!#${'$'}%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#${'$'}%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)])""".toRegex()

        fun newInstance(candidate: String): Validated<ValidationErrors, Email> {
            return try {
                validateEmail(candidate)
                Email(candidate).valid()
            } catch (e: Throwable) {
                return ValidationError(e.message ?: "Exception").invalidNel()
            }
        }

        private fun validateEmail(email: String) {
            require(email.length <= 100) { "Email too long" }
            if (!emailPattern.matches(email)) {
                throw java.lang.IllegalArgumentException("Invalid format on email address")
            }
        }


    }
}
