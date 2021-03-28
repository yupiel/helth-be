package de.yupiel.helth.integration.web

import com.beust.klaxon.JsonObject
import de.yupiel.helth.domain.application.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController {
    @Autowired
    lateinit var userService: UserService
    
    @PostMapping
    fun createNewUserByUsernameAndPassword(
        @RequestBody request: UserCreationRequest
    ): String {
        val userCreation = userService.createNewUser(request.username, request.password)

        return JsonObject(mapOf("id" to userCreation.toString())).toJsonString()
    }

    data class UserCreationRequest(
        val username: String,
        val password: String
    )
}