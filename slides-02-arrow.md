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
- media i hovedmenyen for kilder til opplæring.

Videre skal vi se på noen måter å bruke Kotlin Arrow på.
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

# Either block
```kotlin
either {
  val email: Email = getEmailEither().bind()
  val username: Username = getUsernameEither().bind()
  User(email, username)
}
```

<!--
either{} sammen med .bind()
Vil returnerer en Left på første som (ikke er Right)
- unboxer fra Right
Returnerer til slutt en Right av siste verdi
-->

--- 
# Either: catch and mapLeft

```kotlin
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
- Validated values

<!--
Nå skal vi se på et tema som har fasinert meg siden JavaZone 2021 sist desember.
Hvordan en med ett enkelt grep kan
- Får bedre typesignaturer
- Garantere gyldige verdier inn til applikasjonen
- Samlet logikk om gyldige verdier i felter der hvor det hører hjemme.
-->
---

# Domain types
Given a class `Event` with the signature:
```kotlin
Event(String, String, LocalDateTime)
```

- What are the two Strings?

```kotlin
Event(organizer: String, title: String, eventDate: LocalDateTime)
```

<!--
Får mye hjelp fra moderne IDE-er om hva hver paramter er, men ingen hjelp fra kompilatoren om du gjør feil. 

- How hard is it to find out/remember what is what?
- How easy is it to mix the arguments
- Where do you put business logic for valid values?
- Where do you validate values?
- Can we still instanciate invalid User objects?
- Did you note that the constructor can throw exception?

Å navngi parametrene er derfor lurt.

Valideringsregler som lengde på brukernavn, gyldige tegn,
gyldig e-postadresse osv. Ligger gjerne et helt annet sted.

Er likevel mulig å instansiere et slikt objekt med ugyldige verdier.
-->

---
# Inline classes

```kotlin
// For JVM backends include @JvmInline
@JvmInline
value class Organizer(val value: String)
```
https://kotlinlang.org/docs/inline-classes.html



<!--
Noen ganger er det nyttig å lage en wrapper-klasse rundt en primitiv for å ha et sted å putte forretningslogikken.

Primitive typer er optimalisert runtime, mens klasser som instansieres krever heap allokeringer, noe som gir en performance kostnad.

Kotlin har introdusert en spesialklasse, inline klasse.
Koder som om det var en vanlig klasse, men runtime performance som en primitiv.
-->

---

# Inline classes as custom domain types
```kotlin
data class Event(val organizer: String, val title: String, val eventDate: LocalDateTime)
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
                throw java.lang.IllegalArgumentException("Organizer is null or empty string")
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
                ValidationError("Organizer is null or empty string").left()
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

data class ValidationError(val message: String)

typealias ValidationErrors = NonEmptyList<ValidationError>
```

<!--
Arrow har en Validated som ligner på Either.

Basisklassen kan ikke instansieres, så objektet som kommer tilbake er enten Valid eller Invalid.

Semantisk uttrykker det bedre hva vi har enn en Either.

Typisk brukes Validated for å samle opp feil, mens Either har short circuit på første

Eksemplene bruker en dataklasse ValidationError for én valideringsfeil

Lager typealias for en ikke-tom liste med valideringsfeil. Arrows NonEmptyList (NEL)
-->

---
# Organizer with Validated
- Business rules for valid values in same class
- Business rules always enforced

```kotlin
@JvmInline
value class Organizer private constructor(val value: String) {
    companion object {
        fun newInstance(candidate: String): Validated<ValidationErrors, Organizer> = when {
            candidate.isEmpty() || candidate.isBlank() ->
                ValidationError("Organizer is null or empty string").invalidNel()
                //ValidationError("Organizer is null or empty string").nel().invalid()
            else -> Organizer(candidate).valid()
        }
    }
}
```

<!--
Forretningens regler og logikk samlet rundt feltet/typen som regelen gjelder for.

Kan ikke opprette en Organisator med ugyldig verdi

Mer omfattende regler som krever til gang til andre data må ligge utenfor som før,
f.eks. sjekke om en e-post finnes fra før i databasen. 

Reglene som sjekkes her er med lengde, format, syntaks verdiområde, gyldige tegn osv.
-->

---

# Håndtere flere valideringsfeil

```kotlin
fun createEvent(organizer: String, title: String, eventDate: LocalDateTime)
    : Validated<ValidationErrors, Event> {

    // The three are of type Validated<ValidationErrors, XType>
    val validatedOrganizer: Organizer.newInstance(organizer)
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
zip() Tar vanligvis to lister og parer verdier (en fra hver av dem) til en liste av par.

Semigroup er en struktur som bare har en operasjon, pluss, for å kombinere to elementer.

For alle Invalid blir listen med valideringsfeil lagt til "semigruppen".
Lambdaen til slutt blir bare kalt om alle elementene er Valid, og argumentene inn er 
selve typene, ikke pakket inn, så lambdaen kan bruke dem uten å hente ut eller konvertere.

Resultatet er da enten Valid<Event> eller Invalid<ErrorMessages>

Max på 9 argumenter...
-->

---

# Exercises 2
- In the folder exercises02 is a SpringBoot application
- Read exercises-02-arrow.md for instructions.

Should rewrite application by using inline classes and Validation.