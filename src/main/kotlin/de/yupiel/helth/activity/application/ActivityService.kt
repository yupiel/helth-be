package de.yupiel.helth.activity.application

import de.yupiel.helth.activity.model.ActivityRepository
import de.yupiel.helth.activity.model.Activity
import de.yupiel.helth.common.NotFoundException
import de.yupiel.helth.common.RequestBodyException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*
import kotlin.IllegalArgumentException

@Service
class ActivityService(
    @Autowired private val activityRepository: ActivityRepository
) {
    fun findById(id: UUID): Activity {
        val activity = this.activityRepository.findById(id)

        if (!activity.isEmpty)
            return activity.get()
        else
            throw NotFoundException("No activity with id '$id' found")
    }

    fun findBetweenDates(
        userID: UUID,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<Activity> {
        val result = this.activityRepository.findBetweenDates(userID, startDate, endDate)
        if (result.isEmpty()) throw NotFoundException("No activities found between the dates of '$startDate' and '$endDate' for user with id '$userID'")
        else return result
    }

    fun findBetweenDatesWithType(
        userID: UUID,
        startDate: LocalDate,
        endDate: LocalDate,
        activityType: Activity.ActivityType
    ): List<Activity> {
        val result = this.activityRepository.findBetweenDatesWithType(userID, startDate, endDate, activityType)
        if (result.isEmpty()) throw NotFoundException("No activities of type '$activityType' found between the dates of '$startDate' and '$endDate' for user with id '$userID'")
        else return result
    }

    fun save(userID: UUID, creationDate: LocalDate, textType: String): Activity {
        try {
            val activityType = Activity.ActivityType.valueOf(textType)
            val newActivity = Activity(activityType, creationDate, userID)

            return this.activityRepository.save(newActivity)
        } catch (exception: IllegalArgumentException) {
            throw RequestBodyException("Parameter textType does not equate to an Activity Type")
        }
    }
}