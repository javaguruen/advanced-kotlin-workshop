package no.advkotlin.exercises01

/*
This is the file where you will implement the extension functions from part 1 of the exercises
*/

import java.util.Optional

fun String.polite(): String = "Sir $this"

fun Int.isEven(): Boolean = this.mod(2) == 0
fun Int.isOdd():  Boolean = this.mod(2) == 1

fun <E : Any> E?.toOptional(): Optional<E> = Optional.ofNullable(this)
