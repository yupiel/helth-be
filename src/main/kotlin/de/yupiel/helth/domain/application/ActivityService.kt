package de.yupiel.helth.domain.application

import de.yupiel.helth.domain.integration.IActivityRepository
import de.yupiel.helth.domain.model.Activity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

@Service
class ActivityService(@Autowired private val activityRepository: IActivityRepository) {
    fun showActivity(id: UUID): Activity? {
        val retrievedActivityData = this.activityRepository.findById(id)
        return if (retrievedActivityData == null)
            null
        else
            Activity(
                retrievedActivityData.id,
                retrievedActivityData.type,
                retrievedActivityData.creationDate
            )
    }

    fun saveActivity(textType: String, creationDate: LocalDate, userID: UUID): UUID? {
        val activityType = Activity.ActivityType.from(textType) ?: return null
        val newActivity = Activity(activityType, creationDate)
        return this.activityRepository.saveActivity(newActivity, userID)
    }
}