package de.yupiel.helth.persistance.local

import de.yupiel.helth.domain.integration.IUserRepository
import de.yupiel.helth.domain.integration.UserRepositoryData
import de.yupiel.helth.domain.model.User
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
class LocalUserRepository(@Autowired val jtm: JdbcTemplate) : IUserRepository {
    val rowMapper: RowMapper<UserRepositoryData> = RowMapper<UserRepositoryData> { rs: ResultSet, _: Int ->
        if (rs.wasNull()) return@RowMapper null

        UserRepositoryData(
            UUID.fromString(rs.getString("id")),
            rs.getString("username"),
            rs.getString("password"),
            rs.getDate("creation_date").toLocalDate()
        )
    }

    //TODO only return data classes

    override fun findByUsername(username: String): UserRepositoryData? {
        return try {    //TODO add prepared Statements
            val sql = "SELECT * FROM users WHERE username = '$username'"
            jtm.queryForObject(sql, rowMapper)
        } catch (exception: IncorrectResultSizeDataAccessException) {
            null
        }
    }

    override fun createUser(user: User): UUID? {
        return try {
            val sql = """
            INSERT INTO users (id, username, password, creation_date) VALUES
            (${user.id}, ${user.username}, ${user.password}, ${user.creationDate})
        """.trimIndent()

            jtm.execute(sql)

            user.id
        } catch (exception: DataAccessException){
            return null
        }
    }
}
