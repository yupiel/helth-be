package de.yupiel.helth.integration.web

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import de.yupiel.helth.authentication.AuthenticationService
import de.yupiel.helth.domain.application.ChallengeService
import de.yupiel.helth.domain.model.Challenge
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/users/challenges")
class ChallengeController(
    @Autowired private val challengeService: ChallengeService,
    @Autowired private val authenticationService: AuthenticationService
) {
    @GetMapping
    fun showAll(
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestParam(required = false, value = "userID") userIDSearch: Boolean = false
    ): String {
        return try {
            val userID = authenticationService.extractUserIDFromAuthorizationHeader(authorizationHeader)
                ?: return "Not Authorized"

            val challenges = if (userIDSearch)
                this.challengeService.findAllForUserID(userID) ?: return "No Challenges found for user ID $userID"
            else
                this.challengeService.findAll() ?: return "No Challenges found... is the database ok?"

            challengeListToJsonArray(challenges).toJsonString()
        } catch (exception: NullPointerException) {
            "None Found"
        }
    }

    @PostMapping
    fun saveActivity(
        @RequestBody request: ChallengeCreationRequest
    ): String {
        val jwtTokenPayload =
            this.authenticationService.checkJWTTokenValidAndReturnPayload(request.token) ?: return "Not Authorized"

        val challenge =
            this.challengeService.save(
                UUID.fromString(jwtTokenPayload["user_id"] as String),
                request.activityTypeText,
                request.amountOfTimesADay,
                request.startDate,
                request.expirationDate
            ) ?: return "There was an error saving the Challenge"

        return challengeToJsonObject(challenge).toJsonString()
    }

    //TODO Change to DTO with from(Model) and from(List<Model>) methods
    fun challengeToJsonObject(challenge: Challenge): JsonObject {
        return JsonObject(
            mapOf(
                "id" to challenge.id.toString(),
                "activityType" to challenge.activityType.toString(),
                "amountOfTimeADay" to challenge.amountOfTimesADay.toString(),
                "startDate" to challenge.startDate.toString(),
                "endDate" to challenge.expirationDate.toString(),
                "challengeStatus" to challenge.challengeStatus.toString()
            )
        )
    }
    fun challengeListToJsonArray(challenges: List<Challenge>): JsonArray<JsonObject> {
        val resultValue: JsonArray<JsonObject> = JsonArray()

        challenges.forEach {
            resultValue.add(challengeToJsonObject(it))
        }

        return resultValue
    }

    data class ChallengeCreationRequest(
        val token: String,
        val activityTypeText: String,
        val amountOfTimesADay: Int,
        val startDate: String,
        val expirationDate: String
    )
}