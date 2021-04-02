package de.yupiel.helth.user.integration

import com.sun.istack.NotNull
import de.yupiel.helth.user.application.UserService
import de.yupiel.helth.user.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users", produces = [MediaType.APPLICATION_JSON_VALUE])
class UserController(
    @Autowired val userService: UserService
) {
    @GetMapping
    fun showAllUsers(): List<UserDto> {
        val users = userService.getAllUsers()

        return users.map {
            UserDto.from(it)
        }
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun createNewUserByUsernameAndPassword(
        @RequestBody request: UserCreationRequest
    ): UserDto {
        val userCreation = userService.createUser(request.username, request.password)

        return UserDto.from(userCreation)
    }

    data class UserCreationRequest(
        @NotNull val username: String,
        @NotNull val password: String
    )

    data class UserDto(
        val id: String,
        val username: String,
        val creationDate: String
    ) {
        companion object {
            fun from(user: User): UserDto {
                return UserDto(user.id.toString(), user.username, user.creationDate.toString())
            }
        }
    }
}