package no.advkotlin.exercises03.warmup

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class WarmupExercisesTest {

    @Test
    fun helloWorldTest() {
        assertEquals("Hello, World!", WarmupExercises().helloWorld())
    }

    @Test
    fun maxElement() {
        val a = listOf(10, 11, 2, 5, 0, 17)
        val b = listOf(11, 2, 3, 9, 10, 5)
        assertEquals(17, WarmupExercises().maxElement(a, b))
    }

    @Test
    fun sumLists() {
        val lists = listOf(
            (1..10).toList(),
            (11..20).toList(),
            (21..30).toList()
        )
        assertEquals((1..30).sum(), WarmupExercises().sumLists(lists))
    }
}