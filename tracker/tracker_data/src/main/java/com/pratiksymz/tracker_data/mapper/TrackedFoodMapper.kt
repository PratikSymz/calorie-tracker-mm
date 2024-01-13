package com.pratiksymz.tracker_data.mapper

import com.pratiksymz.tracker_data.local.entity.TrackedFoodEntity
import com.pratiksymz.tracker_domain.model.MealType
import com.pratiksymz.tracker_domain.model.TrackedFood
import java.time.LocalDate

/**
 * Local: This method coverts a TrackedFoodEntity [DATA Layer] to a TrackedFood [DOMAIN layer]
 */
fun TrackedFoodEntity.toTrackedFood(): TrackedFood {
    return TrackedFood(
        id = id,
        name = name,
        carbs = carbs,
        protein = protein,
        fat = fat,
        imageUrl = imageUrl,
        mealType = MealType.fromString(mealType),
        amount = amount,
        date = LocalDate.of(year, month, dayOfMonth),
        calories = calories
    )
}

/**
 * Local: This method coverts a TrackedFood [DOMAIN layer] to a TrackedFoodEntity [DATA Layer]
 */
fun TrackedFood.toTrackedFoodEntity(): TrackedFoodEntity {
    return TrackedFoodEntity(
        id = id,
        name = name,
        carbs = carbs,
        protein = protein,
        fat = fat,
        calories = calories,
        imageUrl = imageUrl,
        mealType = mealType.name,
        amount = amount,
        dayOfMonth = date.dayOfMonth,
        month = date.monthValue,
        year = date.year
    )
}