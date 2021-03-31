package de.yupiel.helth.integration.web

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import de.yupiel.helth.domain.application.UserService
import de.yupiel.helth.domain.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController {
    @Autowired
    lateinit var userService: UserService

    @GetMapping
    fun showAllUsers() : String {
        val users = userService.findAll() ?: return "No users found... is the database ok?"

        return userListToJsonObject(users).toJsonString()
    }
    
    @PostMapping
    fun createNewUserByUsernameAndPassword(
        @RequestBody request: UserCreationRequest
    ): String {
        val username = request.username
        val password = request.password
        val userCreation = userService.save(request.username, request.password)

        return JsonObject(mapOf("id" to userCreation.toString())).toJsonString()
    }

    fun userToJsonObject(user: User): JsonObject{
        return JsonObject(
            mapOf(
                "id" to user.id.toString(),
                "username" to user.username
            )
        )
    }
    fun userListToJsonObject(users: List<User>) :JsonArray<JsonObject>{
        val returnValue: JsonArray<JsonObject> = JsonArray()

        users.forEach {
            returnValue.add(userToJsonObject(it))
        }

        return returnValue
    }

    data class UserCreationRequest(
        val username: String,
        val password: String
    )
}