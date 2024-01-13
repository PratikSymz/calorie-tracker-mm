package com.pratiksymz.tracker_presentation.tracker_overview

import androidx.annotation.DrawableRes
import com.pratiksymz.core.R
import com.pratiksymz.core.util.UiText
import com.pratiksymz.tracker_domain.model.MealType

/**
 * This class a represents state for a meal type - B, L, D or S.
 * This includes the values of the nutrients, a drawable resource and whether it's expanded.
 * ONLY used to reflect state in Presentation layer and not needed anywhere else (DATA or DOMAIN).
 */
data class Meal(
    val name: UiText,
    @DrawableRes val drawableRes: Int,
    val mealType: MealType,
    val carbs: Int = 0,
    val protein: Int = 0,
    val fat: Int = 0,
    val calories: Int = 0,
    val isExpanded: Boolean = false
)

val defaultMeals = listOf(
    Meal(
        name = UiText.StringResource(R.string.breakfast),
        drawableRes = R.drawable.ic_breakfast,
        mealType = MealType.Breakfast
    ),
    Meal(
        name = UiText.StringResource(R.string.lunch),
        drawableRes = R.drawable.ic_lunch,
        mealType = MealType.Lunch
    ),
    Meal(
        name = UiText.StringResource(R.string.snacks),
        drawableRes = R.drawable.ic_snack,
        mealType = MealType.Snack
    ),
    Meal(
        name = UiText.StringResource(R.string.dinner),
        drawableRes = R.drawable.ic_dinner,
        mealType = MealType.Dinner
    )
)