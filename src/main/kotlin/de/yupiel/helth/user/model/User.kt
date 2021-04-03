package de.yupiel.helth.user.model

import java.math.BigInteger
import java.time.LocalDate
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class User(username: String, password: String) {
    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    var id: UUID
        private set
    @Column(name = "username", nullable = false, unique = false)
    var username: String
        private set
    @Column(name = "password", nullable = false, unique = false)
    var password: String
        private set
    @Column(name = "score", nullable = false, unique = false)
    var score: BigInteger
        private set
    @Column(name = "creation_date", nullable = false, unique = false)
    var creationDate: LocalDate
        private set

    init {
        this.id = UUID.randomUUID()
        this.username = username
        this.password = password
        this.score = BigInteger("0")    //TODO add support in service and Controller
        this.creationDate = LocalDate.now()
    }
}