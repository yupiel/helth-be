package de.yupiel.helth.domain.application

import de.yupiel.helth.domain.integration.ChallengeRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.event.annotation.BeforeTestClass
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
            3,
            LocalDate.now().toString(),
            LocalDate.now().plusDays(7).toString()
        )
        service.save(
            UUID.fromString("b1805241-3365-455f-8ba9-6228458c55b0"),
            "RUNNING",
            1,
            LocalDate.now().toString(),
            LocalDate.now().plusDays(7).toString()
        )
        service.save(
            UUID.fromString("b1805241-3365-455f-8ba9-6228458c55b0"),
            "WALKING",
            1,
            LocalDate.now().toString(),
            LocalDate.now().plusDays(7).toString()
        )
    }

    @Test
    fun test() {
        val test = service.findAllForUserID(UUID.fromString("b1805241-3365-455f-8ba9-6228458c55b0"))
        Assertions.assertTrue(test!!.isNotEmpty())
    }
}