---
theme: default
_class: lead
paginate: true
backgroundColor: #fff
marp: true
---
# Part 2
- Arrow kotlin library
  - https://arrow-kt.io/
  - "standard" library for functional programming in Kotlin
- Optional, Either 
- Validated, NonEmptyList (NEL)

<!--
De-facto standardbibliotek for funksjonell programmering i Kotlin. Støtter både:
- Optional, Either
- Validated, NonEmptyList (NEL)
-->

---
# Either with extension functions
- left(), right() extension functions
```
fun divideExtensionFunctions(a: Double, b: Double): Either<String, Double> =
  if (b == 0.0) {
    "Cannot divide by zero".left()
  } else {
    (a / b).right()
}
```

<!--
Har extendions functions som wrapper Left og Right
-->

---
# Either with catch

```kotlin
    fun divideCatch(a: Double, b: Double): Either<Throwable, Double> =
        Either.catch { a/b }
```

<!--
Hvis exception, Left ellers Right
Merk at her blir Left av typen Throwable
-->

---
# Either block ???
either {
  val email = getEmailEither().bind()
}
- Ha med?

--- 
# Either: catch and mapLeft

```
    fun divideCatchMapLeft(a: Double, b: Double): Either<String, Double> =
        Either.catch { a/b }
            //.mapLeft { leftValue: Throwable -> "Cannot divide by zero" }
            //.mapLeft { _: Throwable -> "Cannot divide by zero" }
            //.mapLeft { _ -> "Cannot divide by zero" }
            .mapLeft { "Cannot divide by zero" }
```
<!--
Eksempel på map left. Mange måter, velg din foretrukne.
-->

---
# Either: getting the value

```kotlin
val either: Either<String, Double> = divideCatchMapLeft(42.0, 2.0)

val orNull = either.orNull()
val orElse = either.getOrElse { 0.0 }
when (either) {
    is Either.Left -> println("String: ${either.value}") //String
    is Either.Right -> println("Double: ${either.value}") //Double
}
val orHandle: Double = either.getOrHandle { leftValue: String -> 
    LOG.error(leftValue)
    0.0             
}
```

<!--
Hvis vi trenger å få en verdi ut fra en Either.
- orNull(), selvforklarende
- orElse, en producer eller en defaultverdi hvis Left
- pattern matching
- orHandle() med lambda som kalles om det er en Left.
-->

---
# Ensuring valid objects
- Type safety
- validated values

---

# Domain types
```kotlin
Event(String, String, LocalDateTime)
```
- Constructor has signature (String, String, LocalDateTime)
  - How hard is it to find out/remember what is what?
  - How easy is it to mix the arguments
  - Where do you put business logic for valid values?
  - Where do you validate values?
  - Can we still instanciate invalid User objects?
  - Did you note that the constructor can throw exception?
- Consider always naming the arguments.... 

<!--
Får mye hjelp fra moderne IDE-er om hva hver paramter er, men ingen hjelp fra kompilatoren om du gjør feil. 

Å navngi parametrene er derfor lurt.

Valideringsregler som lengde på brukernavn, gyldige tegn,
gyldig e-postadresse osv. Ligger gjerne et helt annet sted.

Har likevel ingen garanti for at verdiene ER validerte før User-objektet instansieres.
-->
---
# Inline classes

```kotlin
// For JVM backends include @JvmInline
@JvmInline
value class Organizer(val value: String)
```
https://kotlinlang.org/docs/inline-classes.html#members



<!--
Noen ganger er det nyttig å lage en wrapper-klasse rundt en primitiv for å ha et sted å putte forretningslogikken.

Primitive typer er optimalisert runtime, mens klasser som instansieres krever heap allokeringer, noe som gir en performance kostnad.

Kotlin har introdusert en spesialklasse, inline klasse.
Koder som om det var en vanlig klasse, men runtime performance som en primitiv.
-->
---
# Inline classes & Custom domain types
```kotlin
data class Event(val organizer: String, val title: String, val data: LocalDateTime)
````
```kotlin
@JvmInline
value class Organizer(val value: String)
@JvmInline
value class Title(val value: String)
@JvmInline
value class EventDate(val value: LocalDateTime)
```
```kotlin
data class Event(Organizer, Title, EventDate)
```
<!--
Hvis vi lager egne typer for enkle verdier, får vi en signatur som er helt eksplisitt og kompilatoren gir feil dersom du blander dem.

Så skal vi se videre på hvor vi kan putte regler for hver type og hindre at det instansieres objekter med ugyldig verdi.
-->

---
# Private constructor & factory method
```kotlin
@JvmInline
value class Organizer private constructor(val value: String) {
    companion object {
        fun newInstanceOrNull(candidate: String): Organizer? = when {
            candidate.isEmpty() || candidate.isBlank() -> null
            else -> Organizer(candidate)
        }
        fun newInstanceOrException(candidate: String): Organizer = when {
            candidate.isEmpty() || candidate.isBlank() ->
                throw java.lang.IllegalArgumentException("Organizer is null og empty string")
            else -> Organizer(candidate)
        }
    }
}
```
<!--
Ønsker ikke null-sjekk over alt.

Ingen steder du MÅ skrive at exception kan kastes hvis ikke du leser koden, og det gjør man sjelden.
-->
---
# Private constructor & factory method
- Either ?
```kotlin
data class ValidationError(val reason: String)
@JvmInline
value class Organizer private constructor(val value: String) {
    companion object {
        fun newInstance(candidate: String): Either<ValidationError, Organizer> = when {
            candidate.isEmpty() || candidate.isBlank() -> 
                ValidationError("Organizer is null og empty string").left()
            else -> Organizer(candidate).right()
        }
    }
}
```
<!--
Either virker, men hvis det er mange valideringsregler eller når en form sendes inn er det kanskje mange felter som skal valideres, og Either gir bare tilbake første feil her.
-->

---
# Validated
- Arrow's Validated
  - https://arrow-kt.io/docs/apidocs/arrow-core/arrow.core/-validated/

```kotlin
sealed class Validated
data class Invalid<E>(value: E) : Validated<E, Nothing>
data class Valid<T>(value: T) : Validated<Nothing, T>
```

<!--
Arrow har en Validated som ligner på Either.

Basisklassen kan ikke instansieres, så objektet som kommer tilbake er enten Valid eller Invalid.

Semantisk uttrykker det bedre hva vi har enn en Either.
-->

---
# Organizer with Validated
- Business rules for valid values in same class
- Business rules always enforced

```kotlin
typealias ValidationErrors = NonEmptyList<ValidationError>

@JvmInline
value class Organizer private constructor(val value: String) {
    companion object {
        fun newInstance(candidate: String): Validated<ValidationErrors, Organizer> = when {
            candidate.isEmpty() || candidate.isBlank() ->
                ValidationError("Organizer is null og empty string").invalidNel()
                //ValidationError("Organizer is null og empty string").nel().invalid()
            else -> Organizer(candidate).valid()
        }
    }
}
```

<!--
Forretningens regler samlet rundt feltet/typen som regelen gjelder for.

Kan ikke opprette en Organisator med ugyldig verdi

Signaturen blir mer type-sikre
-->
---
# Håndtere flere valideringsfeil

```kotlin
fun createEvent(organizer: String, title: String, eventDate: LocalDateTime): Validated<ValidationErrors, Event> {
    val validatedOrganizer = Organizer.newInstance(organizer)
    val validatedTitle = Title.newInstance(title)
    val validatedDate = EventDate.newInstance(eventDate)
    
    val validatedEvent: Validated<ValidationErrors, Event> = 
        validatedOrganizer.zip(Semigroup.nonEmptyList(), 
            validatedTitle, 
            validatedDate) { validOrganizer, validTitle, validDate ->
        Event(validOrganizer, validTitle, validDate)
    }
    return validatedEvent
}
```
<!--
Ignoring the first parameter, from.zip(…, to) works in a similar way, the difference being that, unlike a list, from and to can have at most one (valid!) element. The parameters of our lambda (validFrom, validTo) will always be Valid instances. If any of the emails is not valid, zip won’t invoke our lambda, and it will return an Invalid.

Now let’s go to the first parameter: Semigroup.nonEmptyList(). Sounds scary? A semigroup is one of the most basic structures: it only has one (binary) operation to combine two elements. We can create a semigroup of lists because we have an operation (plus) that we can use to combine two lists. Thus by providing Semigoup.nonEmptyList() as the first parameter, we’re just specifying that, for invalid instances, we want to concatenate non-empty lists.
-->

---