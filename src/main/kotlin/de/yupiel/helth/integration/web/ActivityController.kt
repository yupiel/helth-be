package de.yupiel.helth.integration.web

import com.beust.klaxon.JsonObject
import de.yupiel.helth.domain.application.ActivityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.util.*

@RestController
@RequestMapping("/users/{username}/activities")
class ActivityController() {
    @Autowired
    lateinit var activityService: ActivityService

    @GetMapping("/{activityId}")
    fun showActivity(@PathVariable("activityId") id: String): String {
        val activity = this.activityService.showActivity(UUID.fromString(id))
        return if (activity == null)
            "Activity was not found"
        else
            JsonObject(
                mapOf(
                    "id" to activity.id.toString(),
                    "type" to activity.type,
                    "createdAt" to activity.creationDate.toString()
                )
            ).toJsonString()
    }

    @PostMapping
    fun saveActivity(
        @RequestBody request: ActivityCreationRequest,
    ): String {
        val returnedUUID: UUID? =
            this.activityService.saveActivity(
                request.textType,
                LocalDate.parse(request.creationDate),
                UUID.fromString(request.userID)
            )

        return returnedUUID?.toString() ?: "There was an error saving the activity"
    }

    data class ActivityCreationRequest(
        val textType: String,
        val creationDate: String,
        val userID: String
    )
}