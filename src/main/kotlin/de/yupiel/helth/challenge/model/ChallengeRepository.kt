package de.yupiel.helth.challenge.model

import de.yupiel.helth.challenge.model.Challenge
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ChallengeRepository : CrudRepository<Challenge, UUID> {
    @Query(
        """
        SELECT c FROM Challenge c
        WHERE c.userID = :userID
    """
    )
    fun findAllByUserID(@Param("userID") userID: UUID): List<Challenge>
}