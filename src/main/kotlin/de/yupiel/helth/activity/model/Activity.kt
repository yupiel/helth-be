package de.yupiel.helth.activity.model

import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
@Embeddable
class Activity(type: ActivityType, creationDate: LocalDate, userID: UUID) {
    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    var id: UUID = UUID.randomUUID()
        private set
    @Column(name = "type", nullable = false, unique = false, length = 15)
    @Enumerated(EnumType.STRING)
    var type: ActivityType
        private set
    @Column(name = "creation_date", nullable = false, unique = false, length = 10)
    var creationDate: LocalDate
        private set
    @Column(name = "user_id", nullable = false, unique = false)
    var userID: UUID = userID
        private set

    init {
        this.type = type
        this.creationDate = creationDate
    }

    //Used for loading an Activity from Repository EXCLUSIVELY
    constructor(
        id: UUID,
        type: ActivityType,
        creationDate: LocalDate,
        userID: UUID
    ) : this(type, creationDate, userID) {
        this.id = id
    }

    enum class ActivityType {
        DRINK_WATER,
        WALKING,
        RUNNING,
        CYCLING,
        SWIMMING,
        CALISTHENICS,
    }
}