package com.hamre.javazone.advkotlin.application.rest

import com.hamre.javazone.advkotlin.application.domain.UserService
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.util.UUID

@RestController
@RequestMapping(value = ["/api/v1/users"], produces = [APPLICATION_JSON_VALUE])
class UserController(
    private val userService: UserService
) {
    @PostMapping(path=["/"], consumes = [APPLICATION_JSON_VALUE])
    fun createUser(newUser: User): ResponseEntity<User> {
        val domainUser = Api2DomainMapper.mapUser(newUser)
        val createdUser = userService.createUser(domainUser)
        return ResponseEntity.created(URI("http://localhost:8080/api/v1/users/${createdUser.id}")).build()
    }
}

data class User(
    val id: UUID?,
    val username: String,
    val email: String,
)
