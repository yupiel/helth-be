package de.yupiel.helth.domain.model

import java.time.LocalDate
import java.util.*

class User(_id: UUID, _username: String, _password: String) {
    var id: UUID
        private set
    var username: String
        private set
    var password: String
        private set
    var creationDate: LocalDate
        private set
    var activities: List<Activity>
        private set
    var challenges: List<Challenge>
        private set

    init {
        this.id = _id
        this.username = _username
        this.password = _password
        this.creationDate = LocalDate.now()
        this.activities = mutableListOf()
        this.challenges = mutableListOf()
    }

    constructor(
        id: UUID,
        username: String,
        password: String,
        creationDate: LocalDate,
        userActivities: List<Activity>,
        userChallenges: List<Challenge>
    ) : this(id, username, password) {
        this.creationDate = creationDate

        if (userActivities.isNotEmpty())
            this.activities = userActivities
        if (userChallenges.isNotEmpty())
            this.challenges = userChallenges
    }
}