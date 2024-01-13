package com.pratiksymz.tracker_presentation.tracker_overview.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pratiksymz.core.R
import com.pratiksymz.core_ui.LocalSpacing
import com.pratiksymz.tracker_presentation.components.NutrientInfo
import com.pratiksymz.tracker_presentation.components.UnitDisplay
import com.pratiksymz.tracker_presentation.tracker_overview.Meal

@Composable
fun ExpandableMealItem(
    modifier: Modifier = Modifier,
    meal: Meal,
    onToggleExpand: () -> Unit,
    expandedContent: @Composable () -> Unit
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    // Column for expanded view of list items
    Column(
        modifier = modifier
    ) {
        // The primary list item
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleExpand() }
                .padding(spacing.spaceMedium)
                .clip(
                    RoundedCornerShape(5.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // The meal image
            Image(
                modifier = Modifier,
                painter = painterResource(id = meal.drawableRes),
                contentDescription = meal.name.asString(context)
            )
            Spacer(modifier = Modifier.width(spacing.spaceMedium))
            // Column of 2 rows:
            Column(
                modifier = Modifier.weight(1f)  // Fill up rest of the space
            ) {
                // 1. Meal name and drop down icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = meal.name.asString(context),
                        style = MaterialTheme.typography.h3
                    )
                    Icon(
                        imageVector = if (meal.isExpanded)
                            Icons.Default.KeyboardArrowUp
                        else Icons.Default.KeyboardArrowDown,

                        contentDescription = if (meal.isExpanded)
                            stringResource(id = R.string.collapse)
                        else stringResource(id = R.string.extend)
                    )
                }
                Spacer(modifier = Modifier.height(spacing.spaceSmall))
                // 2. Total calories and the Nutrient info - C, P and F
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    UnitDisplay(
                        amount = meal.calories,
                        unit = stringResource(id = R.string.kcal),
                        amountTextSize = 30.sp
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(spacing.spaceSmall)
                    ) {
                       NutrientInfo(
                           nutrientName = stringResource(id = R.string.carbs),
                           amount = meal.carbs,
                           unit = stringResource(id = R.string.grams)
                       )
                        NutrientInfo(
                            nutrientName = stringResource(id = R.string.protein),
                            amount = meal.protein,
                            unit = stringResource(id = R.string.grams)
                        )
                        NutrientInfo(
                            nutrientName = stringResource(id = R.string.fat),
                            amount = meal.fat,
                            unit = stringResource(id = R.string.grams)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(spacing.spaceMedium))
        // The expanded list items
        // Default animation is expand fade and collapse fade
        AnimatedVisibility(visible = meal.isExpanded) {
            expandedContent()
        }
    }
}