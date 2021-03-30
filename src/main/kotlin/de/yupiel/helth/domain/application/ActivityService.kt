package de.yupiel.helth.domain.application

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import de.yupiel.helth.domain.integration.IActivityRepository
import de.yupiel.helth.domain.model.Activity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.Exception
import java.time.LocalDate
import java.util.*
import kotlin.IllegalArgumentException

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

    fun showActivitiesBetweenDates(
        userID: UUID,
        startDate: LocalDate,
        endDate: LocalDate
    ): JsonArray<JsonObject>? {
        return try {
            val result =
                this.activityRepository.findBetweenDates(userID, startDate, endDate) ?: return null

            val returnValue: JsonArray<JsonObject> = JsonArray<JsonObject>()
            result.forEach {
                returnValue.add(
                    JsonObject(
                        mapOf(
                            "id" to it.id.toString(),
                            "type" to it.type.toString(),
                            "creationDate" to it.creationDate.toString()
                        )
                    )
                )
            }

            returnValue
        } catch (exception: Exception) {
            null
        }
    }

    fun showActivitiesBetweenDatesWithType(
        userID: UUID,
        startDate: LocalDate,
        endDate: LocalDate,
        activityType: Activity.ActivityType
    ): JsonArray<JsonObject>? {
        return try {
            val result =
                this.activityRepository.findBetweenDatesWithType(userID, startDate, endDate, activityType) ?: return null

            val returnValue: JsonArray<JsonObject> = JsonArray<JsonObject>()
            result.forEach {
                returnValue.add(
                    JsonObject(
                        mapOf(
                            "id" to it.id.toString(),
                            "type" to it.type.toString(),
                            "creationDate" to it.creationDate.toString()
                        )
                    )
                )
            }

            returnValue
        } catch (exception: Exception) {
            null
        }
    }

    fun saveActivity(userID: UUID, creationDate: LocalDate, textType: String): UUID? {
        return try {
            val activityType = Activity.ActivityType.valueOf(textType)
            val newActivity = Activity(activityType, creationDate)
            return this.activityRepository.saveActivity(newActivity, userID)
        } catch (exception: IllegalArgumentException) {
            null
        }
    }
}