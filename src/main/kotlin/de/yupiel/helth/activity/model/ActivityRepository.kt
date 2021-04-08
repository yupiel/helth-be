package de.yupiel.helth.activity.model

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.*

@Repository
interface ActivityRepository : CrudRepository<Activity, UUID> {
    @Query(
        """
        SELECT a FROM Activity a 
        WHERE a.userID = :userID
        AND a.creationDate >= :startDate
        AND a.creationDate <= :endDate
    """
    )
    fun findBetweenDates(
        @Param("userID") userID: UUID,
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): List<Activity>

    @Query(
        """
        SELECT a FROM Activity a 
        WHERE a.userID = :userID
        AND a.creationDate >= :startDate
        AND a.creationDate <= :endDate
        AND a.type = :type
    """
    )
    fun findBetweenDatesWithType(
        @Param("userID") userID: UUID,
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate,
        @Param("type") activityType: Activity.ActivityType
    ): List<Activity>

    @Transactional
    @Modifying
    @Query(
        """
            DELETE FROM Activity a
            WHERE a.userID = :userID
        """
    )
    fun deleteByRelatedUserID(@Param("userID") userID: UUID)
}