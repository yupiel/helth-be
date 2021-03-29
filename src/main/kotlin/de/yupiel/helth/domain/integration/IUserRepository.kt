package de.yupiel.helth.domain.integration

import de.yupiel.helth.domain.model.User
import java.time.LocalDate
import java.util.*

interface IUserRepository {
    fun findByUsername(username: String): UserRepositoryData?
    fun createUser(user: User): UUID?
}

data class UserRepositoryData(
    val id: UUID,
    val username: String,
    val password: String,
    val creationDate: LocalDate
)