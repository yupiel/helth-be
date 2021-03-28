package de.yupiel.helth.domain.model

import java.time.LocalDate
import java.util.*

class User(username: String, password: String) {
    var id: UUID
        private set
    var username: String
        private set
    var password: String
        private set
    var creationDate: LocalDate
        private set
    var activities: MutableList<Activity>
    var challenges: MutableList<Challenge>

    init {
        this.id = UUID.randomUUID()
        this.username = username
        this.password = password
        this.creationDate = LocalDate.now()
        this.activities = mutableListOf()
        this.challenges = mutableListOf()
    }

    constructor(
        id: UUID,
        username: String,
        password: String,
        creationDate: LocalDate
    ) : this(username, password) {
        this.id = id
        this.creationDate = creationDate
    }
}