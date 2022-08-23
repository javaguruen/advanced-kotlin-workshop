package com.hamre.javazone.advkotlin.application.domain

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException

internal class UserValidatorTest {
    private val userValidator = UserValidator()

    @Test
    internal fun `too long email address should throw exception`() {
        val longEmail = "01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
        val exception = assertThrows<IllegalArgumentException> {
            userValidator.validateEmail(longEmail)
        }
        assertTrue(exception.message?.contains("Email too long") ?: false)
    }

    @Test
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


}
