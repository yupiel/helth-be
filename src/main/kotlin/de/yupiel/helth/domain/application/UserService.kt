package de.yupiel.helth.domain.application

import de.yupiel.helth.authentication.AuthenticationService
import de.yupiel.helth.domain.integration.IUserRepository
import de.yupiel.helth.domain.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    @Autowired private val userRepository: IUserRepository,
    @Autowired private val authenticationService: AuthenticationService
) {

    fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    fun createNewUser(username: String, password: String): UUID? {
        if (usernameMeetsMinimumRequirements(username)) return null
        if (!passwordMeetsMinimumRequirements(password)) return null

        val encryptedPassword = authenticationService.encodePassword(password)
        val newUser = User(username, encryptedPassword)

        return userRepository.createUser(newUser)
    }

    private final val USERNAME_MIN_LENGTH = 4
    private fun usernameMeetsMinimumRequirements(username: String): Boolean {
        if (username.length > USERNAME_MIN_LENGTH
            && this.findByUsername(username) == null    //Check if username exists already
        ) {
            return true
        }
        return false
    }

    private final val PASSWORD_MIN_LENGTH = 8
    private fun passwordMeetsMinimumRequirements(password: String): Boolean {
        if (password.length > PASSWORD_MIN_LENGTH) {
            return true
        }
        return false
    }
}