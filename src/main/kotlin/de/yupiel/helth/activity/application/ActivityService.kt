package de.yupiel.helth.activity.application

import de.yupiel.helth.activity.model.ActivityRepository
import de.yupiel.helth.activity.model.Activity
import de.yupiel.helth.challenge.application.ChallengeService
import de.yupiel.helth.common.NotFoundException
import de.yupiel.helth.common.RequestBodyException
import de.yupiel.helth.common.RequestParameterException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*
import kotlin.IllegalArgumentException

@Service
class ActivityService(
    @Autowired private val activityRepository: ActivityRepository,
    @Autowired private val challengeService: ChallengeService
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
        if(startDate > endDate) throw RequestParameterException("endDate '$endDate' can't have a date before startDate '$startDate")

        val result = this.activityRepository.findBetweenDates(userID, startDate, endDate)
        if (result.isEmpty()) throw NotFoundException("No activities found between the dates of '$startDate' and '$endDate' for user with id '$userID'")
        else return result
    }

    fun findBetweenDatesWithType(
        userID: UUID,
        startDate: LocalDate,
        endDate: LocalDate,
        activityTypeText: String
    ): List<Activity> {
        try {
            if (startDate > endDate) throw RequestParameterException("endDate '$endDate' can't have a date before startDate '$startDate")
            val activityType = Activity.ActivityType.valueOf(activityTypeText)

            val result = this.activityRepository.findBetweenDatesWithType(userID, startDate, endDate, activityType)
            if (result.isEmpty()) throw NotFoundException("No activities of type '$activityType' found between the dates of '$startDate' and '$endDate' for user with id '$userID'")
            else return result
        } catch(exception: IllegalArgumentException){
            throw RequestParameterException("Activity type text was malformed")
        }
    }

    fun createActivity(userID: UUID, creationDate: LocalDate, textType: String): Activity {
        try {
            val activityType = Activity.ActivityType.valueOf(textType)
            val newActivity = Activity(activityType, creationDate, userID)

            val savedActivity = this.activityRepository.save(newActivity)
            this.challengeService.updateChallengeCounterForUserForType(userID, activityType)

            return savedActivity
        } catch (exception: IllegalArgumentException) {
            throw RequestBodyException("Parameter textType does not equate to an Activity Type")
        }
    }

    fun deleteActivitiesFromUserID(userID: UUID) {
        this.activityRepository.deleteByRelatedUserID(userID)
    }
}