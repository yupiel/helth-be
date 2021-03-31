package de.yupiel.helth.domain.application

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import de.yupiel.helth.domain.integration.ActivityRepository
import de.yupiel.helth.domain.model.Activity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.Exception
import java.time.LocalDate
import java.util.*
import kotlin.IllegalArgumentException

@Service
class ActivityService(
    @Autowired private val activityRepository: ActivityRepository
) {
    fun findById(id: UUID): Activity? {
        val activity = this.activityRepository.findById(id)

        return if (!activity.isEmpty)
            activity.get()
        else
            null
    }

    fun findBetweenDates(
        userID: UUID,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<Activity>? {
        return try {
            val result = this.activityRepository.findBetweenDates(userID, startDate, endDate)
            if (result.isEmpty()) return null

            result
        } catch (exception: Exception) {
            null
        }
    }

    fun findBetweenDatesWithType(
        userID: UUID,
        startDate: LocalDate,
        endDate: LocalDate,
        activityType: Activity.ActivityType
    ): List<Activity>? {
        return try {
            val result = this.activityRepository.findBetweenDatesWithType(userID, startDate, endDate, activityType)
            if (result.isEmpty()) return null

            result
        } catch (exception: Exception) {
            null
        }
    }

    fun save(userID: UUID, creationDate: LocalDate, textType: String): Activity? {
        return try {
            val activityType = Activity.ActivityType.valueOf(textType)
            val newActivity = Activity(activityType, creationDate, userID)

            this.activityRepository.save(newActivity)
        } catch (exception: IllegalArgumentException) {
            null
        }
    }
}