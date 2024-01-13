package com.pratiksymz.tracker_data.mapper

import com.pratiksymz.tracker_data.remote.dto.Product
import com.pratiksymz.tracker_domain.model.TrackableFood
import kotlin.math.roundToInt

/**
 * Remote: This method coverts a Product Dto [DATA Layer] to a Trackable Product [DOMAIN layer]
 */
fun Product.toTrackableFood(): TrackableFood? {
    val carbsPer100g = nutriments.carbohydrates100g.roundToInt()
    val proteinPer100g = nutriments.proteins100g.roundToInt()
    val fatPer100g = nutriments.fat100g.roundToInt()
    val caloriesPer100g = nutriments.energyKcal100g.roundToInt()

    return TrackableFood(
        name = productName ?: return null,
        carbsPer100g = carbsPer100g,
        proteinPer100g = proteinPer100g,
        fatPer100g = fatPer100g,
        caloriesPer100g = caloriesPer100g,
        imageUrl = imageFrontThumbUrl
    )
}