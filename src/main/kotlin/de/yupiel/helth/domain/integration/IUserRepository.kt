package de.yupiel.helth.domain.integration

import de.yupiel.helth.domain.model.User
import java.util.*

interface IUserRepository {
    fun findByUsername(username: String): User?
    fun createUser(user: User): UUID
}