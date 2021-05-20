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
    userID: UUID,
    challengeStatus: ChallengeStatus = ChallengeStatus.IN_PROGRESS
) {
    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    val id: UUID = UUID.randomUUID()

    @Column(name = "activity_type", nullable = false, unique = false, length = 15)
    @Enumerated(EnumType.STRING)
    val activityType: Activity.ActivityType = activityType

    @Column(name = "times_a_week_goal", nullable = false, unique = false)
    val timesAWeekGoal: Int = timesAWeek

    @Column(name = "times_a_week_current", nullable = false, unique = false)
    var timesAWeekCurrent: Int = 0

    @Column(name = "start_date", nullable = false, unique = false)
    val startDate: LocalDate = LocalDate.now()

    private var durationOfChallengeInWeeks: Long = durationOfChallengeInWeeks.toLong()

    @Column(name = "expiration_date", nullable = false, unique = false)
    val expirationDate: LocalDate = this.startDate.plusWeeks(this.durationOfChallengeInWeeks)

    @Column(name = "challenge_status", nullable = false, unique = false, length = 12)
    @Enumerated(EnumType.STRING)
    var challengeStatus: ChallengeStatus = challengeStatus

    @Column(name = "user_id", nullable = false, unique = false)
    val userID: UUID = userID

    enum class ChallengeStatus {
        IN_PROGRESS,
        SUCCEEDED,
        FAILED
    }
}