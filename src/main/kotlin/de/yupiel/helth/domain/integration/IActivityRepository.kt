package de.yupiel.helth.domain.integration

import de.yupiel.helth.domain.model.Activity
import java.util.*

interface IActivityRepository {
    fun findById(id: UUID): Activity?
    fun saveActivity(textType: String): UUID?
}