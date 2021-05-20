package de.yupiel.helth.user.model

import java.math.BigInteger
import java.time.LocalDate
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name="\"user\"")
class User(username: String, password: String) {
    @Id
    @Column(name = "id", unique = true, updatable = false)
    var id: UUID = UUID.randomUUID()
        private set
    @Column(name = "username", nullable = false, unique = false)
    var username: String = username
        private set
    @Column(name = "password", nullable = false, unique = false)
    var password: String = password
        private set
    @Column(name = "score", nullable = false, unique = false, columnDefinition = "varchar")
    var score: BigInteger = BigInteger("999999999")
        private set
    @Column(name = "creation_date", nullable = false, unique = false)
    var creationDate: LocalDate = LocalDate.now()
        private set
}