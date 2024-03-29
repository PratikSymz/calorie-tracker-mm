package com.pratiksymz.tracker_presentation.tracker_overview.components.alt

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pratiksymz.core.R
import com.pratiksymz.core_ui.CarbColor
import com.pratiksymz.core_ui.FatColor
import com.pratiksymz.core_ui.LocalSpacing
import com.pratiksymz.core_ui.ProteinColor
import com.pratiksymz.tracker_presentation.components.UnitDisplay
import com.pratiksymz.tracker_presentation.tracker_overview.TrackerOverviewState
import com.pratiksymz.tracker_presentation.tracker_overview.components.NutrientCircle
import com.pratiksymz.tracker_presentation.tracker_overview.components.NutrientsBar

@Composable
fun NutrientsHeaderNew(
    modifier: Modifier = Modifier,
    state: TrackerOverviewState
) {
    val spacing = LocalSpacing.current

    val animatedCaloriesCount = animateIntAsState(
        targetValue = state.totalCalories, label = ""
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    bottomStart = 50.dp,
                    bottomEnd = 50.dp
                )
            )
            .background(MaterialTheme.colors.primary)
            .padding(
                horizontal = spacing.spaceLarge,
                vertical = spacing.spaceExtraLarge
            )
    ) {
        // Row for the calories consumed and the calories goal
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            // Calories consumed
            UnitDisplay(
                amount = animatedCaloriesCount.value,
                unit = stringResource(id = R.string.kcal),
                amountColor = MaterialTheme.colors.onPrimary,
                amountTextSize = 40.sp,
                unitTextColor = MaterialTheme.colors.onPrimary
            )
            // Calories Goal
            Column {
                Text(
                    text = stringResource(id = R.string.your_goal),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onPrimary
                )
                UnitDisplay(
                    amount = state.caloriesGoal,
                    unit = stringResource(id = R.string.kcal),
                    amountColor = MaterialTheme.colors.onPrimary,
                    amountTextSize = 40.sp,
                    unitTextColor = MaterialTheme.colors.onPrimary
                )
            }
        }
        
        Spacer(modifier = Modifier.height(spacing.spaceSmall))

        // The calories consumed progress bar
        NutrientsBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            carbs = state.totalCarbs,
            protein = state.totalProtein,
            fat = state.totalFat,
            calories = state.totalCalories,
            caloriesGoal = state.caloriesGoal
        )

        Spacer(modifier = Modifier.height(spacing.spaceLarge))

        // Row for the nutrients circles denoting the amount of carbs, proteins and fats consumed
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NutrientCircle(
                modifier = Modifier.size(90.dp),
                value = state.totalCarbs,
                goal = state.carbsGoal,
                name = stringResource(id = R.string.carbs),
                color = CarbColor,
                backgroundColor = CarbColor.copy(alpha = 0.4f)
            )
            NutrientCircle(
                modifier = Modifier.size(90.dp),
                value = state.totalProtein,
                goal = state.proteinGoal,
                name = stringResource(id = R.string.protein),
                color = ProteinColor,
                backgroundColor = ProteinColor.copy(alpha = 0.4f)
            )
            NutrientCircle(
                modifier = Modifier.size(90.dp),
                value = state.totalFat,
                goal = state.fatGoal,
                name = stringResource(id = R.string.fat),
                color = FatColor,
                backgroundColor = FatColor.copy(alpha = 0.4f)
            )
        }
    }
}