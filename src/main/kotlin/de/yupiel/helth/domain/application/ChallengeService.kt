package de.yupiel.helth.domain.application

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
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
    fun showAll(): JsonArray<JsonObject>? {
        return try {
            val result = this.challengeRepository.findAll() ?: return null

            val returnValue: JsonArray<JsonObject> = JsonArray()
            result.forEach {
                returnValue.add(
                    JsonObject(
                        mapOf(
                            "id" to it.id.toString(),
                            "activityType" to it.activityType.toString(),
                            "amountOfTimeADay" to it.amountOfTimesADay.toString(),
                            "startDate" to it.startDate.toString(),
                            "endDate" to it.expirationDate.toString(),
                            "challengeStatus" to it.challengeStatus.toString()
                        )
                    )
                )
            }
            returnValue
        } catch (exception: Exception) {
            null
        }
    }

    fun showAll(userID: UUID): JsonArray<JsonObject>? {
        return try {
            val result = this.challengeRepository.findAll(userID) ?: return null

            val returnValue: JsonArray<JsonObject> = JsonArray()
            result.forEach {
                returnValue.add(
                    JsonObject(
                        mapOf(
                            "id" to it.id.toString(),
                            "activityType" to it.activityType.toString(),
                            "amountOfTimeADay" to it.amountOfTimesADay.toString(),
                            "startDate" to it.startDate.toString(),
                            "endDate" to it.expirationDate.toString(),
                            "challengeStatus" to it.challengeStatus.toString()
                        )
                    )
                )
            }
            returnValue
        } catch (exception: Exception) {
            null
        }
    }

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