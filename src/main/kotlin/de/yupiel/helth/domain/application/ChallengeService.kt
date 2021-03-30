package de.yupiel.helth.domain.application

import de.yupiel.helth.authentication.AuthenticationService
import de.yupiel.helth.domain.integration.IChallengeRepository
import de.yupiel.helth.domain.model.Activity
import de.yupiel.helth.domain.model.Challenge
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeParseException
import java.util.*

@Service
class ChallengeService(
    @Autowired private val challengeRepository: IChallengeRepository
) {
    fun saveChallenge(
        userID: UUID,
        activityType: String,
        amountOfTimesADay: Int,
        startDate: String,
        expirationDate: String
    ): UUID? {
        return try {
            val activityTypeFromText = Activity.ActivityType.valueOf(activityType)

            val newChallenge = Challenge(
                activityTypeFromText,
                amountOfTimesADay,
                LocalDate.parse(startDate),
                LocalDate.parse(expirationDate)
            )

            this.challengeRepository.saveChallenge(
                newChallenge,
                userID
            )
        } catch (exception: Exception) {
            null
        }
    }
}