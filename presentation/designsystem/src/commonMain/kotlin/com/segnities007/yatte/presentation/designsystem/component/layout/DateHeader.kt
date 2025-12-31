package com.segnities007.yatte.presentation.designsystem.component.layout

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick
import com.segnities007.yatte.presentation.designsystem.component.button.YatteIconButton
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.designsystem.generated.resources.cd_next_day
import yatte.presentation.designsystem.generated.resources.cd_prev_day
import yatte.presentation.designsystem.generated.resources.date_header_format
import yatte.presentation.designsystem.generated.resources.weekday_fri_short
import yatte.presentation.designsystem.generated.resources.weekday_mon_short
import yatte.presentation.designsystem.generated.resources.weekday_sat_short
import yatte.presentation.designsystem.generated.resources.weekday_sun_short
import yatte.presentation.designsystem.generated.resources.weekday_thu_short
import yatte.presentation.designsystem.generated.resources.weekday_tue_short
import yatte.presentation.designsystem.generated.resources.weekday_wed_short
import yatte.presentation.designsystem.generated.resources.Res as DSRes

/**
 * 日付表示ヘッダー（前日/翌日ボタン付き）
 */
@Composable
fun DateHeader(
    selectedDate: LocalDate?,
    onPreviousDay: () -> Unit,
    onNextDay: () -> Unit,
    onTodayClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        YatteIconButton(
            onClick = onPreviousDay,
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(DSRes.string.cd_prev_day),
        )
        Text(
            text = selectedDate?.let { formatDate(it) } ?: "",
            modifier = Modifier
                .weight(1f)
                .bounceClick(onTap = onTodayClick),
            textAlign = TextAlign.Center,
        )
        YatteIconButton(
            onClick = onNextDay,
            icon = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = stringResource(DSRes.string.cd_next_day),
        )
    }
}

/**
 * 日付を「12月25日（水）」形式でフォーマット
 */
@Composable
fun formatDate(date: LocalDate): String {
    val dayOfWeekJapanese = when (date.dayOfWeek) {
        DayOfWeek.MONDAY -> stringResource(DSRes.string.weekday_mon_short)
        DayOfWeek.TUESDAY -> stringResource(DSRes.string.weekday_tue_short)
        DayOfWeek.WEDNESDAY -> stringResource(DSRes.string.weekday_wed_short)
        DayOfWeek.THURSDAY -> stringResource(DSRes.string.weekday_thu_short)
        DayOfWeek.FRIDAY -> stringResource(DSRes.string.weekday_fri_short)
        DayOfWeek.SATURDAY -> stringResource(DSRes.string.weekday_sat_short)
        DayOfWeek.SUNDAY -> stringResource(DSRes.string.weekday_sun_short)
    }

    return stringResource(
        DSRes.string.date_header_format,
        date.month.number,
        date.day,
        dayOfWeekJapanese,
        dayOfWeekJapanese,
    )
}

@org.jetbrains.compose.ui.tooling.preview.Preview
@Composable
private fun DateHeaderPreview() {
    MaterialTheme {
        DateHeader(
            selectedDate = LocalDate(2025, 1, 1),
            onPreviousDay = {},
            onNextDay = {},
            onTodayClick = {}
        )
    }
}
