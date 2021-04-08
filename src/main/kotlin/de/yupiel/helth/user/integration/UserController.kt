package de.yupiel.helth.user.integration

import com.sun.istack.NotNull
import de.yupiel.helth.activity.application.ActivityService
import de.yupiel.helth.authentication.application.AuthenticationService
import de.yupiel.helth.challenge.application.ChallengeService
import de.yupiel.helth.common.RequestBodyException
import de.yupiel.helth.user.application.UserService
import de.yupiel.helth.user.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users", produces = [MediaType.APPLICATION_JSON_VALUE])
class UserController(
    @Autowired val userService: UserService,
    @Autowired val activityService: ActivityService,
    @Autowired val challengeService: ChallengeService,
    @Autowired val authenticationService: AuthenticationService
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
        checkUsernameMeetsMinimumRequirements(request.username)
        checkPasswordMeetsMinimumRequirements(request.password)

        val encryptedPassword = authenticationService.encryptPassword(request.password)
        val userCreation = userService.createUser(request.username, encryptedPassword)

        return UserDto.from(userCreation)
    }

    @DeleteMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Successfully deleted user account.")
    fun deleteExistingUser(
        @RequestHeader("Authorization") authorizationHeader: String
    ) {
        val userID = this.authenticationService.extractUserIDFromAuthorizationHeader(authorizationHeader)

        this.userService.deleteUserAccount(userID)
        this.activityService.deleteActivitiesFromUserID(userID)
        this.challengeService.deleteChallengesFromUserID(userID)
    }

    data class UserCreationRequest(
        @NotNull val username: String,
        @NotNull val password: String
    )

    data class UserDto(
        val id: String,
        val username: String,
        val score: String,
        val creationDate: String
    ) {
        companion object {
            fun from(user: User): UserDto {
                return UserDto(user.id.toString(), user.username, user.score.toString(), user.creationDate.toString())
            }
        }
    }

    //HELPER FUNCTIONS
    private final val USERNAME_MIN_LENGTH = 4
    private fun checkUsernameMeetsMinimumRequirements(username: String) {
        if (username.length < USERNAME_MIN_LENGTH) throw RequestBodyException("Username must be at least $USERNAME_MIN_LENGTH characters long")
        if (this.userService.getUserWithUsername(username) != null) throw RequestBodyException("User with username '$username' already exists")
    }

    private final val PASSWORD_MIN_LENGTH = 8
    private fun checkPasswordMeetsMinimumRequirements(password: String) {
        if (password.length < PASSWORD_MIN_LENGTH) throw RequestBodyException("Password must be at least $PASSWORD_MIN_LENGTH characters long")
    }
}