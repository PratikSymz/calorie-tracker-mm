package com.pratiksymz.tracker_presentation.tracker_overview.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import com.pratiksymz.core_ui.CarbColor
import com.pratiksymz.core_ui.FatColor
import com.pratiksymz.core_ui.ProteinColor

@Composable
fun NutrientsBar(
    modifier: Modifier = Modifier,
    carbs: Int,
    protein: Int,
    fat: Int,
    calories: Int,
    caloriesGoal: Int
) {
    val background = MaterialTheme.colors.background
    val caloriesExceededColor = MaterialTheme.colors.error

    val cornerRadius = 75f

    // Ratios of nutrients filling the bar
    val carbWidthRatio = remember {
        Animatable(0f)
    }
    val proteinWidthRatio = remember {
        Animatable(0f)
    }
    val fatWidthRatio = remember {
        Animatable(0f)
    }

    // Animate the bar as soon as the composable is composed
    // 1. Animate when the 'carbs' state changes
    LaunchedEffect(key1 = carbs) {
        // 1g carbs = 4kcal
        carbWidthRatio.animateTo(
            targetValue = (carbs * 4f) / caloriesGoal
        )
    }
    // 2. Animate when the 'protein' state changes
    LaunchedEffect(key1 = protein) {
        // 1g protein = 4kcal
        proteinWidthRatio.animateTo(
            targetValue = (protein * 4f) / caloriesGoal
        )
    }
    // 3. Animate when the 'fat' state changes
    LaunchedEffect(key1 = fat) {
        // 1g carbs = 9kcal
        fatWidthRatio.animateTo(
            targetValue = (fat * 9f) / caloriesGoal
        )
    }

    // Round-ish rectangle with a full bar
    // "size" -> Size of the canvas (in px)
    Canvas(modifier = modifier) {
        if (calories <= caloriesGoal) {
            // Carbs bar width (in pixels)
            val carbsWidth = carbWidthRatio.value * size.width
            val proteinWidth = proteinWidthRatio.value * size.width
            val fatWidth = fatWidthRatio.value * size.width

            // Stack the rectangles
            // Background -> Fat -> Protein -> Carb [TOP]
            // The Background rectangle
            drawRoundRect(
                color = background,
                size = size,
                cornerRadius = CornerRadius(cornerRadius)
            )
            // The fat bar
            drawRoundRect(
                color = FatColor,
                size = Size(
                    width = fatWidth + proteinWidth + carbsWidth,
                    height = size.height
                ),
                cornerRadius = CornerRadius(cornerRadius)
            )
            // The protein bar
            drawRoundRect(
                color = ProteinColor,
                size = Size(
                    width = proteinWidth + carbsWidth,
                    height = size.height
                ),
                cornerRadius = CornerRadius(cornerRadius)
            )
            // The carb bar
            drawRoundRect(
                color = CarbColor,
                size = Size(
                    width = carbsWidth,
                    height = size.height
                ),
                cornerRadius = CornerRadius(cornerRadius)
            )
        } else {
            // Draw one RED rectangle spanning the entire width
            drawRoundRect(
                color = caloriesExceededColor,
                size = size,
                cornerRadius = CornerRadius(cornerRadius)
            )
        }
    }
}