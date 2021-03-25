package de.yupiel.helth.domain.application

import de.yupiel.helth.domain.integration.IActivityRepository
import de.yupiel.helth.domain.model.Activity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ActivityService(@Autowired private val activityRepository: IActivityRepository) {
    public fun showActivity(id: UUID): Activity? {
        return activityRepository.findById(id)
    }

    public fun saveActivity(textType: String): UUID? {
        return activityRepository.saveActivity(textType)
    }
}