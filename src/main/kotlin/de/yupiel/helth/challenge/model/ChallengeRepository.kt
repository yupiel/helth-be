package de.yupiel.helth.challenge.model

import de.yupiel.helth.activity.model.Activity
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
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

    @Query(
        """
            SELECT c FROM Challenge c
            WHERE c.userID = :userID
            AND c.startDate >= :startDate
            AND c.expirationDate <= :endDate 
        """
    )
    fun findBetweenDates(
        @Param("userID") userID: UUID,
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ) : List<Challenge>

    @Query(
        """
            SELECT c FROM Challenge c
            WHERE c.userID = :userID
            AND c.startDate >= :startDate
            AND c.expirationDate <= :endDate
            AND c.challengeStatus = :challengeStatus
        """
    )
    fun findBetweenDatesWithStatus(
        @Param("userID") userID: UUID,
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate,
        @Param("challengeStatus") challengeStatus: Challenge.ChallengeStatus
    ) : List<Challenge>

    @Query(
        """
            SELECT c FROM Challenge c
            WHERE c.userID = :userID
            AND c.activityType = :activityType
            AND c.challengeStatus = :challengeStatus
        """
    )
    fun findChallengeByUserIDActivityTypeAndChallengeStatus(
        @Param("userID") userID: UUID,
        @Param("activityType") activityType: Activity.ActivityType,
        @Param("challengeStatus") challengeStatus: Challenge.ChallengeStatus
    ): List<Challenge>

    @Transactional
    @Modifying
    @Query(
        """
            DELETE FROM Challenge c
            WHERE c.userID = :userID
        """
    )
    fun deleteByRelatedUserID(@Param("userID") userID: UUID)
}