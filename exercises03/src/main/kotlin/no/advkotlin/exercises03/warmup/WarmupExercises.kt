package no.advkotlin.exercises03.warmup

import kotlinx.coroutines.*
import kotlin.concurrent.thread
import kotlin.math.max

class WarmupExercises {

    fun helloWorld(): String = runBlocking {

        val buffer = StringBuffer()

        val t1 = launch {
            delay(1000)
            buffer.append("World!")
        }

        val t2 = launch {
            delay(500)
            buffer.append("Hello, ")
        }

        joinAll(t1, t2)

        buffer.toString()
    }

    suspend fun maxElement(a: List<Int>, b: List<Int>): Int = coroutineScope {
        val maxA = async {
            a.maxOrNull() ?: throw IllegalArgumentException("a was empty")
        }
        val maxB = async {
            b.maxOrNull() ?: throw IllegalArgumentException("b was empty")
        }
        max(maxA.await(), maxB.await())
    }

    suspend fun sumLists(lists: List<List<Int>>): Int = coroutineScope {
        lists.map {
            async { it.sum() }
        }.awaitAll().sum()
    }

}