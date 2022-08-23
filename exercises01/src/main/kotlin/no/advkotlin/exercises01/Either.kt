package no.advkotlin.exercises01

/*
This is where you will implement the Either type
*/

sealed class Either<out L, out R>{
    abstract fun getOrNull(): R?
    inline fun <U> map(f: (R) -> U): Either<L, U>{
        return flatMap { Right(f(it)) }
    }

    inline fun <U> mapLeft(f: (L) -> U): Either<U, R>{
        return when(this){
            is Left -> Left(f(this.value))
            is Right -> this
        }
    }

    data class Left<L>(val value: L) : Either<L, Nothing>(){
        override fun getOrNull() = null
    }

    data class Right<R>(val value: R) : Either<Nothing, R>(){
        override fun getOrNull(): R? = value
    }
}

inline fun <L, R, U> Either<L, R>.flatMap(f: (R) -> Either<L, U>): Either<L, U>{
    return when (this){
        is Either.Left -> this
        is Either.Right -> f(this.value)
    }
}

inline fun<L, R> Either<L, R>.filter(
    predicate: (R) -> Boolean,
    orElse: () -> L
): Either<L, R>{
    return when (this){
        is Either.Left -> this
        is Either.Right -> if (predicate(this.value)) this else Either.Left(orElse())
    }
}

fun <E : Any, R> E.left(): Either<E, R> = Either.Left(this)
fun <L, E : Any> E.right(): Either<L, E> = Either.Right(this)
