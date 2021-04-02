package de.yupiel.helth.authentication.application

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import de.yupiel.helth.common.NotAuthorizedException
import de.yupiel.helth.common.RequestHeaderException
import de.yupiel.helth.user.model.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class AuthenticationService(@Autowired private val userRepository: UserRepository) {
    private val encoder: BCryptPasswordEncoder = BCryptPasswordEncoder()

    //PASSWORD GENERATION AND CHECKING
    fun encodePassword(password: String): String {
        return encoder.encode(password)
    }

    fun checkPassword(password: String, passwordHash: String): Boolean {
        return encoder.matches(password, passwordHash)
    }

    //JWT TOKEN GENERATION AND CHECKING
    private final val algorithmTypeShorthand: String = "BCRYPT"
    private final val tokenTypeAccess: String = "ACCESS_JWT"

    //Only access tokens for now, eventually could add enum to differentiate type in header
    //This technically isn't a real JWT token since it uses bcrypt but the idea is the same
    fun generateJWTAccessToken(userID: UUID, username: String, expirationInHours: Long = 12): String {
        val header = Base64.getEncoder().encodeToString(
            JsonObject(
                mapOf(
                    "alg" to algorithmTypeShorthand,
                    "typ" to tokenTypeAccess
                )
            ).toJsonString().encodeToByteArray()
        )
        val payload = Base64.getEncoder().encodeToString(
            JsonObject(
                mapOf(
                    "user_id" to userID.toString(),
                    "username" to username,
                    "iat" to LocalTime.now().truncatedTo(ChronoUnit.SECONDS).toString(),
                    "exp" to LocalTime.now().plusHours(expirationInHours).truncatedTo(ChronoUnit.SECONDS).toString()
                )
            ).toJsonString().encodeToByteArray()
        )
        val signatureRaw = encoder.encode("${header}.${payload}")
        val signature = Base64.getEncoder().encodeToString(signatureRaw.toByteArray())

        return "${header}.${payload}.${signature}"
    }

    fun checkJWTTokenValidAndReturnPayload(jwtToken: String): JsonObject {
        val (retrievedHeader, retrievedPayload, retrievedSignature) = jwtToken.split('.')

        val signature = String(Base64.getDecoder().decode(retrievedSignature))

        val verifyingSignature = encoder.matches("${retrievedHeader}.${retrievedPayload}", signature)

        return if (!verifyingSignature)
            throw NotAuthorizedException("The Authorization token could not be validated")
        else {
            val payload = String(Base64.getDecoder().decode(retrievedPayload))
            Parser.default().parse(StringBuilder(payload)) as JsonObject
        }
    }

    fun extractUserIDFromAuthorizationHeader(authorizationHeader: String): UUID {
        val authHeaderParts = authorizationHeader.split(" ")
        if (authHeaderParts[0] != "Bearer")
            throw RequestHeaderException("Wrong or missing Authorization type used in Header")
        if (authHeaderParts.size < 2)
            throw RequestHeaderException("No Bearer token value found in Authorization Header")

        val jwtTokenPayload =
            this.checkJWTTokenValidAndReturnPayload(authHeaderParts[1])

        return UUID.fromString(jwtTokenPayload["user_id"] as String)
    }
}