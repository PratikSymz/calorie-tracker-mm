package com.pratiksymz.onboarding_presentation.nutrient_goal

/**
 * Sealed class for all the events that can occur on the Nutrient Goal screen
 * Such as on entering the carb, protein and fat ratio and
 * clicking NEXT, we need to check whether it all adds up to 100
 */
sealed class NutrientGoalEvent {
    data class OnCarbRatioEnter(val ratio: String) : NutrientGoalEvent()
    data class OnProteinRatioEnter(val ratio: String) : NutrientGoalEvent()
    data class OnFatRatioEnter(val ratio: String) : NutrientGoalEvent()
    object OnNextClick : NutrientGoalEvent()
}
