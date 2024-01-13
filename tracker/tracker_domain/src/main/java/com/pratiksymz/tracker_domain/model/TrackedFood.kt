package com.pratiksymz.tracker_domain.model

import java.time.LocalDate

/**
 * This class represents a food that should be tracked (local tracked food entity)
 */
data class TrackedFood(
    val id: Int? = null,
    val name: String,
    val carbs: Int,
    val protein: Int,
    val fat: Int,
    val imageUrl: String?,
    val mealType: MealType,
    val amount: Int,
    val date: LocalDate,
    val calories: Int
)
