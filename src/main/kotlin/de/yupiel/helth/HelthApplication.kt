package de.yupiel.helth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HelthApplication

fun main(args: Array<String>) {
    runApplication<HelthApplication>(*args)
}
