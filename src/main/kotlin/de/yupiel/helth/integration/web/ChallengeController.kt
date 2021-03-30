package de.yupiel.helth.integration.web

import de.yupiel.helth.authentication.AuthenticationService
import de.yupiel.helth.domain.application.ChallengeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/users/{username}/challenges")
class ChallengeController(
    @Autowired private val challengeService: ChallengeService,
    @Autowired private val authenticationService: AuthenticationService
) {
    @GetMapping
    fun showAll(
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestParam(required = false, value = "userID") userID: Boolean = false
    ): String {
        return try {
            val authHeaderParts = authorizationHeader.split(" ")
            if (authHeaderParts[0] != "Bearer")
                return "Wrong Authorization Type"
            if (authHeaderParts.size < 2)
                return "No Token found in Authorization Header"

            val jwtTokenPayload =
                this.authenticationService.checkJWTTokenValidAndReturnPayload(authHeaderParts[1])
                    ?: return "Not Authorized"

            if (userID)
                return this.challengeService.showAll(UUID.fromString(jwtTokenPayload["user_id"] as String))!!.toJsonString()
            else
                this.challengeService.showAll()!!.toJsonString()
        } catch (exception: NullPointerException) {
            "None Found"
        }
    }

    @PostMapping
    fun saveActivity(
        @RequestBody request: ChallengeCreationRequest,
        @PathVariable("username") username: String
    ): String {
        val jwtTokenPayload =
            this.authenticationService.checkJWTTokenValidAndReturnPayload(request.token) ?: return "Not Authorized"

        val returnedUUID: UUID? =
            this.challengeService.saveChallenge(
                UUID.fromString(jwtTokenPayload["user_id"] as String),
                request.activityTypeText,
                request.amountOfTimesADay,
                request.startDate,
                request.expirationDate
            )

        return returnedUUID?.toString() ?: "There was an error saving the Challenge"
    }

    data class ChallengeCreationRequest(
        val token: String,
        val activityTypeText: String,
        val amountOfTimesADay: Int,
        val startDate: String,
        val expirationDate: String
    )
}