package com.pratiksymz.tracker_domain.use_case

import com.pratiksymz.tracker_domain.model.TrackedFood
import com.pratiksymz.tracker_domain.repository.TrackerRepository

/**
 * This use case is responsible for checking the search query as not blank and uses the repository to search for foods.
 */
class DeleteTrackedFood(
    private val repository: TrackerRepository
) {
    suspend operator fun invoke(trackedFood: TrackedFood) {
        repository.deleteTrackedFood(trackedFood)
    }
}