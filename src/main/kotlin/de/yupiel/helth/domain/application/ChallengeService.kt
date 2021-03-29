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
    @Autowired private val challengeRepository: IChallengeRepository,
    @Autowired private val authenticationService: AuthenticationService
) {
    fun saveChallenge(
        jwtToken: String,
        activityType: String,
        amountOfTimesADay: Int,
        startDate: String,
        expirationDate: String
    ): UUID? {
        return try {
            val jwtTokenPayload = this.authenticationService.checkJWTTokenValidAndReturnPayload(jwtToken) ?: return null
            val activityTypeFromText = Activity.ActivityType.from(activityType) ?: return null

            val newChallenge = Challenge(
                activityTypeFromText,
                amountOfTimesADay,
                LocalDate.parse(startDate),
                LocalDate.parse(expirationDate)
            )

            this.challengeRepository.saveChallenge(
                newChallenge,
                UUID.fromString(jwtTokenPayload["user_id"] as String)
            )
        } catch (dateTimeParseException: DateTimeParseException) {
            null
        }
    }
}