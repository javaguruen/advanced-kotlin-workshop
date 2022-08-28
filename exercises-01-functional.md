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
In the file no.advkotlin.exxercises01.Either.kt:

Create a sealed class `Either<out L, out R>` with two (inner) subclasses: 

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

## flatmap as extension function
At the bottom of the Either.kt file, implement an extension method for flatmap
with the following signature:
```inline fun <L, R, U> Either<L, R>.flatMap(f: (R) -> Either<L, U>): Either<L, U>```
Remember that flatmap is right oriented and will not be applied if the Either is a Left.

## map (right)
In the Either class, implement a map method `inline fun <U> map(f: (R) -> U): Either<L, U>`.
Implement this method using the flatmap extension function from the previous step.

Remember that map is right oriented and will not be applied if the Either is a left.

## mapLeft
In the Either class, implement a map method `inline fun <U> mapLeft(f: (L) -> U): Either<U, R>`.
Implement this using matching (when) in the Either class.

mapLeft should map the Left value but do nothing if the Either is a right

## filter
implement filter as an extension function. The filter function should take two arguments:
 1. the predicate (for the Right value)
 2. a producer creating a Left value if the predicate evaluates to false
The filter should not be applied if the object is a Left

## left() and right() as extension functions
In the Either.kt file, implement extension functions left() and right() so that
any non-null object can be lifted to a Left og Right of that type.
