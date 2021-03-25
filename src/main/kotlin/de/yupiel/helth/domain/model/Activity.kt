package de.yupiel.helth.domain.model

import java.time.LocalDate
import java.util.*

class Activity(_type: ActivityType, _creationDate: LocalDate) {
    var id: UUID
    private set
    var type: ActivityType
    private set
    var creationDate: LocalDate
    private set

    init {
        this.id = UUID.randomUUID()
        this.type = _type
        this.creationDate = _creationDate
    }

    //Used for loading an Activity from Repository EXCLUSIVELY
    constructor(id: UUID, type: ActivityType, creationDate: LocalDate) : this(type, creationDate) {
        this.id = id
    }

    enum class ActivityType(val asText: String) {
        DRINK_WATER("Drink Water"),
        WALKING("Walking"),
        RUNNING("Running");

        companion object {
            fun from(text: String?): ActivityType? = values().find { it.asText == text }
        }
    }
}