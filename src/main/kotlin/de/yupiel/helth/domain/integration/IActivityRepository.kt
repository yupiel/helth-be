package de.yupiel.helth.domain.integration

import de.yupiel.helth.domain.model.Activity
import java.time.LocalDate
import java.util.*

interface IActivityRepository {
    fun findById(id: UUID): ActivityRepositoryData?
    fun findBetweenDates(
        userID: UUID,
        startDate: LocalDate,
        endDate: LocalDate
    ) : MutableList<ActivityRepositoryData>?
    fun findBetweenDatesWithType(
        userID: UUID,
        startDate: LocalDate,
        endDate: LocalDate,
        activityType: Activity.ActivityType
    ) : MutableList<ActivityRepositoryData>?

    fun saveActivity(activity: Activity, userID: UUID): UUID?
}

data class ActivityRepositoryData(
    val id: UUID,
    val type: Activity.ActivityType,
    val creationDate: LocalDate
)