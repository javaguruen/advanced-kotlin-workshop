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

# What is a coroutine?

<div class="columns">
<div>

```kotlin
launch {
    println("Coroutine started")
    delay(1000L)
    println("Coroutine done")
}
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
  Korutine api er på et høyere abstraksjonsnivå enn tråder. Kotlin håndterer
  bytting av hvilke korutiner som kjører og blir suspended, og kan gjenbruke minnet 
  til en suspended korutine. Korutine er mye mindre ressurskrevende enn tråder.
-->

---

# Coroutines - Motivation

- Avoid blocking main thread
- Do long computation in the background
- Threads are resource hungry
- Concurrency with Threads is difficult. Deadlocks & memory leaks
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

# launch
<div class="columns">
<div>

```kotlin
val job: Job = launch {
    println("Coroutine started")
    delay(1000L)
    println("Coroutine done")
}
println("Coroutine launched")

// Coroutine launched
// Coroutine started
// Coroutine done
```
</div>

<div>

- Builds coroutine and starts it immediately
- extension function on CoroutineScope interface
- returns a `Job`
  can cancel or wait for completion
- does not return a value from the lambda (other than the job)
- waits for coroutines inside to finish (suspends but does not block)

</div>
</div>

<!--
  launch er en coroutine builder som lager en korutine, som startes umiddelbart (men det 
  kan konfigureres). 
  Den er implementert som en extension funksjon på interface CoroutineScope. launch returnerer
  en instans av Job. den har metoder for bla.a å kansellere en korutine, eller vente 
  på at den skal fullføre.
-->

---

# async / await

<div class="columns">
<div>

```kotlin
val result: Deferred<Int> = async {
    delay(1000L)
    42
}
val intValue = result.await()
println("The answer is $intValue")

// The answer is 42
```

</div>
<div>

- Build and start coroutine (like launch)
- extension on CoroutineScope interface
- Returns Deferred<T> which is subclass of Job
- Can cancel or join like with Job
- Can also `await`, which produces a value when completed
- `await` will suspend until value is ready

</div>
</div>

---

# async / await (continued)

<div class="columns">
<div>

```kotlin
val deferreds: List<Deferred<Int>> = (1..100).map {
    async {
        delay(1000)
        it
    }
}
val list: List<Int> = deferreds.awaitAll()
println("Sum = ${list.sum()}")

// Sum = 5050
```

</div>
<div>


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

// Hello, world!
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
  men må blokkere tråden den kjører i til corutinene er ferdig. Returnerer siste verdi fra lambdaen direkte
-->

---

# Suspend Functions

<div class="columns">
<div>

```kotlin
suspend fun doSomething(): String = coroutineScope {
  delay(100L)

  val result = async {
    delay(100L)
    "done"
  }
  "I am ${result.await()}"
}

// I am done
```

</div>
<div>

- all functions that can suspend are marked with the `suspend` keyword
- suspending code can not be called from a non-suspending code
- but non-suspending code can be called from suspending code
- a suspend function doesn't provide a CoroutineScope
  - (we can use `coroutineScope {}` for that)

<!--
suspend er et nøkkelord i kotlin. Det markerer at funksjonen kan suspende og resume.
når den kompileres har den derfor et ekstra argument, som vi ikke ser i signaturen her

Funksjoner som `runBlocking` og 
`coroutineScope {}` kan benyttes i en suspend funksjon for å få tilgang til 
coroutine scopet som funksjonen blir kallet fra kan man bruke couroutineScope
-->

</div>
</div>

---

# Suspend functions (continued)

<div class="columns">
<div>

```kotlin
public fun <T> CoroutineScope.async(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
): Deferred<T> 
```

</div>
<div>

- `suspend` keyword in function signature
- lambda body is also `suspend`
- `this` is bound to a CoroutineScope in the lambda body

</div>
</div>

<!--
block er en lambda med en CoroutineScope receiver, som gjør at this er bundet til 
et CoroutineScope når lambdaen vi angir kjører. på samme måte som for en funksjon definert
med suspend fun makrkerer vi her at block er suspending, og derfor kan kalle andre suspend
funksjoner.
-->

---

# Suspend points

![](images/screenshot_suspend.png)

<!--
suspend og resume kan kun skje på suspend points. Intellij viser hvor i margen.
kall til coroutine biblioteket er typisk suspend points. også tredjeparts bibliotek som
støtter korutiner. Om man f.eks gjør en http request med en http klient som er beregnet
for bruke med korutiner, vil den suspende når http requesten gjøres, og resumes når responsen
kommer tilbake.
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

StructuredConcurency betyr at korutiner arver context egenskaper (som Dispatcher) fra korutinen den startes i (parent). 
Om forelder korutinen blir cancelled, vil
alle child korutiner også bli canceled. Dette gir en naturlig måte å organisere korutiner på. 
-->

---

# CoroutineScope

- Holds a `CoroutineContext`
- Coroutine builders as extension functions 
  - `launch`
  - `async`
- Coroutine builders inherit context from `CoroutineScope`
- `coroutineScope { }` creates a new `CoroutineScope`
  - used in suspending function gives access to calling scope
  
<!--
En CoroutineScope har en CoroutineContext som har contexten som bestemmer hvordan korutinen kjører. 
CoroutineContext inneholder bla.a 
En dispatcher som avgjør hvordan tråder allokeres og er `Job` objekt som kan brukes til å sjekke om korutinen kjører, og til å cancellere den.
Innebygde dispatchere `Dispatcher.Default|Main|IO|Unconfined` (TODO: beskrivelse av hver dispatcher). Man kan også sett opp sin egen dispatcher.

Context og dermed tråd kan endres underveis i en korutine med `withContext`
-->

---

# coroutineScope  

```kotlin
suspend fun downloadResource(url: URL): Resource = coroutineScope {
    // Using a suspending client
    httpClient.get(url)
}

suspend fun downloadResource(url: URL): Resource = withContext(Dispatchers.IO) {
    // Using a blocking client
    httpClient.get(url)
}
```

---

# Combining contexts

```kotlin
fun scopeTest() {
    runTest {
        val c1 = currentCoroutineContext() + CoroutineName("c1") + Dispatchers.IO
        val c2 = c1 + Job() + CoroutineName("c2")
        
        assertEquals(c1[CoroutineDispatcher], c2[CoroutineDispatcher])
    }
}
```

---

# Coroutine Builders

- **launch**  
extension on `CoroutineScope`
asynchronous
returns Job but does not return value from body
- **async**  
extension on `CoroutineScope`
asynchronous
returns `Deferred` with return value from body
- **runBlocking**  
normal function
synchronous, blocks thread
returns value from body directly

<!-- 
alle kan kalles utenfor et CoroutineScope, men kun runBlocking kan kalles uten en CoroutineContext
body har suspend modifier, så den kan kalle andre suspoend funksjoner.
kan kalle async og launch uten å være i en suspend context siden de er vanlige extension funksjoner,
men kan ikke kalle join eller await siden de er suspend funksjoner
-->

---

# Coroutine scoping functions

- **coroutineScope**  
suspending function  
creates a new CoroutineScope with a new CoroutineContext based on current context
runs sequentially, and waits for all containing coroutines to finish
- **withContext**  
same as `coroutineScope`, but can configure the new context
- **supervisorScope**  
same as `coroutineScope`, but does not propagate exceptions to parent

---

# Dispatchers

- **Default**  
General work. Expensive computations. Threadpool based on cpu cores
- **Main**  
Run on main/UI thread. (Android, JavaFX, Swing)
- **IO**  
for IO operations. Large threadpool
- **Unconfined**  
Don't care. Same thread

In addition custom Dispatchers can be created, for instance with `newSingleThreadContext` or `newFixedThreadPoolContext`

<!--

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

```kotlin
val job = launch {
  
    launch {
        delay(2000L)
    }.also { it.invokeOnCompletion { println("C1 Done $it") } }

    launch {
        delay(500L)
    }.also { it.invokeOnCompletion { println("C2 Done $it") } }
}
job.invokeOnCompletion { println("P Done $it") }

delay(700L)
job.cancel()
job.join()

// C2 Done null
// C1 Done kotlinx.coroutines.JobCancellationException
// P Done kotlinx.coroutines.JobCancellationException
```

<!--
kansellering er cooperativ. korutiner kan kun stoppe på suspension points.
vi kan kalle join på job etter cancell for å vente til den er faktisk stoppet.
finnes en funksjon som kombinerer begge cancellAndJoin()
kansellert parent vil kansellere barn

-->

---

# Error handling

- Exceptions propogate to parent coroutines
- supervisorScope, SupervisorJob
- try catch works as expected inside a coroutine
- but wrapping a coroutine in a try block does not work

<!--
exception i en coroutine cancellerer den, exceptions propageres til parent, som cancellerer barn,
og seg selv, helt opp til ytterste korutine, som avslutter applikasjonen.
Try catch "rundt" en coroutine fungerer ikke, fordi exception blir propagert via job hierarkiet,
og ikke på vanlig måte
SupervisorJob ignorerer exceptions i barn
CoroutineExceptionHandler can be installed to the top level coroutine to handle uncaught exceptions
anbefalt å bruke try catch på innsiden av en korutine
-->

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