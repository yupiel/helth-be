package de.yupiel.helth.domain.model

import java.time.LocalDate
import java.util.*

class Challenge(
    activityType: Activity.ActivityType,
    amountOfTimesADay: Int,
    startDate: LocalDate,
    expirationDate: LocalDate
) {
    var id: UUID
        private set
    var activityType: Activity.ActivityType
        private set
    var amountOfTimesADay: Int
        private set
    var startDate: LocalDate
        private set
    var expirationDate: LocalDate
        private set
    var challengeStatus: ChallengeStatus
        private set

    init {
        this.id = UUID.randomUUID()
        this.activityType = activityType
        this.amountOfTimesADay = amountOfTimesADay
        this.startDate = startDate
        this.expirationDate = expirationDate
        this.challengeStatus = ChallengeStatus.IN_PROGRESS
    }

    constructor(
        id: UUID,
        activityType: Activity.ActivityType,
        amountOfTimesADay: Int,
        startDate: LocalDate,
        expirationDate: LocalDate,
        challengeStatus: ChallengeStatus
    ) : this(activityType, amountOfTimesADay, startDate, expirationDate) {
        this.id = id
        this.challengeStatus = challengeStatus
    }

    enum class ChallengeStatus {
        IN_PROGRESS,
        SUCCEEDED,
        FAILED
    }
}