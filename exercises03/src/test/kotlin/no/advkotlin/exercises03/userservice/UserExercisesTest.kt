package no.advkotlin.exercises03.userservice

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import no.advkotlin.exercises03.userservice.service.UserService
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

@OptIn(ExperimentalCoroutinesApi::class)
internal class UserExercisesTest {

    @Test
    fun getUserAndItemsCoroutine() = runTest {
        val userExercises = UserExercises(UserService())
        val result = userExercises.getUserAndItemsCoroutine("101")
        assertEquals("Signy Evensen", result.first.name)
        assertEquals(1, result.second.size)
        assertEquals(2000, currentTime)
    }

    @Test
    fun getUserAndItemsConcurrent() = runTest {
        val userExercises = UserExercises(UserService())
        val result = userExercises.getUserAndItemsConcurrent("101")
        assertEquals("Signy Evensen", result.first.name)
        assertEquals(1, result.second.size)
        assertEquals(1000, currentTime)
    }

    @Test
    fun getUsersAndItemsInBulk() = runTest {
        val userExercises = UserExercises(UserService())
        val result = userExercises.getUsersAndItemsInBulk(listOf("101", "102", "103"))
        assertEquals(3, result.size)
        val user = result.first()
        assertEquals("Signy Evensen", user.first.name)
        assertEquals(1, user.second.size)
        assertEquals(1000, currentTime)
    }
}