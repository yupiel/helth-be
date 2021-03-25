package de.yupiel.helth.domain.application

import de.yupiel.helth.domain.model.Activity
import de.yupiel.helth.persistance.local.LocalActivityRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.*

internal class ActivityServiceTest{
    private lateinit var service: ActivityService
    private lateinit var repository: LocalActivityRepository

    @BeforeEach
    fun before(){
        repository = LocalActivityRepository()
        service = ActivityService(repository)
    }

    @Test
    fun `showActivity should show existing activity`(){
        val savedActivityId: UUID? = givenActivityExists("Drink Water")
        val actualActivity = service.showActivity(savedActivityId!!)
        Assertions.assertEquals(savedActivityId, actualActivity!!.id)
    }
    private fun givenActivityExists(textType: String): UUID? {
        return repository.saveActivity(textType)
    }

    @Test
    fun `showActivity should return null on missing activity`(){
        val actualActivity = service.showActivity(UUID.randomUUID())

        Assertions.assertNull(actualActivity)
    }
}