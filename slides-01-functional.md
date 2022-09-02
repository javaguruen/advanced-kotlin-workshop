---
theme: default
_class: lead
paginate: true
backgroundColor: #fff
marp: true
---

# Level up your Kotlin skills

Get the repo: 
`git clone https://github.com/javaguruen/advanced-kotlin-workshop.git`

Morten Nygaard Åsnes
Bjørn Hamre

---
### Part 1
Extensions
Null and Exception handling

### Part 2
Arrow
Custom domain types
Validated

### Part 3
Coroutines

---
# Extensions

- Functions
- Properties
- Companion object

<!--
Kotlin har muligheten til å utvide en klasse med ny funksjonalitet uten å arve fra klassen
eller bruke design patterns som f.eks. Decorator.

Du kan lage egne funksjoner for en klasse fra tredjeparts-biblioteker som du ellers ikke 
har koden til og kan modifisere. 

Du kan legge til funksjoner, properties også i companion-objektet
-->

---

# Extension functions

- Adding a square() function to Int

```kotlin
fun Int.square() = this * this
```

Receiver class Int gets a square function

```kotlin
val n = 43
val nSquared = n.square()
//val nSquared = MathUtil.square(n)
println("$n * $n == $nSquared")
> 43 * 43 == 1849
```

<!--
Merk Int. foran metodenavnet:
- Int is the receiver "type"/class
- n is the "receiver" object
-->
---

# Extend a nullable type

Can extend a nullable type to handle null values

```kotlin
fun Int.square() = this * this
fun Int?.square(): Int {
    if (this == null) return -42
    // after the null check, 'this' is autocast to a non-null type, so the square() above
    // resolves to the non-null square
    return this.square()
}
val nullInt: Int? = null
println(nullInt.square())
println(4.square())
> -42
> 16
```
<!--
Må vi ha eksplisitt for Int? også?
-->
---

# Extension properties

- Can add properties to classes
- lastIndex

```kotlin
val <T> List<T>.lastIndex: Int
    get() = size - 1

val numbers = listOf(10, 12, 13, 14)
println("last index is ${numbers.lastIndex}")

> last index is 3
```
<!--
Legge til nye properties i en klasse er likt å legge til en funksjon.
-->
---

# Extending companion object function

_If_ a class has a companion object it can be extended

```kotlin
class MyClass {
    companion object { }  // will be called "Companion"
}

fun MyClass.Companion.printCompanion() { println("companion") }

MyClass.printCompanion()

> companion
```
<!--
Gir kompileringsfeil hvis ikke klassen har en companion object fra før.
-->

---

# Quiz: What is printed?
```kotlin
open class Shape
class Rectangle: Shape()

fun Shape.getName() = "Shape"
fun Rectangle.getName() = "Rectangle"

fun printClassName(s: Shape) {
    println(s.getName())
}

printClassName(Rectangle())
```
* `> Shape`

<!--
Her har både super og subklassen den samme extension funksjonen.

=> Blir kalt basert på typen til referansen, ikke 
typen på det faktiske objektet...

Extension endrer ikke klassen de utvider, du gjør det bare
mulig å kalle den vi .notasjonen på en referanse av den typen.
-->


---
# What is printed?
Note that `printFunctionType` is defined in class and top level

```kotlin
class Example {
    fun printFunctionType() { println("Class method") }
}

fun Example.printFunctionType() { println("Extension function") }

Example().printFunctionType()
```

* `> Class method`
  - Class member "always win"

<!--
Hvis en funksjon er definert i en klasse og på toppnivå i en fil, vil den i klassen alltid vinne. 
-->
---
# Null handling

---
# Sealed class (and interface)

- Restricts possible subclasses
- All subclasses must be known at compile time
- Subclasses must be defined in the same package
- Sealed class is abstract
- Can have abstract methods

<!--
Bare en repetisjon på sealed classes før vi går inn på 
null handing. 
-->
---
# Java's Optional
- In Java, return Optional<T>, not null

```java
public Optional<String> middleName(){};
```

<!--
Null har historisk sett vært et stort problem og noen sier det er en av de store design-feilene i Java.

I Java 8 kom Optional som gjorde det mulig å kommunisere i signaturen til metoden at den ikke alltid returnerer en verdi. 

Tvinges til å håndtere at du ikke har en verdi.
-->
---
# Java's Optional class
- Fluent code
- Can operate on it
  - filter, map, flatmap, fold
```java
    var maybeValue = Optional.of("Forty Two")
        .filter( st -> isNumber(st) )
        .map( Integer::parseInt )
        .map( n -> n * n );
````

<!--
Med optional kan en også skrive mer deklarativt og flytende (som i Collections)
-->

---
# Null-handling in Kotlin
- Supported in the type system
  - String vs. String?
- Not foolproof in mixed projects
  - Tip: Use @Nullable and @NotNull in Java code

```kotlin
val value = getSomeNullableInt()
    ?.let { n -> if (n % 2 == 0) n else null }
    ?.let { n -> n * b }
    ?: 0
```
--- 
# Scala's Option class
```kotlin
sealed class Option<T>
class None<T>(): Option<T>()
class Some<T>(val value: T) : Option<T>()

val maybeSomething: Option<Int> = TODO()
when (maybeSomething){
    is None -> println("Nothing")
    is Some -> println("value=${maybeSomething.value}")
}
```
- Well suited for matching (when)
  - Compiler knows when all subclasses are matched
<!--
    Sealed class og alle subklasser definert.
    Godt egent for matching.
-->


---
# Optional lacks context
- Optional.empty() enables fluency
- Sometimes we want to know why it was empty

```kotlin
fun catchOptional(): Optional<String> {
    return try {
        Optional.of(someMethodCall())
    } catch (e: Exception) {
        Optional.empty()
        //No context or reason expressed
    }
}
````
<!--
Svakheten med Optional og nullbare typer er at vi har mistet
konteksten og ikke vet hvorfor det ikke er noen verdi der.
-->
---

# Either<L, R>
- Returns one of two values
- L - Left is failure
- R - Right is right/success

```kotlin
fun divide(a: Double, b: Double): Either<String, Double> =
    if (b == 0.0 ) {
        Either.Left("Cannot divide by zero")
    } else {
        Either.Right( a/b )
    }
```
<!--
- Sealed class Either, subklasser Left og Right.
- Er alltid en av de to subklassene.
- Right is Right/Correct value
- Left er kontekst for manglende verdi.
-->

---
# Mapping and filtering
- As with Optional
- In the end you've got a value
  - or a context why there is no value. 

<!--
Ingen kodeeksempler her, dere skal implementere det selv
etterpå.

Har du en Either, kan du kode like deklarativt som med Optional i Java, med noen unntak siden det kan være en av to verdier, ikke bare en verdi eller tomt.
-->

---
# map vs mapLeft
- Either.map: Right oriented
- Either.mapLeft: maps the error/left side
- Either.flatMap: If you map to an Either

<!--
Kan mappe begge sider. map() er høyre-orientert.
Egen mapLeft() for å endre typen på "årsaken"
Flatmap i collection hvis du mapper til en liste/collection,
flatMap i Either hvis du mapper til en Either
-->
---

# filter
- Either.filter takes two parameters
- a predicate: 
  - (R) -> Boolean
- a producer if predicate is negative:
  - () -> L
  - Must have a value Right or Left

<!--
Filter tar to verdier:
- predikat som vanlig
- producer hvis predikatet evalueres til false -> Må produsere en Left.
- Hvis typen er Left, evalueres ikke filter()
-->

---

# Exercise 1
- Extension functions
- Implement Either

<!--
- Implement a simple Either in Kotlin
  - Sealed class Either
  - Left and Right subclasses
  - filter()
  - map()
  - mapLeft()
  - getOrElse()
  - ...
-->
