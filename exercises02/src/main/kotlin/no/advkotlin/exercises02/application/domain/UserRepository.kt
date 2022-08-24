package no.advkotlin.exercises02.application.domain

import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserRepository {

    fun saveUser(user: User): User{
        if( users.none { it.email == user.email }){
            val userWithId = user.copy(id = UUID.randomUUID())
            users.add(userWithId)
            return userWithId
        } else {
            throw java.lang.IllegalArgumentException("Email must be unique")
        }
    }

    companion object DATASTORE {
        private val users: MutableSet<User> = mutableSetOf()
    }
}
