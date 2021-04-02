package de.yupiel.helth.activity.application

import de.yupiel.helth.activity.application.ActivityService
import de.yupiel.helth.activity.model.ActivityRepository
import de.yupiel.helth.activity.model.Activity
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
    private lateinit var service: ActivityService

    @BeforeEach
    fun beforeEach(){
        service = ActivityService(repository)
    }

    @Test
    fun `showActivity should show existing activity`() {
        val savedActivity: Activity? =
            givenActivityExists("DRINK_WATER", UUID.fromString("c776e082-6407-49a5-a246-9d7265fc2583"))
        Assertions.assertNotNull(savedActivity)

        val actualActivity = service.findById(savedActivity!!.id)
        Assertions.assertEquals(savedActivity, actualActivity)
    }

    private fun givenActivityExists(textType: String, userID: UUID): Activity? {
        return service.save(userID, LocalDate.now(), textType)
    }

    @Test
    fun `showActivity should return null on missing activity`() {
        val actualActivity = service.findById(UUID.randomUUID())

        Assertions.assertNull(actualActivity)
    }
}