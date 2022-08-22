package no.advkotlin.excersies01

import no.advkotlin.exercises01.Either
import no.advkotlin.exercises01.filter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class EitherTest {
    @Test
    internal fun `is left`() {
        val either = Either.Left("Text")
        Assertions.assertNull(either.getOrNull())
    }

    @Test
    internal fun `is Right`() {
        val either = Either.Right(100)
        Assertions.assertEquals(100, either.getOrNull())
    }

    @Test
    internal fun `map a right`() {
        val either = Either.Right("42")
        val mappedEither = either.map { it.toInt() }
        assert(mappedEither is Either.Right)
        Assertions.assertEquals(42, mappedEither.getOrNull())
    }

    @Test
    internal fun `map a left`() {
        val either: Either<String, String> = Either.Left("Error")
        val mappedEither = either.map { it.toInt() }
        assert(mappedEither is Either.Left)
        Assertions.assertNull(mappedEither.getOrNull())
    }

    @Test
    internal fun `filter right positive predicate`() {
        val either: Either<String, Int> = Either.Right(42)
        val filteredEither = either.filter({i -> i%2==0}, { Either.Left("Not an even number") })
        Assertions.assertTrue(filteredEither is Either.Right)
        Assertions.assertEquals(42, (filteredEither as Either.Right).value)
    }

    @Test
    internal fun `filter right negative predicate`() {
        val either: Either<String, Int> = Either.Right(41)
        val filteredEither = either.filter({i -> i%2==0}, { "Not an even number"})
        Assertions.assertTrue(filteredEither is Either.Left)
        Assertions.assertEquals("Not an even number", (filteredEither as Either.Left).value)
    }

    @Test
    internal fun `filter left gives left`() {
        val either: Either<String, Int> = Either.Left("A left")
        val filteredEither = either.filter({i -> i%2==0}, { Either.Left("Not an even number") })
        Assertions.assertTrue(filteredEither is Either.Left)
        Assertions.assertEquals("A left", (filteredEither as Either.Left).value)
    }
}
