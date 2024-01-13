package com.pratiksymz.tracker_presentation.tracker_overview.components.alt

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.pratiksymz.core_ui.SecondaryGreen

@Composable
fun NutrientsArc(
    modifier: Modifier = Modifier,
    carbs: Int,
    protein: Int,
    fat: Int,
    calories: Int,
    caloriesGoal: Int
) {
    val caloriesExceededColor = MaterialTheme.colors.error

    val caloriesWidthRatio = remember {
        Animatable(0f)
    }
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
            targetValue = if (caloriesGoal == 0) 0f else (carbs * 4f) / caloriesGoal,
            animationSpec = tween(durationMillis = 400)
        )
    }
    // 2. Animate when the 'protein' state changes
    LaunchedEffect(key1 = protein) {
        // 1g protein = 4kcal
        proteinWidthRatio.animateTo(
            targetValue = if (caloriesGoal == 0) 0f else (protein * 4f) / caloriesGoal,
            animationSpec = tween(durationMillis = 400)
        )
    }
    // 3. Animate when the 'fat' state changes
    LaunchedEffect(key1 = fat) {
        // 1g carbs = 9kcal
        fatWidthRatio.animateTo(
            targetValue = if (caloriesGoal == 0) 0f else (fat * 9f) / caloriesGoal,
            animationSpec = tween(durationMillis = 400)
        )
    }

    // Round-ish rectangle with a full bar
    // "size" -> Size of the canvas (in px)
    Canvas(
        modifier = modifier
            .clipToBounds()
            .padding(3.dp)
    ) {
        if (calories <= caloriesGoal) {
            // Carbs bar width (in pixels)
            val carbsAngle = carbWidthRatio.value * 180f
            val proteinAngle = proteinWidthRatio.value * 180f
            val fatAngle = fatWidthRatio.value * 180f

            // Stack the rectangles
            // Background -> Fat -> Protein -> Carb [TOP]
            // The Background rectangle
            drawArc(
                color = SecondaryGreen,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                size = Size(size.width, size.height * 2),
                style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = Color.White,
                startAngle = 180f,
                sweepAngle = carbsAngle + proteinAngle + fatAngle,
                useCenter = false,
                size = Size(size.width, size.height * 2),
                style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
            )
        } else {
            // Draw one RED rectangle spanning the entire width
            drawArc(
                color = caloriesExceededColor,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                size = Size(size.width, size.height * 2),
                style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
            )
        }
    }
}