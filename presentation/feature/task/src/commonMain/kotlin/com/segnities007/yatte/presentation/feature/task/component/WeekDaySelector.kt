package com.segnities007.yatte.presentation.feature.task.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import com.segnities007.yatte.presentation.designsystem.component.input.YatteChip
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import kotlinx.datetime.DayOfWeek
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.task.generated.resources.weekday_fri_short
import yatte.presentation.feature.task.generated.resources.weekday_mon_short
import yatte.presentation.feature.task.generated.resources.weekday_sat_short
import yatte.presentation.feature.task.generated.resources.weekday_sun_short
import yatte.presentation.feature.task.generated.resources.weekday_thu_short
import yatte.presentation.feature.task.generated.resources.weekday_tue_short
import yatte.presentation.feature.task.generated.resources.weekday_wed_short
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
        horizontalArrangement = Arrangement.spacedBy(YatteSpacing.xs),
    ) {
        DayOfWeek.entries.forEach { day ->
            YatteChip(
                label = day.toDisplayString(),
                selected = day in selectedDays,
                onClick = { onDayToggle(day) },
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
