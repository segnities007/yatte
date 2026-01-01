package com.segnities007.yatte.presentation.designsystem.component.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

@Composable
fun YatteStickyDateHeader(
    date: LocalDate,
    modifier: Modifier = Modifier,
) {
    val now = Clock.System.now()
    val today = now.toLocalDateTime(TimeZone.currentSystemDefault()).date
    
    val label = when (date) {
        today -> "今日"
        today.minus(DatePeriod(days = 1)) -> "昨日"
        else -> "${date.year}年${date.month.ordinal + 1}月${date.day}日"
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background) // Match screen background to "hide" the bar look
            .padding(horizontal = YatteSpacing.md, vertical = YatteSpacing.sm),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@org.jetbrains.compose.ui.tooling.preview.Preview
@Composable
private fun YatteStickyDateHeaderPreview() {
    MaterialTheme {
        YatteStickyDateHeader(
            date = LocalDate(2025, 1, 1)
        )
    }
}
