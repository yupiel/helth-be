package de.yupiel.helth.integration.web

import com.beust.klaxon.JsonArray
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
        val activity = this.activityService.findById(UUID.fromString(id))

        return if (activity == null)
            "Activity was not found"
        else
            activityToJsonObject(activity).toJsonString()
    }

    @GetMapping()
    fun showActivitiesInDateRange(
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestParam(required = true, value = "startDate") startDateParam: String,
        @RequestParam(required = true, value = "endDate") endDateParam: String,
        @RequestParam(required = false, defaultValue = "", value = "activityType") activityTypeParamText: String
    ): String {
        return try {
            val userID = authenticationService.extractUserIDFromAuthorizationHeader(authorizationHeader)
                ?: return "Not Authorized"

            val startDate = LocalDate.parse(startDateParam)
            val endDate = LocalDate.parse(endDateParam)

            val activities: List<Activity>?
            if (activityTypeParamText.isEmpty()) {
                activities = this.activityService.findBetweenDates(
                    userID,
                    startDate,
                    endDate
                ) ?: return "No Activities found between the dates of $startDate and $endDate"
            } else {
                val activityType = Activity.ActivityType.valueOf(activityTypeParamText)

                activities = this.activityService.findBetweenDatesWithType(
                    userID,
                    startDate,
                    endDate,
                    activityType
                ) ?: return "No Activities of type $activityTypeParamText found between the dates of $startDate and $endDate"
            }

            activityListToJsonArray(activities).toJsonString()
        } catch (exception: DateTimeParseException) {
            "Date Format was wrong. Try YYYY-MM-DD."
        }
    }

    @PostMapping
    fun saveActivity(
        @RequestBody request: ActivityCreationRequest,
    ): String {
        val activity =
            this.activityService.save(
                UUID.fromString(request.userID),
                LocalDate.parse(request.creationDate),
                request.textType
            ) ?: return "There was an error saving the activity"

        return activityToJsonObject(activity).toJsonString()
    }

    private fun activityToJsonObject(activity: Activity): JsonObject {
        return JsonObject(
            mapOf(
                "id" to activity.id.toString(),
                "type" to activity.type.toString(),
                "creationDate" to activity.creationDate.toString()
            )
        )
    }

    private fun activityListToJsonArray(activities: List<Activity>): JsonArray<JsonObject> {
        val returnValue: JsonArray<JsonObject> = JsonArray()

        activities.forEach {
            returnValue.add(activityToJsonObject(it))
        }

        return returnValue
    }

    data class ActivityCreationRequest(
        val textType: String,
        val creationDate: String,
        val userID: String
    )
}