package de.yupiel.helth.authentication

import de.yupiel.helth.domain.integration.IUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(@Autowired private val userRepository: IUserRepository) {
    private val encoder: BCryptPasswordEncoder = BCryptPasswordEncoder()

    //PASSWORD GENERATION AND CHECKING

    fun encodePassword(password: String): String {
        return encoder.encode(password)
    }
}