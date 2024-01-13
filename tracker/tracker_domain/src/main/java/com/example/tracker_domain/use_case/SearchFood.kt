package com.example.tracker_domain.use_case

import com.example.tracker_domain.model.TrackableFood
import com.example.tracker_domain.repository.TrackerRepository

/**
 * This use case is responsible for checking the search query as not blank and uses the repository to search for foods.
 */
class SearchFood(
    private val repository: TrackerRepository
) {
    suspend operator fun invoke(
        query: String,
        page: Int = 1,
        pageSize: Int = 40
    ): Result<List<TrackableFood>> {

        if (query.isBlank()) {  // " " -> Counts as Blank
            return Result.success(emptyList())
        }
        return repository.searchFood(query.trim(), page, pageSize)
    }
}