package com.segnities007.yatte.presentation.feature.task.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import kotlinx.datetime.DayOfWeek
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.task.generated.resources.Res as TaskRes

/**
 * 曜日選択コンポーネント
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WeekDaySelector(
    selectedDays: Set<DayOfWeek>,
    onDayToggle: (DayOfWeek) -> Unit,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        DayOfWeek.entries.forEach { day ->
            FilterChip(
                selected = day in selectedDays,
                onClick = { onDayToggle(day) },
                label = { Text(day.toDisplayString()) },
            )
        }
    }
}

@Composable
private fun DayOfWeek.toDisplayString(): String = when (this) {
    DayOfWeek.MONDAY -> stringResource(TaskRes.string.weekday_mon_short)
    DayOfWeek.TUESDAY -> stringResource(TaskRes.string.weekday_tue_short)
    DayOfWeek.WEDNESDAY -> stringResource(TaskRes.string.weekday_wed_short)
    DayOfWeek.THURSDAY -> stringResource(TaskRes.string.weekday_thu_short)
    DayOfWeek.FRIDAY -> stringResource(TaskRes.string.weekday_fri_short)
    DayOfWeek.SATURDAY -> stringResource(TaskRes.string.weekday_sat_short)
    DayOfWeek.SUNDAY -> stringResource(TaskRes.string.weekday_sun_short)
}
