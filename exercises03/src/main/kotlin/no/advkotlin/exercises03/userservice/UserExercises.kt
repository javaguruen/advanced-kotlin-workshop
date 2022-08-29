package no.advkotlin.exercises03.userservice

import no.advkotlin.exercises03.userservice.domain.Product
import no.advkotlin.exercises03.userservice.domain.User
import no.advkotlin.exercises03.userservice.service.UserService

class UserExercises(val userService: UserService) {

    suspend fun getUserAndItemsCoroutine(userId: String): Pair<User, List<Product>> = TODO()

    suspend fun getUserAndItemsConcurrent(userId: String): Pair<User, List<Product>> = TODO()

    suspend fun getUsersAndItemsInBulk(userIds: List<String>): List<Pair<User, List<Product>>> = TODO()
}