# Coroutine Exercises

## Part 1 - Warm up

In these warm up exercises we will convert some simple functions to use coroutines. 

In [no.advkotlin.exercises03.warmup.WarmupExercises](src/main/kotlin/no/advkotlin/exercises03/warmup/WarmupExercises.kt)
there are 3 functions that should be converted to use coroutines.

### Hello World

```kotlin
fun helloWorld(): String
```

This function starts two threads to produce a particular String. Convert it to use two coroutines
instead. There is a corresponding test in 
[WarmupExercisesTest](src/test/kotlin/no/advkotlin/exercises03/warmup/WarmupExercisesTest.kt) 
that you might have to modify as well.

### Max Elements

```kotlin
fun maxElement(a: List<Int>, b: List<Int>): Int
```

The second function, takes two lists and returns the overall highest element. Change the function so
that two concurrent coroutines are started, one for finding the max of each list. And then when they
are done, the overall highest element is returned.

### Sum Lists

```kotlin
fun sumLists(lists: List<List<Int>>): Int
```

The last function takes a list of lists, and computes the total sum of all the lists together.
Make it start one coroutine to sum each list, and then compute the total sum when all coroutines
are done.

## Part 2 - User Service

In these exercises we will call some methods on the service 
[no.advkotlin.exercises03.userservice.service.UserService](src/main/kotlin/no/advkotlin/exercises03/userservice/service/UserService.kt)
a simple service with to methods. One to retrieve a `User` from a `userId`. And one to retrieve a list `List<Product>`
belonging to this user. This service has some hard coded data, and a built in delay of 1 second for each 
method. This is to simulate a service that would retrieve the data from a database or network call.


```kotlin
suspend fun getUser(userId: String): User
    
suspend fun getProducts(userId: String): List<Product>
```

The functions that you will implement are in
[no.advkotlin.exercises03.userservice.UserExercises](src/main/kotlin/no/advkotlin/exercises03/userservice/UserExercises.kt). 
Corresponding tests are in
[UserExercisesTest](src/test/kotlin/no/advkotlin/exercises03/userservice/UserExercisesTest.kt). 
The test are commented out as they will no succeed until the exercises are done. You can 
comment in the tests one by one as you work through implementing the functions.

Notice that the tests use the `runTest` coroutine builder which provides a special TestDispatcher that will keep
track of virtual time. The `delay(1000)` statements in the UserService, will return immediately, but the TestDispatcher
will keep track of how long it would take if there had been a delay of 1 second. The tests uses this to assert the
run time of the functions. But this only works if the TestDispatcher is inherited down to the service we are testing,
so you should avoid specifying a dispatcher in your solution, and not use `runBlocking` as that has it's own way of dispatching.

The functions you should implement are:

```kotlin
suspend fun getUserAndItemsCoroutine(userId: String): Pair<User, List<Product>> 
```

Given a user id, this function should use the `UserService` and return a pair of `User` and `List<Product>`. This should
be done sequentially in a single coroutine, so the tests expects it to finish in 2 seconds (two calls to the service)

```kotlin
suspend fun getUserAndItemsConcurrent(userId: String): Pair<User, List<Product>>
```

Now we will try to improve running time by doing the two calls to `UserService` concurrently. The expected running
time should thus be 1 second (two concurrent calls to the service)

```kotlin
suspend fun getUsersAndItemsInBulk(userIds: List<String>): List<Pair<User, List<Product>>>
```

Finally, implement a function that given a lists of userIds returns a list of pairs of users and products. All
calls to the service should be done concurrently, so the virtual running time should still be only 1 second.

## Part 3 - Timetable

In this exercise we are looking to improve an airport departures information screen. The data is rather static,
and the service we are using have some built in delays to simulate network traffic when fetching timetables.

There are two services we will use,
[no.advkotlin.exercises03.timetable.service.TimetableService](src/main/kotlin/no/advkotlin/exercises03/timetable/service/TimetableService.kt)
gives a list of the next departures as they are scheduled, 
and
[no.advkotlin.exercises03.timetable.service.RealTimeService](src/main/kotlin/no/advkotlin/exercises03/timetable/service/RealTimeService.kt)
which is used to get the live departure time for a particular flight. The `RealTimeService`
is unfortunately a little unreliable, so once in a while it will throw a `TimeoutException`

The
[no.advkotlin.exercises03.timetable.service.DepartureFetcher](src/main/kotlin/no/advkotlin/exercises03/timetable/service/DepartureFetcher.kt)
uses these services, to first fetch the timetable, and then for each departure in the table
fetch the live time. 

Your task is to improve `DepartureFetcher` by using coroutines to fetch all the live times concurrently. You should see
the delay between the header and table being printed come down significantly when you run the application.

The application can be run directly from your IDE, but looks a little better when run in a terminal 
(suporting ansi color and clear-screen codes)

you can make a runnable jar file by doing `mvn package` in the exercises03 directory, and then run the application
with `java -jar target/coroutines-0.0.1-SNAPSHOT-jar-with-dependencies.jar`

## Part 4 - Extra credit

Here are some extra things do try out if you've come this far

### Debugging Coroutines

Try to add some brakpoints to the timetable application, and look at what information intellij can show you on
active coroutines. Remember to select to stop all thread for the brakpoint, or it might not work.

Try to add the DebugProbe agent to the application, and dump coroutines with DebugProbe.dumpCoroutines()
Remember to call `DebugProbes.install()` when the application starts. And make sure to do the dump from
a place where the coroutines are active.

### Restful Coroutines

Going back to the SpringBoot application in Exercises 2, try to experiment with adding some suspending functions
to the rest controller. For instance, you could try to make a new endpoint to fetch several users in bulk. 
Add a `getUser(id: UUID)` to the `UserRepository` and `UserService`, and use `async` in the controller to fetch
all the users concurrently.

To be able to use suspending functions and coroutines in the spring boot application you should add these 
dependencies:

```xml
<dependency>
    <groupId>org.jetbrains.kotlinx</groupId>
    <artifactId>kotlinx-coroutines-core-jvm</artifactId>
    <version>1.6.4</version>
</dependency>
<dependency>
    <groupId>org.jetbrains.kotlinx</groupId>
    <artifactId>kotlinx-coroutines-reactor</artifactId>
    <version>1.6.4</version>
</dependency>
```