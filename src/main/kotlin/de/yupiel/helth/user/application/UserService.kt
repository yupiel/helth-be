package de.yupiel.helth.user.application

import de.yupiel.helth.common.NotFoundException
import de.yupiel.helth.user.model.UserRepository
import de.yupiel.helth.user.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    @Autowired private val userRepository: UserRepository
) {
    fun getUserByID(userID: UUID): User {
        val retrieved = this.userRepository.findById(userID)
        if(retrieved.isEmpty) throw NotFoundException("User with id $userID does not exist")
        else return retrieved.get()
    }

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
        val newUser = User(username, password)

        return this.userRepository.save(newUser)
    }

    fun deleteUserAccount(userID: UUID) {
        this.userRepository.deleteById(userID)
    }
}