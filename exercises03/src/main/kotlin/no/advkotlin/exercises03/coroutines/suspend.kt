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
    println(doSomething())

}

suspend fun doSomething(): String = coroutineScope {
    delay(100L)

    val result = async {
        delay(100L)
        42
    }
    "Hello ${result.await()}"
}