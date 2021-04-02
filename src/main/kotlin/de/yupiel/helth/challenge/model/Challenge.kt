package de.yupiel.helth.challenge.model

import de.yupiel.helth.activity.model.Activity
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
class Challenge(
    activityType: Activity.ActivityType,
    timesAWeek: Int,
    durationOfChallengeInWeeks: Int,
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

    @Column(name = "times_a_week_goal", nullable = false, unique = false)
    var timesAWeekGoal: Int
        private set
    @Column(name = "times_a_week_current", nullable = false, unique = false)
    var timesAWeekCurrent: Int = 0
        private set

    @Column(name = "start_date", nullable = false, unique = false)
    var startDate: LocalDate
        private set

    private var durationOfChallengeInWeeks: Long

    @Column(name = "expiration_date", nullable = false, unique = false)
    var expirationDate: LocalDate
        private set

    @Column(name = "challenge_status", nullable = false, unique = false, length = 12)
    @Enumerated(EnumType.STRING)
    var challengeStatus: ChallengeStatus
        private set

    @Column(name = "user_id", nullable = false, unique = false)
    var userID: UUID = userID
        private set

    init {
        this.id = UUID.randomUUID()
        this.activityType = activityType
        this.timesAWeekGoal = timesAWeek
        this.startDate = LocalDate.now()
        this.durationOfChallengeInWeeks = durationOfChallengeInWeeks.toLong()
        this.expirationDate = this.startDate.plusWeeks(this.durationOfChallengeInWeeks)
        this.challengeStatus = ChallengeStatus.IN_PROGRESS
    }

    enum class ChallengeStatus {
        IN_PROGRESS,
        SUCCEEDED,
        FAILED
    }
}