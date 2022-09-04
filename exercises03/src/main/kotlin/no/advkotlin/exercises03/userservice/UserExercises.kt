package no.advkotlin.exercises03.userservice

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import no.advkotlin.exercises03.userservice.domain.Product
import no.advkotlin.exercises03.userservice.domain.User
import no.advkotlin.exercises03.userservice.service.UserService

class UserExercises(val userService: UserService) {

    suspend fun getUserAndItemsCoroutine(userId: String): Pair<User, List<Product>> = coroutineScope {
        userService.getUser(userId) to userService.getProducts(userId)
    }

    suspend fun getUserAndItemsConcurrent(userId: String): Pair<User, List<Product>> = coroutineScope {
        val user = async {
            userService.getUser(userId)
        }
        val products = async {
            userService.getProducts(userId)
        }
        user.await() to products.await()
    }

    suspend fun getUsersAndItemsInBulk(userIds: List<String>): List<Pair<User, List<Product>>> = coroutineScope {
        val usersAndProducts = userIds.map {
            async { userService.getUser(it) } to async { userService.getProducts(it) }
        }
        usersAndProducts.map {
            it.first.await() to it.second.await()
        }
    }
}