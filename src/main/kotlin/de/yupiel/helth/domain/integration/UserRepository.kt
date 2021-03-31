package de.yupiel.helth.domain.integration

import de.yupiel.helth.domain.model.Activity
import de.yupiel.helth.domain.model.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository: CrudRepository<User, UUID> {
    @Query(
        """
            SELECT u FROM User u
            WHERE u.username = :username
        """
    )
    fun findByUsername(@Param("username") username: String): List<User>
}