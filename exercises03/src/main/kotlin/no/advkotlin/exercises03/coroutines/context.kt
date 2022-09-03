package samples.coroutines.context

import kotlinx.coroutines.*
import kotlin.coroutines.EmptyCoroutineContext

@OptIn(ExperimentalStdlibApi::class)
fun main(): Unit = runBlocking {

    /**
     * # Launch a coroutine that produces a value
     *
     * [async] returns a [Deferred] instance, which is a subclass of [Job]
     *
     * `deferred.await()` will wait for the coroutine to produce a value.
     *
     * async like launch is an extension function on the [CoroutineScope] interface.
     *
     */

    val e = EmptyCoroutineContext
    val context = e + Dispatchers.IO + SupervisorJob() + CoroutineName("my coroutine")
    println("context: $context")
    val name = context[CoroutineName]
    val dispatcher = context[CoroutineDispatcher]

    launch(Dispatchers.IO + SupervisorJob()) {
        launch(CoroutineName("my name")) {
            launch(Dispatchers.Default) {
                println(coroutineContext[CoroutineName])
            }
        }
    }

    launch(Dispatchers.IO + CoroutineName("my coroutine")) {

    }
}