package com.pratiksymz.calorytracker

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.common.truth.Truth.assertThat
import com.pratiksymz.calorytracker.navigation.Route
import com.pratiksymz.calorytracker.repository.MockTrackerRepository
import com.pratiksymz.calorytracker.ui.theme.CaloryTrackerTheme
import com.pratiksymz.core.domain.models.ActivityLevel
import com.pratiksymz.core.domain.models.Gender
import com.pratiksymz.core.domain.models.GoalType
import com.pratiksymz.core.domain.models.UserInfo
import com.pratiksymz.core.domain.preferences.Preferences
import com.pratiksymz.core.domain.use_case.FilterOutDigits
import com.pratiksymz.tracker_domain.model.TrackableFood
import com.pratiksymz.tracker_domain.use_case.CalculateMealNutrients
import com.pratiksymz.tracker_domain.use_case.DeleteTrackedFood
import com.pratiksymz.tracker_domain.use_case.GetFoodsForDate
import com.pratiksymz.tracker_domain.use_case.SearchFood
import com.pratiksymz.tracker_domain.use_case.TrackFood
import com.pratiksymz.tracker_domain.use_case.TrackerUseCases
import com.pratiksymz.tracker_presentation.search.SearchScreen
import com.pratiksymz.tracker_presentation.search.SearchViewModel
import com.pratiksymz.tracker_presentation.tracker_overview.TrackerOverviewScreen
import com.pratiksymz.tracker_presentation.tracker_overview.TrackerOverviewViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.math.roundToInt

@HiltAndroidTest
class TrackerOverviewE2E {

    /*
        Rules: Provides Android features for testing
     */
    // To inject dependencies in this test class using Dagger-Hilt
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()  // @EntryPoint

    private lateinit var mockRepository: MockTrackerRepository
    private lateinit var trackerUseCases: TrackerUseCases
    private lateinit var preferences: Preferences

    private lateinit var trackerOverviewViewModel: TrackerOverviewViewModel
    private lateinit var searchViewModel: SearchViewModel

    private lateinit var navController: NavHostController

    @Before
    fun setUp() {
        preferences = mockk(relaxed = true)
        every { preferences.loadUserInfo() } returns UserInfo(
            gender = Gender.Male,
            age = 20,
            weight = 80f,
            height = 180,
            activityLevel = ActivityLevel.Medium,
            goalType = GoalType.KeepWeight,
            carbRatio = 0.4f,
            proteinRatio = 0.3f,
            fatRatio = 0.3f
        )

        mockRepository = MockTrackerRepository()
        trackerUseCases = TrackerUseCases(
            trackFood = TrackFood(mockRepository),
            searchFood = SearchFood(mockRepository),
            getFoodsForDate = GetFoodsForDate(mockRepository),
            deleteTrackedFood = DeleteTrackedFood(mockRepository),
            calculateMealNutrients = CalculateMealNutrients(preferences)
        )

        trackerOverviewViewModel = TrackerOverviewViewModel(
            preferences = preferences,
            trackerUseCases = trackerUseCases
        )

        searchViewModel = SearchViewModel(
            trackerUseCases = trackerUseCases,
            filterOutDigits = FilterOutDigits()
        )

        // Set up the test screen
        composeRule.activity.setContent {
            CaloryTrackerTheme {
                navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    scaffoldState = scaffoldState
                ) {
                    NavHost(
                        modifier = Modifier.padding(it),
                        navController = navController,
                        startDestination = Route.TRACKER_OVERVIEW
                    ) {
                        /* Tracker navigation */
                        composable(Route.TRACKER_OVERVIEW) {
                            TrackerOverviewScreen(
                                onNavigateToSearch = { mealName, dayOfMonth, month, year ->
                                    val route = Route.SEARCH + "/$mealName" + "/$dayOfMonth" + "/$month" + "/$year"
                                    navController.navigate(route)
                                },
                                viewModel = trackerOverviewViewModel
                            )
                        }
                        composable(
                            route = Route.SEARCH + "/{mealName}/{dayOfMonth}/{month}/{year}",
                            arguments = listOf(
                                navArgument("mealName") { type = NavType.StringType },
                                navArgument("dayOfMonth") { type = NavType.IntType },
                                navArgument("month") { type = NavType.IntType },
                                navArgument("year") { type = NavType.IntType }
                            )
                        ) {
                            val mealName = it.arguments?.getString("mealName")!!
                            val dayOfMonth = it.arguments?.getInt("dayOfMonth")!!
                            val month = it.arguments?.getInt("month")!!
                            val year = it.arguments?.getInt("year")!!
                            SearchScreen(
                                mealName = mealName,
                                dayOfMonth = dayOfMonth,
                                month = month,
                                year = year,
                                scaffoldState = scaffoldState,
                                onNavigateUp = { navController.navigateUp() },
                                viewModel = searchViewModel
                            )
                        }
                    }
                }
            }
        }
    }

    @Test
    fun addBreakfast_appearsUnderBreakfast_nutrientsProperlyCalculated() {
        mockRepository.searchResult = listOf(
            TrackableFood(
                name = "banana",
                imageUrl = null,
                caloriesPer100g = 150,
                carbsPer100g = 5,
                proteinPer100g = 50,
                fatPer100g = 1
            )
        )

        val addedAmountInGrams = 150
        val expectedCalories = ((1.5f) * 150).roundToInt()  // 150 gms * 150 kcal per gm
        val expectedCarbs = ((1.5f) * 5).roundToInt()
        val expectedProtein = ((1.5f) * 50).roundToInt()
        val expectedFat = ((1.5f) * 1).roundToInt()

        // Test toggling of Breakfast meal to show the Add Breakfast icon
        composeRule
            // Node => Composable (Button)
            .onNodeWithText("Add Breakfast")
            // Make sure that it doesn't exist
            .assertDoesNotExist()

        // Find the breakfast section to toggle it
        // Content description of breakfast image
        composeRule
            .onNodeWithContentDescription("Breakfast")
            .performClick()

        // After toggling that section, we need to make sure that the add breakfast button is visible
        composeRule
            .onNodeWithText("Add Breakfast")
            .assertIsDisplayed()

        // If displayed -> Click it
        composeRule
            .onNodeWithText("Add Breakfast")
            .performClick()

        // Nav to search screen
        assertThat(
            navController
                .currentDestination
                ?.route
                ?.startsWith(Route.SEARCH)
        ).isTrue()

        // Type something in search bar
        composeRule
            .onNodeWithTag("search_textfield")
            .performTextInput("banana")

        // Click search button to perform search
        composeRule
            .onNodeWithContentDescription("Search...")
            .performClick()

        // Get the mock search result using any text field within that composable
        composeRule
            .onNodeWithText("Carbs")
            .performClick()

        // Enter an amount -> Content desc on non images -> .semantics {}
        composeRule
            .onNodeWithContentDescription("Amount")
            .performTextInput(addedAmountInGrams.toString())

        // Click the checkmark icon
        composeRule
            .onNodeWithContentDescription("Track")
            .performClick()

        // Make sure nav to Tracker Overview
        assertThat(
            navController
                .currentDestination
                ?.route
                ?.startsWith(Route.TRACKER_OVERVIEW)
        ).isTrue()

        // Make sure that the overview shows the correct carbs or p or f
        composeRule
            .onAllNodesWithText(expectedCalories.toString())
            // Some text that contains the amount of carbs tracked
            .onFirst()
            .assertIsDisplayed()

        composeRule
            .onAllNodesWithText(expectedCarbs.toString())
            .onFirst()
            .assertIsDisplayed()

        composeRule
            .onAllNodesWithText(expectedProtein.toString())
            .onFirst()
            .assertIsDisplayed()

        composeRule
            .onAllNodesWithText(expectedFat.toString())
            .onFirst()
            .assertIsDisplayed()
    }
}