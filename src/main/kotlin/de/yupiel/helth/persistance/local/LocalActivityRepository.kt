package de.yupiel.helth.persistance.local

import de.yupiel.helth.domain.integration.ActivityRepositoryData
import de.yupiel.helth.domain.integration.IActivityRepository
import de.yupiel.helth.domain.model.Activity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.dao.DataAccessException
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.ResultSet
import java.time.LocalDate
import java.util.*

@Repository
@Profile("local")
class LocalActivityRepository(@Autowired val jtm: JdbcTemplate) : IActivityRepository {
    val rowMapper: RowMapper<ActivityRepositoryData> = RowMapper<ActivityRepositoryData> { rs: ResultSet, _: Int ->
        if (rs.wasNull()) return@RowMapper null

        ActivityRepositoryData(
            UUID.fromString(rs.getString("id")),
            Activity.ActivityType.valueOf(rs.getString("type")),
            rs.getDate("creation_date").toLocalDate()
        )
    }

    override fun findById(id: UUID): ActivityRepositoryData? {
        return try {
            jtm.queryForObject(
                "SELECT * FROM activities WHERE id = ?",
                rowMapper,
                id
            )
        } catch (exception: IncorrectResultSizeDataAccessException) {
            null
        }
    }

    override fun findBetweenDates(
        userID: UUID,
        startDate: LocalDate,
        endDate: LocalDate
    ): MutableList<ActivityRepositoryData>? {
        return try {
            jtm.query(
                "SELECT * FROM activities WHERE user_id = ? AND creation_date >= ? AND creation_date <= ?",
                rowMapper,
                userID,
                startDate,
                endDate
            )
        } catch (exception: DataAccessException) {
            null
        }
    }
    override fun findBetweenDatesWithType(
        userID: UUID,
        startDate: LocalDate,
        endDate: LocalDate,
        activityType: Activity.ActivityType
    ): MutableList<ActivityRepositoryData>? {
        return try {
            jtm.query(
                "SELECT * FROM activities WHERE user_id = ? AND creation_date >= ? AND creation_date <= ? AND type = ?",
                rowMapper,
                userID,
                startDate.toString(),
                endDate.toString(),
                activityType.toString()
            )
        } catch (exception: DataAccessException) {
            null
        }
    }

    override fun saveActivity(activity: Activity, userID: UUID): UUID? {
        return try {
            jtm.update(
                "INSERT INTO activities VALUES (?,?,?,?)",
                activity.id,
                activity.type.toString(),
                activity.creationDate,
                userID
            )

            activity.id
        } catch (exception: DataAccessException) {
            return null
        }
    }
}