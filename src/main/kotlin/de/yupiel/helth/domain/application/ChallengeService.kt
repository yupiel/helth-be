package de.yupiel.helth.domain.application

import de.yupiel.helth.domain.integration.ChallengeRepository
import de.yupiel.helth.domain.model.Activity
import de.yupiel.helth.domain.model.Challenge
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

@Service
class ChallengeService(
    @Autowired private val challengeRepository: ChallengeRepository
) {
    fun findAll(): List<Challenge>? {
        return try {
            val result = this.challengeRepository.findAll().toList()
            if (result.isEmpty()) return null

            result
        } catch (exception: Exception) {
            null
        }
    }

    fun findAllForUserID(userID: UUID): List<Challenge>? {
        return try {
            val result = this.challengeRepository.findAllByUserID(userID)
            if (result.isEmpty()) return null

            result
        } catch (exception: Exception) {
            null
        }
    }

    fun save(
        userID: UUID,
        activityType: String,
        amountOfTimesADay: Int,
        startDate: String,
        expirationDate: String
    ): Challenge? {
        return try {
            val activityTypeFromText = Activity.ActivityType.valueOf(activityType)

            val newChallenge = Challenge(
                activityTypeFromText,
                amountOfTimesADay,
                LocalDate.parse(startDate),
                LocalDate.parse(expirationDate),
                userID
            )

            this.challengeRepository.save(newChallenge)
        } catch (exception: Exception) {
            null
        }
    }
}