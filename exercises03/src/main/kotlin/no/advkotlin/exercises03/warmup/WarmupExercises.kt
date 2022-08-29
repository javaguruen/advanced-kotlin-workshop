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

    /*
        application:

        try to set some breakpoints in coroutines and start debug run
        make sure breakpoint is set to stop all threads
        look around in the debuger to se active corutines

        Try to add the DebugProbe agent to the application, and dump coroutines
        with DebugProbe.dumpCoroutines()
        Remember to call DebugProbe.install()
        make sure to do it in a place where the coroutines are active

     */


}