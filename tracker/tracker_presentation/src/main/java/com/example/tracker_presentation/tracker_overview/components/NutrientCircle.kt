package com.example.tracker_presentation.tracker_overview.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.core.R
import com.example.core_ui.SecondaryGreen
import com.example.tracker_presentation.components.UnitDisplay

@Composable
fun NutrientCircle(
    modifier: Modifier = Modifier,
    value: Int,
    goal: Int,
    name: String,
    color: Color,
    backgroundColor: Color = SecondaryGreen,
    strokeWidth: Dp = 8.dp
) {
    val background = MaterialTheme.colors.background
    val primaryColor = MaterialTheme.colors.onPrimary
    val goalExceededColor = MaterialTheme.colors.error

    val angleRatio = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = value) {
        angleRatio.animateTo(
            targetValue = if (goal > 0) {
                value / goal.toFloat()
            } else 0f,
            animationSpec = tween(durationMillis = 400)
        )
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                // Circle -> Symmetrical
                .aspectRatio(1f)
        ) {
            // Draw Background
            drawArc(
                color = if (value <= goal) backgroundColor else goalExceededColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,   // Not connected to center
                size = size,
                style = Stroke(
                    width = strokeWidth.toPx(),
                    cap = StrokeCap.Round   // Corners of stroke are Rounded
                )
            )

            // Draw actual value
            if (value <= goal) {
                drawArc(
                    color = color,
                    startAngle = 270f,
                    sweepAngle = 360f * angleRatio.value,
                    useCenter = false,
                    size = size,
                    style = Stroke(
                        width = strokeWidth.toPx(),
                        cap = StrokeCap.Round
                    )
                )
            }
        }

        // Column for the values in the center
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UnitDisplay(
                amount = value,
                amountColor = if (value <= goal) primaryColor else goalExceededColor,
                unit = stringResource(id = R.string.grams),
                unitTextColor = if (value <= goal) primaryColor else goalExceededColor
            )
            Text(
                text = name,
                color = if (value <= goal) primaryColor else goalExceededColor,
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Light
            )
        }
    }
}