package no.advkotlin.exercises03.warmup

import kotlin.concurrent.thread
import kotlin.math.max

class WarmupExercises {

    fun helloWorld(): String {

        val buffer = StringBuffer()

        val t1 = thread {
            Thread.sleep(1000)
            buffer.append("World!")
        }

        val t2 = thread {
            Thread.sleep(500)
            buffer.append("Hello, ")
        }

        t1.join()
        t2.join()

        return buffer.toString()
    }

    fun maxElement(a: List<Int>, b: List<Int>): Int {
        val maxA = a.maxOrNull() ?: throw IllegalArgumentException("a was empty")
        val maxB = b.maxOrNull() ?: throw IllegalArgumentException("b was empty")
        return max(maxA, maxB)
    }

    fun sumLists(lists: List<List<Int>>): Int {
        return lists.map { it.sum() }.sum()
    }

}