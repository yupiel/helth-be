package de.yupiel.helth.challenge.application

import de.yupiel.helth.challenge.model.ChallengeRepository
import de.yupiel.helth.activity.model.Activity
import de.yupiel.helth.challenge.model.Challenge
import de.yupiel.helth.common.NotFoundException
import de.yupiel.helth.common.RequestBodyException
import de.yupiel.helth.common.RequestParameterException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.IllegalArgumentException

@Service
class ChallengeService(
    @Autowired private val challengeRepository: ChallengeRepository
) {
    fun findByID(challengeIDParam: String): Challenge {
        try {
            val challengeID = UUID.fromString(challengeIDParam)
            val challenge = this.challengeRepository.findById(challengeID)

            if (!challenge.isEmpty)
                return challenge.get()
            else
                throw NotFoundException("No challenge with id '$challengeIDParam' found")
        } catch (exception: IllegalArgumentException) {
            throw RequestParameterException("Request ID parameter was malformed")
        }
    }

    fun findAll(): List<Challenge> {
        val result = this.challengeRepository.findAll().toList()
        if (result.isEmpty()) throw NotFoundException("No Challenges found")
        else return result
    }

    fun findAllForUserID(userID: UUID): List<Challenge> {
        this.updateApplicableChallengeStatus(userID)

        val result = this.challengeRepository.findAllByUserID(userID)
        if (result.isEmpty()) throw NotFoundException("No Challenges found user with id '$userID'")
        else return result
    }

    fun findBetweenDates(
        userID: UUID,
        startDateParam: String,
        endDateParam: String
    ): List<Challenge> {
        try {
            this.updateApplicableChallengeStatus(userID)

            val startDate = LocalDate.parse(startDateParam)
            val endDate = LocalDate.parse(endDateParam)

            return this.challengeRepository.findBetweenDates(userID, startDate, endDate)
        } catch (exception: IllegalArgumentException) {
            throw RequestParameterException("One or more Parameter values were malformed and could not be parsed")
        }
    }

    fun findBetweenDatesWithStatus(
        userID: UUID,
        startDateParam: String,
        endDateParam: String,
        challengeStatusParam: String
    ): List<Challenge> {
        try {
            this.updateApplicableChallengeStatus(userID)

            val startDate = LocalDate.parse(startDateParam)
            val endDate = LocalDate.parse(endDateParam)
            val challengeStatus = Challenge.ChallengeStatus.valueOf(challengeStatusParam)

            return this.challengeRepository.findBetweenDatesWithStatus(userID, startDate, endDate, challengeStatus)
        } catch (exception: IllegalArgumentException) {
            throw RequestParameterException("One or more Parameter values were malformed and could not be parsed")
        }
    }

    fun save(
        userID: UUID,
        activityType: String,
        timesAWeek: Int,
        durationInWeeks: Int
    ): Challenge {
        try {
            val activityTypeFromText = Activity.ActivityType.valueOf(activityType)

            if(challengeOfGiveActivityTypeExistsForUser(userID, activityTypeFromText)) throw RequestBodyException("Only one challenge per Activity Type allowed")

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

    fun updateChallengeCounterForUserForType(userID: UUID, activityType: Activity.ActivityType) {
        val challengeToUpdate = this.challengeRepository.findChallengeByUserIDActivityTypeAndChallengeStatus(
            userID,
            activityType,
            Challenge.ChallengeStatus.IN_PROGRESS
        )

        if(challengeToUpdate.isEmpty()) return

        challengeToUpdate[0].timesAWeekCurrent++
        this.challengeRepository.save(challengeToUpdate[0])

        this.updateApplicableChallengeStatus(userID)
    }

    fun updateApplicableChallengeStatus(userID: UUID) {
        val activeChallenges = this.challengeRepository.findAllByUserID(userID) //TODO retrieve only in progress challenges

        activeChallenges.forEach {
            if(it.timesAWeekCurrent >= (it.timesAWeekGoal * ChronoUnit.WEEKS.between(it.startDate, it.expirationDate))){
                it.challengeStatus = Challenge.ChallengeStatus.SUCCEEDED
            }
            if(it.expirationDate < LocalDate.now()){
                it.challengeStatus = Challenge.ChallengeStatus.FAILED
            }
        }

        this.challengeRepository.saveAll(activeChallenges)
    }

    fun challengeOfGiveActivityTypeExistsForUser(userID: UUID, activityType: Activity.ActivityType): Boolean {
        val challenges = this.challengeRepository.findChallengeByUserIDActivityTypeAndChallengeStatus(
            userID,
            activityType,
            Challenge.ChallengeStatus.IN_PROGRESS
        )

        if(challenges.isEmpty()) return false
        return true
    }

    fun deleteChallengesFromUserID(userID: UUID) {
        this.challengeRepository.deleteByRelatedUserID(userID)
    }
}