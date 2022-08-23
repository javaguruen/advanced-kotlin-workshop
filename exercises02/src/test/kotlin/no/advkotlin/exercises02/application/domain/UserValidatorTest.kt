package no.advkotlin.exercises02.application.domain

import no.advkotlin.exercises02.application.rest.Api2DomainMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException
import no.advkotlin.exercises02.application.rest.User as ApiUser

internal class UserValidatorTest {
    private val userValidator = UserValidator()

    @Test
    internal fun `too long email address should throw exception`() {
        val longEmail = "01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
        val exception = assertThrows<IllegalArgumentException> {
            Api2DomainMapper.mapUser(ApiUser(email = longEmail, username = "uname"))
        }
        assertTrue(exception.message?.contains("Email too long") ?: false)
    }

/*    @Test
    internal fun `invalid email format should throw exception`() {
        val invalidEmail = "bjorn@gmail"
        assertThrows<IllegalArgumentException> {
            userValidator.validateEmail(invalidEmail)
        }
    }

    @Test
    internal fun `valid email format should pass validation`() {
        val invalidEmail = "bjorn@gmail.com"
        userValidator.validateEmail(invalidEmail)
    }
*/

}
