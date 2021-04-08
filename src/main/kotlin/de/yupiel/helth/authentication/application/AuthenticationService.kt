package de.yupiel.helth.authentication.application

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import de.yupiel.helth.common.NotAuthorizedException
import de.yupiel.helth.common.RequestHeaderException
import de.yupiel.helth.user.model.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class AuthenticationService(@Autowired private val userRepository: UserRepository) {
    private val encoder: BCryptPasswordEncoder = BCryptPasswordEncoder()
    private val gson: Gson = Gson()

    //PASSWORD GENERATION AND CHECKING
    fun encryptPassword(password: String): String {
        return encoder.encode(password)
    }

    fun checkPassword(password: String, passwordHash: String): Boolean {
        return encoder.matches(password, passwordHash)
    }

    //Only access tokens for now, eventually could add enum to differentiate type in header
    //This technically isn't a real JWT token since it uses bcrypt but the idea is the same
    fun generateJWTAccessToken(userID: UUID, username: String, expirationInHours: Long = 12): String {
        val header = Base64.getEncoder().encodeToString(gson.toJson(JWTHeader()).toString().toByteArray())
        val test = gson.toJson(
            JWTPayload(
                userID,
                username,
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                LocalDateTime.now().plusHours(expirationInHours).truncatedTo(ChronoUnit.SECONDS)
            )
        ).toString()
        val payload = Base64.getEncoder().encodeToString(
            gson.toJson(
                JWTPayload(
                    userID,
                    username,
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                    LocalDateTime.now().plusHours(expirationInHours).truncatedTo(ChronoUnit.SECONDS)
                )
            ).toString().toByteArray()
        )
        val signatureRaw = encoder.encode("${header}.${payload}")
        val signature = Base64.getEncoder().encodeToString(signatureRaw.toByteArray())

        return "${header}.${payload}.${signature}"
    }

    fun checkJWTTokenValidAndReturnPayload(jwtToken: String): JWTPayload {
        val (retrievedHeader, retrievedPayload, retrievedSignature) = jwtToken.split('.')

        val signature = String(Base64.getDecoder().decode(retrievedSignature))

        val verifyingSignature = encoder.matches("${retrievedHeader}.${retrievedPayload}", signature)

        if (!verifyingSignature)
            throw NotAuthorizedException("The Authorization token could not be validated")
        val payload = gson.fromJson(String(Base64.getDecoder().decode(retrievedPayload)), JWTPayload::class.java)
        val expirationDate = payload.expires

        if (expirationDate < LocalDateTime.now())
            throw NotAuthorizedException("The access token has expired. Please login again.")

        return payload
    }

    fun extractUserIDFromAuthorizationHeader(authorizationHeader: String): UUID {
        val authHeaderParts = authorizationHeader.split(" ")
        if (authHeaderParts[0] != "Bearer")
            throw RequestHeaderException("Wrong or missing Authorization type used in Header")
        if (authHeaderParts.size < 2)
            throw RequestHeaderException("No Bearer token value found in Authorization Header")

        val jwtTokenPayload =
            this.checkJWTTokenValidAndReturnPayload(authHeaderParts[1])

        return jwtTokenPayload.userID
    }

    data class JWTHeader(
        @SerializedName("alg") val algorithm: String = "BCRYPT",
        @SerializedName("typ") val type: String = "ACCESS_JWT"
    )

    data class JWTPayload(
        @SerializedName("user_id") val userID: UUID,
        @SerializedName("username") val username: String,
        @SerializedName("iat") val issuedAt: LocalDateTime,
        @SerializedName("exp") val expires: LocalDateTime
    )
}