package de.yupiel.helth.activity.application

import de.yupiel.helth.activity.model.ActivityRepository
import de.yupiel.helth.activity.model.Activity
import de.yupiel.helth.challenge.application.ChallengeService
import de.yupiel.helth.common.NotFoundException
import de.yupiel.helth.common.RequestBodyException
import de.yupiel.helth.common.RequestParameterException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.time.LocalDate
import java.util.*

@DataJpaTest
internal class ActivityServiceTest {
    @Autowired
    private lateinit var repository: ActivityRepository
    @Autowired
    private lateinit var challengeService: ChallengeService

    private lateinit var activityService: ActivityService

    private final val testUserID: UUID = UUID.fromString("c776e082-6407-49a5-a246-9d7265fc2583")

    @BeforeEach
    fun beforeEach() {
        activityService = ActivityService(repository, challengeService)

        givenActivityExists("DRINK_WATER", testUserID)
        givenActivityExists("DRINK_WATER", testUserID)
        givenActivityExists("RUNNING", testUserID)
        givenActivityExists("RUNNING", testUserID)
    }

    private fun givenActivityExists(textType: String, userID: UUID): Activity {
        return activityService.createActivity(userID, LocalDate.now(), textType)
    }

    @Test
    fun `findByID should find existing activity and returns it`() {
        val savedActivity: Activity? =
            givenActivityExists("DRINK_WATER", testUserID)
        Assertions.assertNotNull(savedActivity)

        val actualActivity = activityService.findById(savedActivity!!.id)
        Assertions.assertEquals(savedActivity, actualActivity)
    }

    @Test
    fun `findByID does not find activity and throws NotFoundException`() {
        Assertions.assertThrows(NotFoundException::class.java) {
            activityService.findById(UUID.randomUUID())
        }
    }

    @Test
    fun `findBetweenDates finds existing activities between supplied dates and return them`() {
        val activities = activityService.findBetweenDates(
            testUserID,
            LocalDate.now().minusDays(2),
            LocalDate.now().plusDays(1)
        )

        Assertions.assertTrue(activities.size == 4)
    }

    @Test
    fun `findBetweenDates does not find activities when the startDate is further in the future than the endDate and throws RequestParameterException`() {
        Assertions.assertThrows(RequestParameterException::class.java) {
            activityService.findBetweenDates(
                testUserID,
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(1)
            )
        }
    }

    @Test
    fun `findBetweenDates does not find activities between supplied dates and throws NotFoundException`() {
        Assertions.assertThrows(NotFoundException::class.java) {
            activityService.findBetweenDates(
                testUserID,
                LocalDate.now().plusYears(2),
                LocalDate.now().plusYears(5)
            )
        }
    }

    @Test
    fun `findBetweenDatesWithType finds existing activities between supplied dates and return them`() {
        val activities = activityService.findBetweenDatesWithType(
            testUserID,
            LocalDate.now().minusDays(2),
            LocalDate.now().plusDays(1),
            "DRINK_WATER"
        )

        Assertions.assertTrue(activities.size == 2)
    }

    @Test
    fun `findBetweenDatesWithType does not find activities when the startDate is further in the future than the endDate and throws RequestParameterException`() {
        Assertions.assertThrows(RequestParameterException::class.java) {
            activityService.findBetweenDatesWithType(
                testUserID,
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(1),
                "DRINK_WATER"
            )
        }
    }

    @Test
    fun `findBetweenDatesWithType does not find activities between supplied dates and throws NotFoundException`() {
        Assertions.assertThrows(NotFoundException::class.java) {
            activityService.findBetweenDatesWithType(
                testUserID,
                LocalDate.now().plusYears(2),
                LocalDate.now().plusYears(5),
                "DRINK_WATER"
            )
        }
    }

    @Test
    fun `findBetweenDatesWithType does not find activities of specified type between supplied dates and throws NotFoundException`() {
        Assertions.assertThrows(NotFoundException::class.java) {
            activityService.findBetweenDatesWithType(
                testUserID,
                LocalDate.now().minusDays(2),
                LocalDate.now().plusDays(2),
                "WALKING"
            )
        }
    }

    @Test
    fun `createActivity successfully creates an Activity and return it`() {
        val activity = activityService.createActivity(
            testUserID,
            LocalDate.now(),
            "WALKING"
        )

        Assertions.assertTrue(activity.userID == testUserID)
    }

    @Test
    fun `createActivity can't resolve activityType String and throws RequestBodyException`() {
        Assertions.assertThrows(RequestBodyException::class.java) {
            activityService.createActivity(
                testUserID,
                LocalDate.now(),
                "EAT_BURGER"
            )
        }
    }

}