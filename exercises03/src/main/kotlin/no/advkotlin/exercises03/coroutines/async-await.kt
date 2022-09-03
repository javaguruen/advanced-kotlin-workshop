package samples.coroutines.async

import kotlinx.coroutines.*

suspend fun main() = coroutineScope {

    val result: Deferred<Int> = async {
        println("Coroutine started")
        delay(1000L)
        println("Coroutine done")
        42
    }
    println("Coroutine launched")
    println("Done. Result=${ result.await() }")
}
