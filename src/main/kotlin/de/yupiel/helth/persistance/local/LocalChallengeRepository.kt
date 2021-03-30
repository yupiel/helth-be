package de.yupiel.helth.persistance.local

import de.yupiel.helth.domain.integration.ChallengeRepositoryData
import de.yupiel.helth.domain.integration.IChallengeRepository
import de.yupiel.helth.domain.model.Challenge
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.*

@Repository
@Profile("local")
class LocalChallengeRepository(@Autowired val jtm: JdbcTemplate): IChallengeRepository {
    val rowMapper: RowMapper<ChallengeRepositoryData> = RowMapper<ChallengeRepositoryData> { rs: ResultSet, _: Int ->
        if(rs.wasNull()) return@RowMapper null

        ChallengeRepositoryData(
            UUID.fromString(rs.getString("id")),
            rs.getString("activity_type"),
            rs.getInt("amount_of_times_day"),
            rs.getDate("start_date").toLocalDate(),
            rs.getDate("expiration_date").toLocalDate(),
            rs.getString("challenge_status")
        )
    }

    override fun findAll(): MutableList<ChallengeRepositoryData>? {
        return try {
            val test = jtm.query(
                "SELECT * FROM challenges",
                rowMapper
            )
            test
        } catch (exception: DataAccessException) {
            null
        }
    }

    override fun findAll(userID: UUID): MutableList<ChallengeRepositoryData>? {
        return try {
            val test = jtm.query(
                "SELECT * FROM challenges WHERE user_id = ?",
                rowMapper,
                userID.toString()
            )
            test
        } catch (exception: DataAccessException) {
            null
        }
    }

    override fun saveChallenge(challenge: Challenge, userID: UUID): UUID? {
        return try {
            jtm.update(
                "INSERT INTO challenges VALUES (?,?,?,?,?,?,?)",
                challenge.id,
                challenge.activityType.toString(),
                challenge.amountOfTimesADay,
                challenge.startDate,
                challenge.expirationDate,
                challenge.challengeStatus.toString(),
                userID
            )

            challenge.id
        } catch (exception: DataAccessException){
            return null
        }
    }
}