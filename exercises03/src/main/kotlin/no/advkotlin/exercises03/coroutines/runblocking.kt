package samples.coroutines.launch

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {

    /**
     * Launch a coroutine.
     *
     * Notice the order of the printed output.
     *
     * `launch` is an extension function on the [CoroutineScope] interface.
     * Here the scope it set up by runBlocking, and is available as `this`, thus we can call `launch` without explicitly
     * providing a coroutine scope.
     *
     */

    val s = runBlocking {
        delay(1000L)
        "world!"
    }
    println("Hello, $s")
}