package com.pratiksymz.tracker_presentation.tracker_overview

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratiksymz.core.domain.preferences.Preferences
import com.pratiksymz.core.util.UiEvent
import com.pratiksymz.tracker_domain.use_case.TrackerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackerOverviewViewModel @Inject constructor(
    private val preferences: Preferences,
    private val trackerUseCases: TrackerUseCases
) : ViewModel() {

    var state by mutableStateOf(TrackerOverviewState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    // Coroutine job
    var getFoodsForDateJob: Job? = null

    init {
        refreshOverviewState()
        preferences.saveShouldShowOnboarding(false)
    }

    fun onEvent(event: TrackerOverviewEvent) {
        when (event) {
            is TrackerOverviewEvent.OnPreviousDayClick -> {
                state = state.copy(
                    date = state.date.minusDays(1)
                )
                refreshOverviewState()
            }

            is TrackerOverviewEvent.OnNextDayClick -> {
                state = state.copy(
                    date = state.date.plusDays(1)
                )
                refreshOverviewState()
            }

            is TrackerOverviewEvent.OnToggleMealClick -> {
                // Get the meal state and update the 'isExpanded' field
                state = state.copy(
                    meals = state.meals.map {
                        if (it.name == event.meal.name) {
                            it.copy(isExpanded = !it.isExpanded)
                        } else it
                    }
                )
            }

//            is TrackerOverviewEvent.OnAddFoodClick -> {
//                // 1. MealType
//                // 2. Current date
//                viewModelScope.launch {
//                    _uiEvent.send(UiEvent.Success)
////                    route = Route.SEARCH
////                        +"/${event.meal.mealType.name}"
////                        +"/${state.date.dayOfMonth}"
////                        +"/${state.date.monthValue}"
////                        +"/${state.date.year}"
//                }
//
//            }

            is TrackerOverviewEvent.OnDeleteTrackedFoodClick -> {
                viewModelScope.launch {
                    // Delete the tracked food
                    trackerUseCases.deleteTrackedFood(event.trackedFood)
                    // Refresh TrackerOverviewState since a tracked food has now been removed
                    refreshOverviewState()
                }
            }
        }
    }

    private fun refreshOverviewState() {
        // Cancel the current job if running
        getFoodsForDateJob?.cancel()
        // Reassign and launch the new job
        getFoodsForDateJob = trackerUseCases
            .getFoodsForDate(state.date)
            // On each emission -> Foods list, we want to re-calculate
            .onEach { foods ->
                val nutrientsResult = trackerUseCases
                    .calculateMealNutrients(foods)
                Log.d("GOAL", nutrientsResult.caloriesGoal.toString())

                state = state.copy(
                    totalCarbs = nutrientsResult.totalCarbs,
                    totalProtein = nutrientsResult.totalProtein,
                    totalFat = nutrientsResult.totalFat,
                    totalCalories = nutrientsResult.totalCalories,
                    // TODO: Check if the goals also need to be updated
                    carbsGoal = nutrientsResult.carbsGoal,
                    proteinGoal = nutrientsResult.proteinGoal,
                    fatGoal = nutrientsResult.fatGoal,
                    caloriesGoal = nutrientsResult.caloriesGoal,
                    trackedFoods = foods,
                    // Update the default meals by updating the nutrients for all tracked meals within each meal type
                    meals = state.meals.map { meal ->
                        val nutrientsForMeal = nutrientsResult.mealNutrients[meal.mealType]
                        // If nutrients for the meal are somehow NULL, return a new meal with 0 nutrients
                            ?: return@map meal.copy(
                                carbs = 0,
                                protein = 0,
                                fat = 0,
                                calories = 0
                            )

                        meal.copy(
                            carbs = nutrientsForMeal.carbs,
                            protein = nutrientsForMeal.protein,
                            fat = nutrientsForMeal.fat,
                            calories = nutrientsForMeal.calories
                        )
                    }
                )
            }
            .launchIn(viewModelScope)
    }
}