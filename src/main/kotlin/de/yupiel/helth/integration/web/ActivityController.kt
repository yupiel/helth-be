package de.yupiel.helth.integration.web

import com.beust.klaxon.JsonObject
import de.yupiel.helth.authentication.AuthenticationService
import de.yupiel.helth.domain.application.ActivityService
import de.yupiel.helth.domain.model.Activity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.format.DateTimeParseException
import java.util.*

@RestController
@RequestMapping("/users/activities")
class ActivityController(
    @Autowired private val activityService: ActivityService,
    @Autowired private val authenticationService: AuthenticationService
) {
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

    @GetMapping()
    fun showActivitiesInDateRange(
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestParam(required = true, value = "startDate") startDateParam: String,
        @RequestParam(required = true, value = "endDate") endDateParam: String,
        @RequestParam(required = false, defaultValue = "", value = "activityType") activityTypeParamText: String
    ): String {
        return try {
            val userID = authenticationService.extractUserIDFromAuthorizationHeader(authorizationHeader) ?: return "Not Authorized"

            val startDate = LocalDate.parse(startDateParam)
            val endDate = LocalDate.parse(endDateParam)

            if (activityTypeParamText.isEmpty()) {
                this.activityService.showActivitiesBetweenDates(
                    userID,
                    startDate,
                    endDate
                )!!.toJsonString()
            } else {
                val activityType = Activity.ActivityType.valueOf(activityTypeParamText)

                this.activityService.showActivitiesBetweenDatesWithType(
                    userID,
                    startDate,
                    endDate,
                    activityType
                )!!.toJsonString()
            }
        } catch (exception: DateTimeParseException) {
            "Date Format was wrong. Try YYYY-MM-DD."
        }
    }

    @PostMapping
    fun saveActivity(
        @RequestBody request: ActivityCreationRequest,
    ): String {
        val returnedUUID: UUID? =
            this.activityService.saveActivity(
                UUID.fromString(request.userID),
                LocalDate.parse(request.creationDate),
                request.textType
            )

        return returnedUUID?.toString() ?: "There was an error saving the activity"
    }

    data class ActivityCreationRequest(
        val textType: String,
        val creationDate: String,
        val userID: String
    )
}