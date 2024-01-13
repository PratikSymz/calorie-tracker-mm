package com.pratiksymz.tracker_domain.model

/**
 * This class represents a food that can potentially be tracked (search result), that has certain nutriments
 */
data class TrackableFood(
    // Product [DATA/remote/dto]
    val name: String,
    val imageUrl: String?,
    // Nutriments [DATA/remote/dto]
    val caloriesPer100g: Int,
    val carbsPer100g: Int,
    val proteinPer100g: Int,
    val fatPer100g: Int
)
