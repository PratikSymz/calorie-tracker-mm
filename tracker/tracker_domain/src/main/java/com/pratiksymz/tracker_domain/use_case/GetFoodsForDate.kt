package com.pratiksymz.tracker_domain.use_case

import com.pratiksymz.tracker_domain.model.TrackedFood
import com.pratiksymz.tracker_domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * This use case is responsible for checking the search query as not blank and uses the repository to search for foods.
 */
class GetFoodsForDate(
    private val repository: TrackerRepository
) {
    operator fun invoke(date: LocalDate): Flow<List<TrackedFood>> {
        return repository.getFoodsForDate(date)
    }
}