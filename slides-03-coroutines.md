---
marp: true
theme: default
paginate: true
style: |
  .columns {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 1rem;
  }
---

# Coroutines

<br/>

### JavaZone 2022
### Bjørn Hamre & Morten Nygaard Åsnes


<br/>

[@mortenaa](twitter.com/mortenaa)
[@javaguruen](twitter.com/javaguruen)

---

# Coroutines - Motivation

- Avoid blocking main thread
- Concurrency with Threads is difficult. Deadlocks & memory leaks
- Threads are resource hungry
- Do long computation in the background
- Do tasks in parallel

<!--
  Langvarige operasjoner som nettverkskall og disk io blokkerer tråden
  mens de venter på svar. For en interaktiv applikasjon (mobil, js, gui) vil
  ui bli uresponsivt om blokkererman tråden som oppdaterer ui.

  Kan løses med tråder, men tråder er vanskelig å gjøre riktig. Kan føre til
  minnelekasje. Tråder er "tunge" å switche mellom. Vanskelig å debugge.
  i motsetning til kode med callbacks blir kode nesten lik vanlig kode
-->

---

# What is a coroutine?

<div class="columns">
<div>

```kotlin
    val job = launch {
        println("Coroutine started")
        delay(1000L)
        println("Coroutine done")
    }
    println("Coroutine launched")
```

</div>
<div>

- A computation that can be suspended and resumed
- Lightweight & fast switching
- Easier to manage than threads
- Can be run on one or many threads

</div>
</div>
<!--
  Korutiner stammer tilbake til 60 tallet, men ble først popularisert med
  goroutines i Golang. 
  Korutiner baserer seg på at en funksjon kan suspendes for å så fortsette senere. 
  Korutin api er på et høyere abstraksjonsnivå enn tråder. Kotlin håndterer
  bytting av hvilke korutiner som kjører og blir suspended, og kan gjenbruke minnet 
  til en suspended korutine. Korutine er mye mindre ressurskrevende enn tråder.
-->

---

# launch
<div class="columns">
<div>

```kotlin
    val job = launch {
        println("Coroutine started")
        delay(1000L)
        println("Coroutine done")
    }
    println("Coroutine launched")

    job.cancel()
    job.join()
```

</div>

<div>

- Build and launch a lambda as a coroutine
- extension function on CoroutineScope
- returns Job
  can cancel or wait for completion
- does not return a value from the lambda
- waits for coroutines inside to finish (suspends but does not block)
  - for GlobalScope it does not wait for contained coroutines to finish

</div>
</div>

<!--
  launch er en coroutine builder som lager en korutine, som startes umiddelbart (men det 
  kan konfigureres).
  Den er implementert som en extension funksjon på interface CoroutineScope. launch returnerer
  en instans av Job. den har metoder for bla.a å kansellere en korutine, eller vente 
  på at den skal fullføre.

  Det finnes en subklasse av CorutineScope, GlobalScope som kjører korutinen utenfor
  structured concurency. Dvs. alt av opprydding av ressurser ved feilsitasjoer. Den må
  derfor brukes forsiktig.
-->

---

# Async / Await

<div class="columns">
<div>

```kotlin
    val result: Deferred<Int> = async {
        delay(1000L)
        42
    }
    val intValue = result.await()
    println("Result=$intValue")
```

```kotlin
    val deferreds: List<Deferred<Int>> = (1..100).map {
        async {
            delay(1000)
            it
        }
    }
    val list: List<Int> = deferreds.awaitAll()
    println("Sum = ${list.sum()}")
```

</div>
<div>

- Build and start coroutine (like launch)
- extension on CoroutineScope interface
- Returns Deferred<T> which is subclass of Job
- Can cancel or join like with Job
- Can also `await`, which produces a value when completed
- `await` will suspend until value is ready
- If you need to await on multiple `Deferred` there is `awaitAll(...)`

</div>
</div>

---

# runBlocking

<div class="columns">
<div>

```kotlin
    val s = runBlocking {
        delay(1000L)
        "world!"
    }
    println("Hello, $s")
```
</div>
<div>

- Starts a coroutine with a new context
- can be called from "normal" code
- not usually used outside of tests or main function
- waits for all contained coroutines to finish
- blocks the thread!

</div>
</div>
<!--
  `runBlocking` er en korutine builder som blokerer til korutinen er ferdig. Den lager en ny korutine, 
  og bruker den aktuelle tråden til å kjøre korutinen. 
  Den er en bro mellom "vanlig" kode og korutiner (suspending functions)
  Siden runBlocking skal kunne kalles fra normal kode kan den ikke suspende selv,
  men må blokkere tråden den kjører i til corutinene er ferdig
-->

---

# Suspend Functions

<div class="columns">
<div>

```kotlin
public fun <T> CoroutineScope.async(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
): Deferred<T> 
```

```kotlin
suspend fun doSomething(): String = coroutineScope {
  delay(100L)

  val result = async {
    delay(100L)
    42
  }
  "Hello ${result.await()}"
}
```
</div>
<div>

- all functions that can suspend are marked with the `suspend` keyword
- suspending code can not be called from a non-suspending code
- but non-suspending code can be called from suspending code
- a suspend function doesn't provide a CoroutineScope
  - (we can use `coroutineScope` for that)

</div>

<!--
Funksjoner som `runBlocking` og 
`coroutineScope {}` kan benyttes i en suspend funksjon for å få tilgang til 
coroutine scopet som funksjonen blir kallet fra kan man bruke couroutineScope

-->

---

# Structured concurrency

<div class="columns">
<div>

```kotlin
    val parent = async {
        var child = async {
            delay(2000L)
        }
    }
```

</div>
<div>

- child coroutine inherits properties from parent
- cancellation of parent propagates to children
- errors in children propagate to parents
- ensures proper cleanup of resources
</div>
</div>

<!--
  Med structured concurrency forsøker man å strukturere bruken av korutiner
  på en måte oversiktlig og trygg måte. Nøsting av uttrykk for korutiner skaper ett 
  hierarki som samsvarer med organiseringen av koden, og som sørger for at feil
  propagerer oppover om de ikke håndteres.

StructuredConcurency betyr at korutiner arver context egenskaper (som Job og dispatcher) fra korutinen den startes i (parent). Om forelder korutinen blir canceled, vil
alle child korutiner også bli canceled. Dette gir en naturlig måte å organisere korutiner på. 
-->

---

# CoroutineScope

- Holds a CoroutineContext
- Coroutine builders as extension functions 
  - launch
  - async
- Coroutine builders inherit context from CoroutineScope
- `coroutineScope { }` creates a new CoroutineScope
  - used in suspending function gives access to calling scope
  
<!--
En CoroutineScope har en CoroutineContext som har contexten som bestemmer hvordan korutinen kjører. 
CoroutineContext inneholder bla.a 
En dispatcher som avgjør hvordan tråder allokeres og er `Job` objekt som kan brukes til å sjekke om korutinen kjører, og til å cancellere den.
Innebygde dispatchere `Dispatcher.Default|Main|IO|Unconfined` (TODO: beskrivelse av hver dispatcher). Man kan også sett opp sin egen dispatcher.

Context og dermed tråd kan endres underveis i en korutine med `withContext`
-->

---

# Coroutine Builders

## launch, async

- extensions on `CoroutineScope`
- context from CoroutineScope receiver
- runs asynchronously 
- exceptions propagate to parent

---

# Scoping functions

## coroutineScope, supervisorScope, withContext

- suspending functions
- context from suspending context
- runs sequentially 
- suspends until done

---

# Dispatchers

- Default: General work. Expensive computations. Threadpool based on cpu cores
- Main: Run on main/UI thread. (Android, JavaFX, Swing)
- IO: for IO operations. Large threadpool
- Unconfined: Don't care. Same thread

- newSingleThreadContext
- newFixedThreadPoolContext

<!--
  newSingleThreadContext(
  newFixedThreadPoolContext()
-->

---

# GlobalScope

- For top level coroutines
- Not managed by any parent
- lifetime as whole application

<!--
  GlobalScope er et top level coroutine scope som har levetid som applikasjonen, 
  og er dermed mulig å lekke minne om man ikke stopper (`cancel`) korutiner som er startet med dette scope.
-->

---

# Cancellation

- Cooperative
- Cancelled parent will cancel all children

---

# Error handling

---

# Testing Coroutines

- we can use runBlocking to call suspend functions from tests
- runTest is similar but uses a special dispatcher for tests
  - calls to delay will return immediately
  - keeps track of virtual time

---

# Debugging Coroutines

- Breakpoints need to suspend all threads
- Intellij will show current coroutines and contexts
- A special JVM agent can be installed to enable dumping of coroutines from code