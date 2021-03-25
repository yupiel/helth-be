package de.yupiel.helth.integration.web

import de.yupiel.helth.domain.application.ActivityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/user/{username}/activities")
class ActivityController() {
    @Autowired
    lateinit var activityService: ActivityService

    @GetMapping("/{activityid}")
    public fun showActivity(@PathVariable("activityid") id: String): String {
        val activity = this.activityService.showActivity(UUID.fromString(id))
        return "id=${activity?.id}&name=${activity?.type}&created${activity?.creationDate}"
    }

    @PostMapping
    public fun saveActivity(
        @RequestBody request: ActivityRequest,
    ): String {
        val returnedUUID: UUID? = this.activityService.saveActivity(request.textType)

        return returnedUUID?.toString() ?: "There was an error saving the activity"
    }

    data class ActivityRequest(
        val textType: String
    )
}