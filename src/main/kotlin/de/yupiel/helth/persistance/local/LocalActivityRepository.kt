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
            val sql = "SELECT * FROM activities WHERE id = '$id'"
            jtm.queryForObject(sql, rowMapper)
        } catch (exception: IncorrectResultSizeDataAccessException) {
            null
        }
    }

    override fun saveActivity(activity: Activity, userID: UUID): UUID? {
        return try {
            jtm.update(
                "INSERT INTO activities VALUES (?,?,?,?)",
                activity.id.toString(),
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