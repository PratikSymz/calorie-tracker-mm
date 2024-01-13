package com.example.tracker_domain.use_case

import com.example.core.domain.models.ActivityLevel
import com.example.core.domain.models.Gender
import com.example.core.domain.models.GoalType
import com.example.core.domain.models.UserInfo
import com.example.core.domain.preferences.Preferences
import com.example.tracker_domain.model.MealType
import com.example.tracker_domain.model.TrackedFood
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import kotlin.random.Random

class CalculateMealNutrientsTest {
    private lateinit var calculateMealNutrients: CalculateMealNutrients

    /*
        Setup function: Code that runs before every testcase in this class
     */
    @Before
    fun setup() {
        // Set up the mock preferences
        val preferences = mockk<Preferences>(relaxed = true)
        // We need the user info for this use case
        // For every call of "loadUserInfo" return a specific user object
        every { preferences.loadUserInfo() } returns UserInfo(
            gender = Gender.Female,
            age = 27,
            weight = 80f,
            height = 182,
            activityLevel = ActivityLevel.High,
            goalType = GoalType.KeepWeight,
            carbRatio = 0.4f,   // 40%
            proteinRatio = 0.4f,
            fatRatio = 0.2f
        )
        calculateMealNutrients = CalculateMealNutrients(preferences)
    }

    // `` -> Function name in natural words
    // NOT RECOMMENDED for actual projects only testing
    @Test
    fun `Calories for Breakfast properly calculated`() {
        val trackedFoods = (1..30).map {
            TrackedFood(
                name = "name",
                carbs = Random.nextInt(100),
                protein = Random.nextInt(100),
                fat = Random.nextInt(100),
                mealType = MealType.fromString(
                    listOf("breakfast", "lunch", "dinner", "snack").random()
                ),
                imageUrl = null,
                amount = 100,
                date = LocalDate.now(),
                calories = Random.nextInt(2000)
            )
        }

        val result = calculateMealNutrients(trackedFoods)
        val breakfastCalories = result.mealNutrients.values
            .filter { it.mealType is MealType.Breakfast }
            .sumOf { it.calories }

        val expectedCalories = trackedFoods
            .filter { it.mealType is MealType.Breakfast }
            .sumOf { it.calories }

        assertThat(breakfastCalories).isEqualTo(expectedCalories)
    }

    @Test
    fun `Calories for Lunch properly calculated`() {
        val trackedFoods = (1..30).map {
            TrackedFood(
                name = "name",
                carbs = Random.nextInt(100),
                protein = Random.nextInt(100),
                fat = Random.nextInt(100),
                mealType = MealType.fromString(
                    listOf("breakfast", "lunch", "dinner", "snack").random()
                ),
                imageUrl = null,
                amount = 100,
                date = LocalDate.now(),
                calories = Random.nextInt(2000)
            )
        }

        val result = calculateMealNutrients(trackedFoods)
        val lunchCalories = result.mealNutrients.values
            .filter { it.mealType is MealType.Lunch }
            .sumOf { it.calories }

        val expectedCalories = trackedFoods
            .filter { it.mealType is MealType.Lunch }
            .sumOf { it.calories }

        assertThat(lunchCalories).isEqualTo(expectedCalories)
    }

    @Test
    fun `Calories for Snack properly calculated`() {
        val trackedFoods = (1..30).map {
            TrackedFood(
                name = "name",
                carbs = Random.nextInt(100),
                protein = Random.nextInt(100),
                fat = Random.nextInt(100),
                mealType = MealType.fromString(
                    listOf("breakfast", "lunch", "dinner", "snack").random()
                ),
                imageUrl = null,
                amount = 100,
                date = LocalDate.now(),
                calories = Random.nextInt(2000)
            )
        }

        val result = calculateMealNutrients(trackedFoods)
        val snackCalories = result.mealNutrients.values
            .filter { it.mealType is MealType.Snack }
            .sumOf { it.calories }

        val expectedCalories = trackedFoods
            .filter { it.mealType is MealType.Snack }
            .sumOf { it.calories }

        assertThat(snackCalories).isEqualTo(expectedCalories)
    }

    @Test
    fun `Calories for Dinner properly calculated`() {
        val trackedFoods = (1..30).map {
            TrackedFood(
                name = "name",
                carbs = Random.nextInt(100),
                protein = Random.nextInt(100),
                fat = Random.nextInt(100),
                mealType = MealType.fromString(
                    listOf("breakfast", "lunch", "dinner", "snack").random()
                ),
                imageUrl = null,
                amount = 100,
                date = LocalDate.now(),
                calories = Random.nextInt(2000)
            )
        }

        val result = calculateMealNutrients(trackedFoods)
        val dinnerCalories = result.mealNutrients.values
            .filter { it.mealType is MealType.Dinner }
            .sumOf { it.calories }

        val expectedCalories = trackedFoods
            .filter { it.mealType is MealType.Dinner }
            .sumOf { it.calories }

        assertThat(dinnerCalories).isEqualTo(expectedCalories)
    }
}