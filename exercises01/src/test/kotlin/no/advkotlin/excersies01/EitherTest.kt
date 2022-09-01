package no.advkotlin.excersies01
/*
import no.advkotlin.exercises01.Either
import no.advkotlin.exercises01.filter
import no.advkotlin.exercises01.left
import no.advkotlin.exercises01.right
*/
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@Suppress("USELESS_IS_CHECK")
internal class EitherTest {
  /*
    @Test
    internal fun `is left`() {
        val either = Either.Left("Text")
        Assertions.assertNull(either.getOrNull())
    }
*/
/*
    @Test
    internal fun `is Right`() {
        val either = Either.Right(100)
        assertEquals(100, either.getOrNull())
    }
*/

    //TODO: Test på flatmap
    
    /*
    @Test
    internal fun `mapLeft on a left`() {
        val either: Either<String, String> = Either.Left("Error")
        val mappedEither = either.mapLeft { java.lang.IllegalArgumentException(it) }
        assert(mappedEither is Either.Left)
        val throwable = (mappedEither as Either.Left).value
        assertTrue(throwable is java.lang.IllegalArgumentException)
        assertEquals("Error", throwable.message!!)
    }

    @Test
    internal fun `mapLeft on a Right`() {
        val either: Either<String, Int> = Either.Right(22)
        val mappedEither = either.mapLeft { java.lang.IllegalArgumentException(it) }
        assert(mappedEither is Either.Right)
        val value = (mappedEither as Either.Right).value
        assertEquals(22, value)
    }
    */

    /*
    @Test
    internal fun `filter right positive predicate`() {
        val either: Either<String, Int> = Either.Right(42)
        val filteredEither = either.filter({i -> i%2==0}, { Either.Left("Not an even number") })
        assertTrue(filteredEither is Either.Right)
        assertEquals(42, (filteredEither as Either.Right).value)
    }

    @Test
    internal fun `filter right negative predicate`() {
        val either: Either<String, Int> = Either.Right(41)
        val filteredEither = either.filter({i -> i%2==0}, { "Not an even number"})
        assertTrue(filteredEither is Either.Left)
        assertEquals("Not an even number", (filteredEither as Either.Left).value)
    }

    @Test
    internal fun `filter left gives left`() {
        val either: Either<String, Int> = Either.Left("A left")
        val filteredEither = either.filter({i -> i%2==0}, { Either.Left("Not an even number") })
        assertTrue(filteredEither is Either.Left)
        assertEquals("A left", (filteredEither as Either.Left).value)
    }

    */

    /*
    @Test
    internal fun `map a right`() {
        val either = Either.Right("42")
        val mappedEither = either.map { it.toInt() }
        assert(mappedEither is Either.Right)
        assertEquals(42, mappedEither.getOrNull())
    }

    @Test
    internal fun `map a left`() {
        val either: Either<String, String> = Either.Left("Error")
        val mappedEither = either.map { it.toInt() }
        assert(mappedEither is Either.Left)
        Assertions.assertNull(mappedEither.getOrNull())
    }
    */

    /*
    @Test
    internal fun `lift a value to a Left`() {
        val either: Either<String, Int> = "Error".left()
        assertTrue(either is Either.Left)
        assertEquals("Error", (either as Either.Left).value)
    }
    */
    /*
    @Test
    internal fun `lift a value to a Right`() {
        val either: Either<String, Int> = 42.right()
        assertTrue(either is Either.Right)
        assertEquals(42, (either as Either.Right).value)
    }

   */
}
