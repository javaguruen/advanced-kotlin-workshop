package no.advkotlin.exercises02.application.rest

import com.fasterxml.jackson.module.kotlin.readValue
import no.advkotlin.exercises02.application.ObjectMapperFactory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class UserTest{

    @Test
    internal fun `Test deserialization`() {
        val objectMapper = ObjectMapperFactory.create()
        val userJson = """{
            "email": "employee01@gmail.com",
            "username": "employee01"
            }"""
        val user = objectMapper.readValue<User>(userJson)
        assertNotNull(user)
        assertEquals("employee01@gmail.com", user.email)
        assertEquals("employee01", user.username)
    }
}
