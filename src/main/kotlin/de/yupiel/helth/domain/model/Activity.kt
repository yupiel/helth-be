package de.yupiel.helth.domain.model

import java.time.LocalDate
import java.util.*

class Activity(_type: ActivityType, _creationDate: LocalDate) {
    var id: UUID? = null
    private set
    var type: ActivityType? = null
    private set
    var creationDate: LocalDate? = null
    private set

    init {
        this.type = _type
        this.creationDate = _creationDate
    }

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