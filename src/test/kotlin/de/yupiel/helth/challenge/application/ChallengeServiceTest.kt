package de.yupiel.helth.challenge.application

import de.yupiel.helth.activity.model.Activity
import de.yupiel.helth.challenge.model.ChallengeRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.time.LocalDate
import java.util.*

@DataJpaTest
class ChallengeServiceTest {
    @Autowired
    private lateinit var repository: ChallengeRepository
    private lateinit var service: ChallengeService

    @BeforeEach
    fun populate() {
        service = ChallengeService(repository)

        service.save(
            UUID.fromString("b1805241-3365-455f-8ba9-6228458c55b0"),
            "DRINK_WATER",
            6,
            1
        )
        service.save(
            UUID.fromString("b1805241-3365-455f-8ba9-6228458c55b0"),
            "RUNNING",
            2,
            2
        )
        service.save(
            UUID.fromString("b1805241-3365-455f-8ba9-6228458c55b0"),
            "WALKING",
            2,
            3
        )
    }

    @Test
    fun `updateChallengeCounterForUserForType correctly updates current counter for challenge should return true`() {
        this.service.updateChallengeCounterForUserForType(
            UUID.fromString("b1805241-3365-455f-8ba9-6228458c55b0"),
            Activity.ActivityType.DRINK_WATER
        )

        val resultList = this.service.findAllForUserID(UUID.fromString("b1805241-3365-455f-8ba9-6228458c55b0"))
        val actual = resultList.first {it.activityType == Activity.ActivityType.DRINK_WATER}

        Assertions.assertTrue(actual.timesAWeekCurrent == 1)
    }
}