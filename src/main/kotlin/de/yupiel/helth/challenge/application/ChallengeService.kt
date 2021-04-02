package de.yupiel.helth.challenge.application

import de.yupiel.helth.challenge.model.ChallengeRepository
import de.yupiel.helth.activity.model.Activity
import de.yupiel.helth.challenge.model.Challenge
import de.yupiel.helth.common.NotFoundException
import de.yupiel.helth.common.RequestBodyException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.time.format.DateTimeParseException
import java.util.*

@Service
class ChallengeService(
    @Autowired private val challengeRepository: ChallengeRepository
) {
    fun findAll(): List<Challenge> {
        val result = this.challengeRepository.findAll().toList()
        if (result.isEmpty()) throw NotFoundException("No Challenges found")
        else return result
    }

    fun findAllForUserID(userID: UUID): List<Challenge> {
        val result = this.challengeRepository.findAllByUserID(userID)
        if (result.isEmpty()) throw NotFoundException("No Challenges found user with id '$userID'")
        else return result
    }

    fun save(
        userID: UUID,
        activityType: String,
        timesAWeek: Int,
        durationInWeeks: Int
    ): Challenge {
        try {
            val activityTypeFromText = Activity.ActivityType.valueOf(activityType)

            val newChallenge = Challenge(
                activityTypeFromText,
                timesAWeek,
                durationInWeeks,
                userID
            )

            return this.challengeRepository.save(newChallenge)
        } catch (exception: IllegalArgumentException) {
            throw RequestBodyException("Request parameter textType malformed")
        } catch (exception: DateTimeParseException) {
            throw RequestBodyException("Request parameter startDate or expirationDate malformed")
        }
    }
}