package de.yupiel.helth.domain.integration

import de.yupiel.helth.domain.model.Activity
import java.time.LocalDate
import java.util.*

interface IActivityRepository {
    fun findById(id: UUID): ActivityRepositoryData?
    //fun findAllByUserId(userID: UUID) : MutableList<LocalActivityRepository.ActivityRepositoryData>?
    fun saveActivity(activity: Activity, userID: UUID): UUID?
}

data class ActivityRepositoryData(
    val id: UUID,
    val type: Activity.ActivityType,
    val creationDate: LocalDate
)