package de.yupiel.helth.domain.integration

import de.yupiel.helth.domain.model.Challenge
import java.time.LocalDate
import java.util.*

interface IChallengeRepository {
    fun saveChallenge(challenge: Challenge, userID: UUID) : UUID?
}

data class ChallengeRepositoryData(
    val id: UUID,
    val activityType: String,
    val amountOfTimesADay: Int,
    val startDate: LocalDate,
    val expirationDate: LocalDate,
    val challengeStatus: String
)