package de.yupiel.helth.activity.integration

import de.yupiel.helth.authentication.application.AuthenticationService
import de.yupiel.helth.activity.application.ActivityService
import de.yupiel.helth.activity.model.Activity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.util.*

@RestController
@RequestMapping("/activities", produces = [MediaType.APPLICATION_JSON_VALUE])
class ActivityController(
    @Autowired private val activityService: ActivityService,
    @Autowired private val authenticationService: AuthenticationService
) {
    @GetMapping("/{activityId}")
    fun showActivity(@PathVariable("activityId") id: String): ActivityDto {
        val activity = this.activityService.findById(UUID.fromString(id))

        return ActivityDto.from(activity)
    }

    @GetMapping
    fun showActivitiesInDateRange(
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestParam(required = true, value = "startDate") startDateParam: String,
        @RequestParam(required = true, value = "endDate") endDateParam: String,
        @RequestParam(required = false, defaultValue = "", value = "activityType") activityTypeParamText: String
    ): List<ActivityDto> {
        val userID = authenticationService.extractUserIDFromAuthorizationHeader(authorizationHeader)

        val startDate = LocalDate.parse(startDateParam)
        val endDate = LocalDate.parse(endDateParam)

        val activities: List<Activity> = if (activityTypeParamText.isEmpty()) {
            this.activityService.findBetweenDates(
                userID,
                startDate,
                endDate
            )
        } else {
            this.activityService.findBetweenDatesWithType(
                userID,
                startDate,
                endDate,
                activityTypeParamText
            )
        }

        return activities.map {
            ActivityDto.from(it)
        }
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun saveActivity(
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestBody request: ActivityCreationRequest,
    ): ActivityDto {
        val userID = authenticationService.extractUserIDFromAuthorizationHeader(authorizationHeader)

        val activity =
            this.activityService.createActivity(
                userID,
                LocalDate.parse(request.creationDate),
                request.textType
            )

        return ActivityDto.from(activity)
    }

    data class ActivityCreationRequest(
        val textType: String,
        val creationDate: String
    )

    data class ActivityDto(
        val id: String,
        val activityType: String,
        val creationDate: String,
        val userID: String
    ) {
        companion object {
            fun from(activity: Activity): ActivityDto {
                return ActivityDto(
                    activity.id.toString(),
                    activity.type.toString(),
                    activity.creationDate.toString(),
                    activity.userID.toString()
                )
            }
        }
    }
}