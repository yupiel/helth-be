package de.yupiel.helth.user.application

import de.yupiel.helth.authentication.application.AuthenticationService
import de.yupiel.helth.common.NotFoundException
import de.yupiel.helth.common.RequestBodyException
import de.yupiel.helth.user.model.UserRepository
import de.yupiel.helth.user.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val authenticationService: AuthenticationService
) {
    fun getAllUsers(): List<User> {
        val users = userRepository.findAll().toList()
        if (users.isEmpty()) throw NotFoundException("No Users found")
        else return users
    }

    fun getUserWithUsername(username: String): User? {
        val foundUserList = this.userRepository.findByUsername(username)
        return if (foundUserList.isEmpty()) null
        else foundUserList[0]
    }

    fun createUser(username: String, password: String): User {
        checkUsernameMeetsMinimumRequirements(username)
        checkPasswordMeetsMinimumRequirements(password)

        val encryptedPassword = authenticationService.encodePassword(password)
        val newUser = User(username, encryptedPassword)

        return this.userRepository.save(newUser)
    }

    private final val USERNAME_MIN_LENGTH = 4
    private fun checkUsernameMeetsMinimumRequirements(username: String) {
        if (username.length < USERNAME_MIN_LENGTH) throw RequestBodyException("Username must be at least $USERNAME_MIN_LENGTH characters long")
        if (this.getUserWithUsername(username) != null) throw RequestBodyException("User with username '$username' already exists")
    }

    private final val PASSWORD_MIN_LENGTH = 8
    private fun checkPasswordMeetsMinimumRequirements(password: String) {
        if (password.length < PASSWORD_MIN_LENGTH) throw RequestBodyException("Password must be at least $PASSWORD_MIN_LENGTH characters long")
    }
}