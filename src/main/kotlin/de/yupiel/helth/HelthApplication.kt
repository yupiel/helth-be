package de.yupiel.helth

import de.yupiel.helth.domain.integration.ActivityRepository
import de.yupiel.helth.domain.model.Activity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.time.LocalDate
import java.util.*

@SpringBootApplication
class HelthApplication

fun main(args: Array<String>) {
    runApplication<HelthApplication>(*args)
}
