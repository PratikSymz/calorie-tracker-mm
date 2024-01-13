package com.pratiksymz.tracker_presentation.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.pratiksymz.core.R
import com.pratiksymz.core_ui.LocalSpacing

@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    text: String,
    hint: String = stringResource(id = R.string.search),
    shouldShowHint: Boolean = false,
    onFocusChanged: (FocusState) -> Unit,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    val spacing = LocalSpacing.current
    // Box -> Easy to align the search icon and the hint
    Box(
        modifier = modifier
    ) {
        BasicTextField(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .padding(2.dp)  // Padding to display shadow
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(5.dp)
                )
                .background(MaterialTheme.colors.surface)
                .fillMaxWidth()
                .padding(spacing.spaceMedium)  // Actual padding
                .padding(end = spacing.spaceMedium) // To prevent search icon and text overlap
                .onFocusChanged { onFocusChanged(it) }
                .testTag("search_textfield"),
            value = text,
            onValueChange = onValueChange,
            singleLine = true,
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch()
                    // Execute default behavior of clicking the search button on the keyboard
                    // -> Close keyboard
                    defaultKeyboardAction(ImeAction.Search)
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            )
        )

        // Hint
        if (shouldShowHint) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = spacing.spaceMedium),
                text = hint,
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Light,
                color = Color.LightGray,

                )
        }

        // Search button
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterEnd),
            onClick = onSearch
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(id = R.string.search)
            )
        }
    }
}