package de.yupiel.helth.user.model

import de.yupiel.helth.user.model.User
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