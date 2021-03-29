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
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.*

@Repository
@Profile("local")
class LocalActivityRepository(@Autowired val jtm: JdbcTemplate) : IActivityRepository {
    val rowMapper: RowMapper<ActivityRepositoryData> = RowMapper<ActivityRepositoryData> { rs: ResultSet, _: Int ->
        if (rs.wasNull()) return@RowMapper null

        ActivityRepositoryData(
            UUID.fromString(rs.getString("id")),
            Activity.ActivityType.from(rs.getString("type"))!!,
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
            val sql = """
            INSERT INTO activities (id, type, creation_date, user_id) VALUES
            (${activity.id}, ${activity.type}, ${activity.creationDate}, $userID)
        """.trimIndent()

            jtm.execute(sql)

            activity.id
        } catch (exception: DataAccessException){
            return null
        }
    }
}