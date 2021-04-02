package de.yupiel.helth.authentication.application

import de.yupiel.helth.user.model.UserRepository
import de.yupiel.helth.user.model.User
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
internal class AuthenticationServiceTest {
    @Autowired
    private lateinit var repository: UserRepository
    private lateinit var service: AuthenticationService

    @BeforeEach
    fun beforeEach(){
        service = AuthenticationService(repository)
    }

    @Test
    fun `encodePassword should return hashed password String`() {
        val password = "yeetyote"
        val actual = service.encodePassword(password)

        assertTrue(actual.isNotEmpty())
    }

    @Test
    fun `checkPassword matches passwords correctly and should return true`() {
        val password = "yeetyote"
        val validPasswordHash = "\$2a\$10\$rsp9oa3MwARG61AuEgVyWOMQ67SX/G9YpHYMiCEgRYurr6VdKMApK"

        val actual = service.checkPassword(password, validPasswordHash)

        assertTrue(actual)
    }

    @Test
    fun `checkPassword can not match password and should return false`() {
        val password = "yeetyote"
        val invalidPasswordHash = "\$2a\$10\$rsp9oa3DFrtg37DgEgVyWOMQ67SX/G9YpHYMiCEgRYurr6VdKMApK"

        val actual = service.checkPassword(password, invalidPasswordHash)

        assertFalse(actual)
    }

    @Test
    fun `generateJWTAccessToken generates a JWT access token from User parameter and should return token String`() {
        val user = User("yupiel", "verySecurePassword")

        val actual = service.generateJWTAccessToken(user.id, user.username)

        assertTrue(actual.isNotEmpty())
    }

    @Test
    fun `checkJWTTokenValidAndReturnPayload should validate JWT access token and should not be null`() {
        val user = User("yupiel", "verySecurePassword")
        val validJWTAccessToken = service.generateJWTAccessToken(user.id, user.username)
        println(validJWTAccessToken)

        val actual = service.checkJWTTokenValidAndReturnPayload(validJWTAccessToken)
        assertNotNull(actual)
    }

    @Test
    fun `checkJWTTokenValidAndReturnPayload should validate JWT access token and should return user UUID`() {
        val user = User("yupiel", "verySecurePassword")
        val validJWTAccessToken = service.generateJWTAccessToken(user.id, user.username)

        val actual = service.checkJWTTokenValidAndReturnPayload(validJWTAccessToken)
        assertEquals(user.id.toString(), actual!!["user_id"])
    }
}