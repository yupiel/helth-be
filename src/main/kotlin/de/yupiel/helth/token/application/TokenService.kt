package de.yupiel.helth.token.application

import de.yupiel.helth.authentication.application.AuthenticationService
import de.yupiel.helth.common.NotFoundException
import de.yupiel.helth.common.RequestBodyException
import de.yupiel.helth.user.application.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TokenService(
    @Autowired private val authenticationService: AuthenticationService,
    @Autowired private val userService: UserService
) {
    fun giveExistingUserAuthorizationToken(username: String, password: String): String {
        val retrievedUserAccount = this.userService.getUserWithUsername(username)
            ?: throw NotFoundException("User with username '$username' was not found")
        val passwordValid = this.authenticationService.checkPassword(password, retrievedUserAccount.password)
        if (!passwordValid) throw RequestBodyException("Password for user '$username' was invalid")

        return this.authenticationService.generateJWTAccessToken(retrievedUserAccount.id, retrievedUserAccount.username)
    }
}