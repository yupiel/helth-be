package de.yupiel.helth.domain.model

import java.time.LocalDate
import java.util.*

class Activity(type: ActivityType, creationDate: LocalDate) {
    var id: UUID
        private set
    var type: ActivityType
        private set
    var creationDate: LocalDate
        private set

    init {
        this.id = UUID.randomUUID()
        this.type = type
        this.creationDate = creationDate
    }

    //Used for loading an Activity from Repository EXCLUSIVELY
    constructor(
        id: UUID,
        type: ActivityType,
        creationDate: LocalDate
    ) : this(type, creationDate) {
        this.id = id
    }

    enum class ActivityType {
        DRINK_WATER,
        WALKING,
        RUNNING
    }
}