package de.yupiel.helth.domain.application

import de.yupiel.helth.authentication.AuthenticationService
import de.yupiel.helth.domain.integration.UserRepository
import de.yupiel.helth.domain.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val authenticationService: AuthenticationService
) {
    fun findAll() : List<User>? {
        return try {
            val users = userRepository.findAll().toList()
            if(users.isEmpty()) return null

            users
        } catch (exception: Exception) {
            return null
        }
    }

    fun findByUsername(username: String): User? {
        return try {
            val users = userRepository.findByUsername(username)
            if (users.size > 1) throw IndexOutOfBoundsException()

            users[0]
        } catch (tooManyUsers: IndexOutOfBoundsException) {
            null
        }
    }

    fun save(username: String, password: String): User? {
        return try {
            if (!usernameMeetsMinimumRequirements(username)) return null
            if (!passwordMeetsMinimumRequirements(password)) return null

            val encryptedPassword = authenticationService.encodePassword(password)
            val newUser = User(username, encryptedPassword)

            this.userRepository.save(newUser)
        } catch (exception: Exception) {
            null
        }
    }

    private final val USERNAME_MIN_LENGTH = 4
    private fun usernameMeetsMinimumRequirements(username: String): Boolean {
        if (username.length >= USERNAME_MIN_LENGTH
            && this.findByUsername(username) == null    //Check if username exists already
        ) {
            return true
        }
        return false
    }

    private final val PASSWORD_MIN_LENGTH = 8
    private fun passwordMeetsMinimumRequirements(password: String): Boolean {
        if (password.length >= PASSWORD_MIN_LENGTH) {
            return true
        }
        return false
    }
}