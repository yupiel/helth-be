package de.yupiel.helth.token.integration

import de.yupiel.helth.token.application.TokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tokens", produces = [MediaType.APPLICATION_JSON_VALUE])
class TokenController(
    @Autowired val tokenService: TokenService
) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun authenticateUserAndCreateNewSession(
        @RequestBody request: UserSessionCreationRequest
    ): TokenDto {
        return TokenDto(
            this.tokenService.giveExistingUserAuthorizationToken(
                request.username,
                request.password
            )
        )
    }

    class UserSessionCreationRequest(
        val username: String,
        val password: String
    )

    data class TokenDto(
        val token: String
    )
}