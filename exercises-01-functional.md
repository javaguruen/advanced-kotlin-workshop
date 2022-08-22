# Extensions


## polite()
Add extension function `polite()` to String that returns 'Sir ' prepended to the string when invoked.

## isEven()
Add extension function `isEven`to Int that returns true if number is even

## isOdd()
Add extension function `isOdd()` to Int that returns true if number is odd

## toOptional()
Add extension function `toOptional()` to all types (including nullable types). When invoked it
will return an Optional<E> which is either empty or contains the receiving object.

# Either
Create a sealed class `Either<L, R>` with two subclasses: 

```kotlin
data class Left<L>(val value: L) : Either<L, Nothing>(){
}
```

```kotlin
data class Right<R>(val value: R) : Either<Nothing, R>(){
}
```

Nothing has no instances. You can use Nothing to represent "a value that never exists": 
for example, if a function has the return type of Nothing, it means that it never returns (always throws an exception). 

In the case of `Left` and `Right` we use `Nothing`to say that that type is irrelevant and will not be used. 

## getOrNull
Add an abstract method in `Either` with the signature `abstract fun getOrNull(): R?`.
Implement this method in both Left and Right subclass.
* Blir problem med typing når denne skal implementeres. Finner på noe lurt

## flatmap as extension function
At the bottom of the Either.kt file, iImplement an extension method 
`inline fun <L, R, U> Either<L, R>.flatMap(f: (R) -> Either<L, U>): Either<L, U>`

## map (right)
In the Either class, implement a map method `inline fun <U> map(f: (R) -> U): Either<L, U>`.
Implement this method using the flatmap extension function from the previous step.

## mapLeft
In the Either class, implement a map method `inline fun <U> map(f: (R) -> U): Either<L, U>`.
Implement this method using the flatmap extension function from the previous step.

## filter
implement filter as an extension function 

## .left and .right() as extension functions
