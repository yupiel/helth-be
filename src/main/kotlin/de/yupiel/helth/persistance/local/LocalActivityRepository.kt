package de.yupiel.helth.persistance.local

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import de.yupiel.helth.domain.integration.IActivityRepository
import de.yupiel.helth.domain.model.Activity
import org.springframework.stereotype.Component
import java.io.File
import java.time.LocalDate
import java.util.*

@Component
class LocalActivityRepository() : IActivityRepository {
    override fun findById(id: UUID): Activity? {
        val jsonOfFile = Parser.default()
            .parse("./src/main/kotlin/de/yupiel/helth/persistance/local/test.json") as JsonArray<JsonObject>
        val activity = jsonOfFile.find { it["id"] == id.toString() }

        val retrievedActivityID = activity?.get("id")
        val retrievedActivityTextType = activity?.get("type")
        val retrievedActivityCreationDate = activity?.get("creationDate")

        return if (retrievedActivityID == null || retrievedActivityTextType == null || retrievedActivityCreationDate == null)
            null
        else
            Activity(
                UUID.fromString(retrievedActivityID as String),
                Activity.ActivityType.valueOf(retrievedActivityTextType as String),
                LocalDate.parse(retrievedActivityCreationDate as String)
            )
    }

    override fun saveActivity(textType: String): UUID? {
        val enumOfTextType: Activity.ActivityType = Activity.ActivityType.from(textType)!!
        val newActivity = Activity(UUID.randomUUID(), enumOfTextType, LocalDate.now())

        val jsonOfFile = Parser.default()
            .parse("./src/main/kotlin/de/yupiel/helth/persistance/local/test.json") as JsonArray<JsonObject>
        jsonOfFile.add(
            JsonObject(
                mapOf(
                    "id" to newActivity.id.toString(),
                    "type" to newActivity.type!!,
                    "creationDate" to newActivity.creationDate.toString()
                )
            )
        )

        File("./src/main/kotlin/de/yupiel/helth/persistance/local/test.json").bufferedWriter().use { out ->
            out.write(jsonOfFile.toJsonString())
        }

        return newActivity.id
    }
}