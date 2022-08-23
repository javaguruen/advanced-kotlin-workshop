package no.advkotlin.excersies01

import no.advkotlin.exercises01.isEven
import no.advkotlin.exercises01.isOdd
import no.advkotlin.exercises01.polite
import no.advkotlin.exercises01.toOptional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

internal class ExtensionsTest {


    @Test
    internal fun `is even`() {
        assertTrue(42.isEven())
        assertFalse(17.isEven())
    }

    @Test
    internal fun `is odd`() {
        assertFalse(42.isOdd())
        assertTrue(17.isOdd())
    }

    @Test
    internal fun polite() {
        assertEquals("Sir Bjørn", "Bjørn".polite())
    }

    @Test
    internal fun `toOptional og null`() {
        val number: Int? = null
        val maybeNumber: Optional<Int> = number.toOptional()
        val maybeNumber42: Optional<Int> = 42.toOptional()

        Assertions.assertTrue(maybeNumber.isEmpty)
        Assertions.assertTrue(maybeNumber42.isPresent)
        Assertions.assertEquals(42, maybeNumber42.orElse(0))
    }
}
