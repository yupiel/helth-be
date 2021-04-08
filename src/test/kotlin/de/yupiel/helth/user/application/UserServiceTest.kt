package de.yupiel.helth.user.application

import de.yupiel.helth.user.model.UserRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
internal class UserServiceTest {
    @Autowired
    private lateinit var repository: UserRepository
    private lateinit var userService: UserService

    @BeforeEach
    fun before() {
        userService = UserService(repository)

        userService.createUser("yupiel", "yeetyote")
    }

    @Test
    fun `getUserByUsername finds user in repository and should return User model`() {
        val username = "yupiel"
        val actual = userService.getUserWithUsername(username)

        assertNotNull(actual)
        assertEquals(username, actual!!.username)
    }

    @Test
    fun `getUserByUsername does not find user in repository and should return null`() {
        val username = randomUsername(16)
        val actual = userService.getUserWithUsername(username)

        assertNull(actual)
    }

    @Test
    fun createNewUserByUsernameAndPassword() {

    }

    @Test
    fun findByUsername() {
    }

    @Test
    fun createNewUser() {
    }

    // Helper functions
    private fun randomUsername(length: Int): String = List(length) {
        (('a'..'z') + ('A'..'Z') + ('0'..'9')).random()
    }.joinToString("")
}
