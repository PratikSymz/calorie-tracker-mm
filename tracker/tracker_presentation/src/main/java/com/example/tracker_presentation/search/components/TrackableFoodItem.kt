package com.example.tracker_presentation.search.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.core.R
import com.example.core_ui.LocalSpacing
import com.example.tracker_presentation.components.NutrientInfo
import com.example.tracker_presentation.search.TrackableFoodUiState

@OptIn(ExperimentalCoilApi::class)
@Composable
fun TrackableFoodItem(
    modifier: Modifier = Modifier,
    trackableFoodUiState: TrackableFoodUiState,
    onClickItem: () -> Unit,
    onAmountChange: (String) -> Unit,
    onTrackFood: () -> Unit
) {
    val food = trackableFoodUiState.food
    val spacing = LocalSpacing.current

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(5.dp))
            .padding(spacing.spaceExtraSmall)   // For shadow
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(5.dp)
            )
            .background(MaterialTheme.colors.surface)
            .clickable { onClickItem() }
            .padding(end = spacing.spaceMedium)
    ) {
        // Visible food info
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Row for Trackable Food image and nutrient info
            Row(
                modifier = Modifier
                    .weight(1f)
            ) {
                // Food image
                Image(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 5.dp
                            )
                        ),
                    painter = rememberImagePainter(
                        data = food.imageUrl,
                        builder = {
                            crossfade(true)
                            error(R.drawable.ic_lunch)
                            fallback(R.drawable.ic_lunch)
                        }
                    ),
                    contentDescription = food.name,
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(spacing.spaceMedium))
                // Food nutrition info
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = food.name,
                        style = MaterialTheme.typography.body1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(spacing.spaceSmall))
                    Text(
                        text = stringResource(
                            id = R.string.kcal_per_100g,
                            food.caloriesPer100g
                        ),
                        style = MaterialTheme.typography.body2
                    )
                }
            }

            // Row for C, P and F info of Trackable Food
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.spaceSmall)
            ) {
                NutrientInfo(
                    nutrientName = stringResource(id = R.string.carbs),
                    amount = food.carbsPer100g,
                    unit = stringResource(id = R.string.grams),
                    amountTextSize = 16.sp,
                    unitTextSize = 12.sp,
                    nameTextStyle = MaterialTheme.typography.body2
                )
                NutrientInfo(
                    nutrientName = stringResource(id = R.string.protein),
                    amount = food.proteinPer100g,
                    unit = stringResource(id = R.string.grams),
                    amountTextSize = 16.sp,
                    unitTextSize = 12.sp,
                    nameTextStyle = MaterialTheme.typography.body2
                )
                NutrientInfo(
                    nutrientName = stringResource(id = R.string.fat),
                    amount = food.fatPer100g,
                    unit = stringResource(id = R.string.grams),
                    amountTextSize = 16.sp,
                    unitTextSize = 12.sp,
                    nameTextStyle = MaterialTheme.typography.body2
                )
            }
        }

        // Expandable food info
        AnimatedVisibility(
            visible = trackableFoodUiState.isExpanded
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.spaceMedium),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Row for amount text field and unit
                Row {
                    BasicTextField(
                        modifier = Modifier
                            .border(
                                shape = RoundedCornerShape(5.dp),
                                width = 0.5.dp,
                                color = MaterialTheme.colors.onSurface
                            )
                            .alignBy(LastBaseline)
                            .padding(spacing.spaceMedium)
                            .semantics {
                                contentDescription = "Amount"
                            },
                        value = trackableFoodUiState.amount,
                        onValueChange = onAmountChange,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = if (trackableFoodUiState.amount.isNotBlank()) {
                                ImeAction.Done
                            } else ImeAction.Default,
                            keyboardType = KeyboardType.Number
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                onTrackFood()
                                defaultKeyboardAction(ImeAction.Done)
                            }
                        )
                    )
                    Spacer(modifier = Modifier.width(spacing.spaceExtraSmall))
                    Text(
                        modifier = Modifier
                            .alignBy(LastBaseline),
                        text = stringResource(id = R.string.grams),
                        style = MaterialTheme.typography.body1
                    )
                }

                // Track food button
                IconButton(
                    onClick = onTrackFood,
                    enabled = trackableFoodUiState.amount.isNotBlank()
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(id = R.string.track)
                    )
                }
            }
        }
    }
}