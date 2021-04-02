package de.yupiel.helth.challenge.integration

import de.yupiel.helth.authentication.application.AuthenticationService
import de.yupiel.helth.challenge.application.ChallengeService
import de.yupiel.helth.challenge.model.Challenge
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/challenges")
class ChallengeController(
    @Autowired private val challengeService: ChallengeService,
    @Autowired private val authenticationService: AuthenticationService
) {
    @GetMapping
    fun showAll(
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestParam(required = false, value = "userID") userIDSearch: Boolean = false
    ): List<ChallengeDto> {
        val userID = authenticationService.extractUserIDFromAuthorizationHeader(authorizationHeader)

        val challenges = if (userIDSearch)
            this.challengeService.findAllForUserID(userID)
        else
            this.challengeService.findAll()

        return challenges.map {
            ChallengeDto.from(it)
        }
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun saveActivity(
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestBody request: ChallengeCreationRequest
    ): ChallengeDto {
        val userID =
            this.authenticationService.extractUserIDFromAuthorizationHeader(authorizationHeader)

        val challenge =
            this.challengeService.save(
                userID,
                request.activityTypeText,
                request.timesAWeekGoal,
                request.weeksDuration
            )

        return ChallengeDto.from(challenge)
    }

    data class ChallengeCreationRequest(
        val activityTypeText: String,
        val timesAWeekGoal: Int,
        val weeksDuration: Int
    )

    data class ChallengeDto(
        val id: String,
        val activityType: String,
        val timesAWeekGoal: String,
        val timesAWeekCurrent: String,
        val startDate: String,
        val expirationDate: String,
        val challengeStatus: String,
        val userID: String
    ) {
        companion object {
            fun from(challenge: Challenge): ChallengeDto {
                return ChallengeDto(
                    challenge.id.toString(),
                    challenge.activityType.toString(),
                    challenge.timesAWeekGoal.toString(),
                    challenge.timesAWeekCurrent.toString(),
                    challenge.startDate.toString(),
                    challenge.expirationDate.toString(),
                    challenge.challengeStatus.toString(),
                    challenge.userID.toString()
                )
            }
        }
    }
}