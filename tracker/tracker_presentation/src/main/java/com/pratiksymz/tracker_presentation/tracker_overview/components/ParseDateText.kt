package com.pratiksymz.tracker_presentation.tracker_overview.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.pratiksymz.core.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun parseDateText(
    date: LocalDate
): String {
    val today = LocalDate.now()
    return when(date) {
        // Today
        today -> stringResource(id = R.string.today)
        // Previous day
        today.minusDays(1) -> stringResource(id = R.string.yesterday)
        // Next day
        today.plusDays(1) -> stringResource(id = R.string.tomorrow)
        // Any other day
        else -> DateTimeFormatter.ofPattern("LLLL dd").format(date) // <Full month> <date>
    }
}