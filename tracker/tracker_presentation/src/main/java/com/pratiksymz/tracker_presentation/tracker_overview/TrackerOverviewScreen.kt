package com.pratiksymz.tracker_presentation.tracker_overview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.pratiksymz.core.R
import com.pratiksymz.core_ui.LocalSpacing
import com.pratiksymz.tracker_presentation.tracker_overview.components.AddButton
import com.pratiksymz.tracker_presentation.tracker_overview.components.DaySelector
import com.pratiksymz.tracker_presentation.tracker_overview.components.ExpandableMealItem
import com.pratiksymz.tracker_presentation.tracker_overview.components.NutrientsHeader
import com.pratiksymz.tracker_presentation.tracker_overview.components.TrackedFoodItem

@Composable
fun TrackerOverviewScreen(
    onNavigateToSearch: (String, Int, Int, Int) -> Unit,
    viewModel: TrackerOverviewViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val context = LocalContext.current

//    LaunchedEffect(key1 = true) {
//        viewModel.uiEvent.collect { event ->
//            when (event) {
//                is UiEvent.Success -> onNavigateToSearch(
//
//                )
//                else -> Unit
//            }
//        }
//    }

    // Scrollable overview screen
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = spacing.spaceMedium)
    ) {
        // The top section
        item {
            // Header Item
            NutrientsHeader(state = state)
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            // Date selector
            DaySelector(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.spaceMedium),
                date = state.date,
                onPreviousDayClick = {
                    viewModel.onEvent(TrackerOverviewEvent.OnPreviousDayClick)
                }, onNextDayClick = {
                    viewModel.onEvent(TrackerOverviewEvent.OnNextDayClick)
                }
            )
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
        }

        // The meal section
        items(state.meals) { meal ->
            ExpandableMealItem(
                modifier = Modifier.fillMaxWidth(),
                meal = meal,
                onToggleExpand = {
                    viewModel.onEvent(TrackerOverviewEvent.OnToggleMealClick(meal))
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = spacing.spaceSmall)
                ) {
                    state.trackedFoods
                        .filter { it.mealType == meal.mealType }
                        .forEach { trackedFood ->
                            TrackedFoodItem(
                                trackedFood = trackedFood,
                                onDeleteItem = {
                                    viewModel.onEvent(
                                        TrackerOverviewEvent.OnDeleteTrackedFoodClick(trackedFood)
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.height(spacing.spaceMedium))
                        }
                    AddButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(
                            id = R.string.add_meal,
                            meal.name.asString(context)
                        ),
                        onClick = {
                            onNavigateToSearch(
                                meal.name.asString(context),
                                state.date.dayOfMonth,
                                state.date.monthValue,
                                state.date.year
                            )
                            // viewModel.onEvent(TrackerOverviewEvent.OnAddFoodClick(meal))
                        }
                    )
                }
            }
        }
    }
}