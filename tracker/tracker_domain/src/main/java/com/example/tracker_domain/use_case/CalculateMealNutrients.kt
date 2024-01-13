package com.example.tracker_domain.use_case

import com.example.core.domain.models.ActivityLevel
import com.example.core.domain.models.Gender
import com.example.core.domain.models.GoalType
import com.example.core.domain.models.UserInfo
import com.example.core.domain.preferences.Preferences
import com.example.tracker_domain.model.MealType
import com.example.tracker_domain.model.TrackedFood
import kotlin.math.roundToInt

class CalculateMealNutrients(
    private val preferences: Preferences
) {
    /**
     * @param trackedFoods List of tracked foods for a given date
     * @return the calculation of all the goals and the nutrients consumed
     */
    operator fun invoke(trackedFoods: List<TrackedFood>): Result {
        // Group all the tracked foods by their distinct meal type
        // Create a map of all nutrients consumed throughout the day for each meal type
        val allNutrients = trackedFoods
            .groupBy { it.mealType }    // Map<MealType, List<TrackedFood>>
            .mapValues { entry ->       // Map<MealType, MealNutrients>
                val mealType = entry.key
                val foods = entry.value

                MealNutrients(
                    carbs = foods.sumOf { it.carbs },
                    protein = foods.sumOf { it.protein },
                    fat = foods.sumOf { it.fat },
                    calories = foods.sumOf { it.calories },
                    mealType = mealType
                )
            }

        // Total nutrients actually consumed throughout the day
        val totalCarbs = allNutrients.values.sumOf { it.carbs }
        val totalProtein = allNutrients.values.sumOf { it.protein }
        val totalFat = allNutrients.values.sumOf { it.fat }
        val totalCalories = allNutrients.values.sumOf { it.calories }

        // Nutrient goals for the day
        val userInfo = preferences.loadUserInfo()
        val caloriesGoal = calculateDailyCalorieRequirement(userInfo)
        val carbsGoal = (caloriesGoal * userInfo.carbRatio / 4f).roundToInt()    // 1g carbs = 4 Kcal
        val proteinGoal = (caloriesGoal * userInfo.proteinRatio / 4f).roundToInt()    // 1g protein = 4 Kcal
        val fatGoal = (caloriesGoal * userInfo.fatRatio / 9f).roundToInt()    // 1g fat = 9 Kcal

        return Result(
            carbsGoal = carbsGoal,
            proteinGoal = proteinGoal,
            fatGoal = fatGoal,
            caloriesGoal = caloriesGoal,
            totalCarbs = totalCarbs,
            totalProtein = totalProtein,
            totalFat = totalFat,
            totalCalories = totalCalories,
            mealNutrients = allNutrients
        )
    }

    /**
     * Calculate the Basal Metabolism Rate (BMR) using the UserInfo.
     * This represents how many calories someone burns throughout the entire day without any movement.
     */
    private fun calculateBMR(userInfo: UserInfo): Int {
        return when (userInfo.gender) {
            is Gender.Male -> {
                (66.47f + 13.75f * userInfo.weight
                        + 5f * userInfo.height - 6.75f * userInfo.age).roundToInt()
            }

            is Gender.Female -> {
                (665.09f + 9.56f * userInfo.weight
                        + 1.84f * userInfo.height - 4.67 * userInfo.age).roundToInt()
            }
        }
    }

    /**
     * Calculate the daily calorie requirement taking into factor the activity level (low, medium or high)
     * and the goal type (lose, keep or gain weight).
     */
    private fun calculateDailyCalorieRequirement(userInfo: UserInfo): Int {
        val activityFactor = when (userInfo.activityLevel) {
            is ActivityLevel.Low -> 1.2f
            is ActivityLevel.Medium -> 1.3f
            is ActivityLevel.High -> 1.4f
        }

        val calorieFactor = when (userInfo.goalType) {
            is GoalType.LoseWeight -> -500
            is GoalType.KeepWeight -> 0
            is GoalType.GainWeight -> 500
        }

        return (calculateBMR(userInfo) * activityFactor + calorieFactor).roundToInt()
    }

    /*
        Data class to store the nutrients for 1 specific meal
     */
    data class MealNutrients(
        val carbs: Int,
        val protein: Int,
        val fat: Int,
        val calories: Int,
        val mealType: MealType
    )

    /*
        Data class to store the 4 goal values and the actual amount of each nutrient we ate per day
     */
    data class Result(
        // Nutrient Goal for each day
        val carbsGoal: Int,
        val proteinGoal: Int,
        val fatGoal: Int,
        val caloriesGoal: Int,
        // Actual consumption on each day
        val totalCarbs: Int,
        val totalProtein: Int,
        val totalFat: Int,
        val totalCalories: Int,
        // Mapping of the meal type to the nutrients of each meal
        val mealNutrients: Map<MealType, MealNutrients>
    )
}