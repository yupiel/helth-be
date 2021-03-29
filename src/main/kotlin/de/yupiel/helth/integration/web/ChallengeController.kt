package de.yupiel.helth.integration.web

import de.yupiel.helth.domain.application.ChallengeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/users/{username}/challenges")
class ChallengeController(@Autowired private val challengeService: ChallengeService) {
    @PostMapping
    fun saveActivity(
        @RequestBody request: ChallengeCreationRequest,
        @PathVariable("username") username: String
    ): String {
        val returnedUUID: UUID? =
            this.challengeService.saveChallenge(
                request.token,
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