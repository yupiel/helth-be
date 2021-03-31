package de.yupiel.helth.domain.model

import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
class Challenge(
    activityType: Activity.ActivityType,
    amountOfTimesADay: Int,
    startDate: LocalDate,
    expirationDate: LocalDate,
    userID: UUID
) {
    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    var id: UUID
        private set

    @Column(name = "activity_type", nullable = false, unique = false, length = 15)
    @Enumerated(EnumType.STRING)
    var activityType: Activity.ActivityType
        private set

    @Column(name = "amount_of_times_a_day", nullable = false, unique = false)
    var amountOfTimesADay: Int
        private set

    @Column(name = "start_date", nullable = false, unique = false)
    var startDate: LocalDate
        private set

    @Column(name = "expiration_date", nullable = false, unique = false)
    var expirationDate: LocalDate
        private set

    @Column(name = "challenge_type", nullable = false, unique = false, length = 12)
    @Enumerated(EnumType.STRING)
    var challengeStatus: ChallengeStatus
        private set

    @Column(name = "user_id", nullable = false, unique = false)
    var userID: UUID = userID
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
        challengeStatus: ChallengeStatus,
        userID: UUID
    ) : this(activityType, amountOfTimesADay, startDate, expirationDate, userID) {
        this.id = id
        this.challengeStatus = challengeStatus
    }

    enum class ChallengeStatus {
        IN_PROGRESS,
        SUCCEEDED,
        FAILED
    }
}