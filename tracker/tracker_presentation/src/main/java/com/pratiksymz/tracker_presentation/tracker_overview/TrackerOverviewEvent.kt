package com.pratiksymz.tracker_presentation.tracker_overview

import com.pratiksymz.tracker_domain.model.TrackedFood

sealed class TrackerOverviewEvent {
    object OnPreviousDayClick : TrackerOverviewEvent()
    object OnNextDayClick : TrackerOverviewEvent()
    data class OnToggleMealClick(val meal: Meal) : TrackerOverviewEvent()

//    data class OnAddFoodClick(val meal: Meal) : TrackerOverviewEvent()
    data class OnDeleteTrackedFoodClick(val trackedFood: TrackedFood) : TrackerOverviewEvent()
}