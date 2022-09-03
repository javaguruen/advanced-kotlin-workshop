package samples.coroutines.structured

import kotlinx.coroutines.*

suspend fun main() = coroutineScope {

    /**
     * Launch a coroutine that produces a value
     *
     * async returns a Defered instance, which is a subclass of Job
     * defered.await() will wait for the coroutine to produce a value.
     *
     * async like launch is an extension function on CoroutineScope.
     *
     */

    val parent = async {
        var child = async {
            delay(2000L)
        }
        child.invokeOnCompletion { println("completed because $it") }
        child.await()
    }
    parent.cancel()
    parent.join()

}