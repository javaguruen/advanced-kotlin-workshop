package samples.coroutines.cancellation

import kotlinx.coroutines.*
import kotlinx.coroutines.NonCancellable.invokeOnCompletion
import kotlin.coroutines.EmptyCoroutineContext

@OptIn(ExperimentalStdlibApi::class)
fun main(): Unit = runBlocking {

    /** cancellation
     * # Launch a coroutine that produces a value
     *
     * [async] returns a [Deferred] instance, which is a subclass of [Job]
     *
     * `deferred.await()` will wait for the coroutine to produce a value.
     *
     * async like launch is an extension function on the [CoroutineScope] interface.
     *
     */

    val job = launch(Dispatchers.Default) {
        println("parent coroutine")

        launch {
            delay(2000L)
            println("first child")
        }.also { it.invokeOnCompletion { println("C1 Done $it") } }

        launch {
            delay(500L)
            println("second child")
            throw IllegalStateException()
        }.also { it.invokeOnCompletion { println("C2 Done $it") } }
    }
    job.invokeOnCompletion { println("P Done $it") }


}