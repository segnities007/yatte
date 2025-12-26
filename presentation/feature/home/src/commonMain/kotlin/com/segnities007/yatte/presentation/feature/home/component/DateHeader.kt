package com.segnities007.yatte.presentation.feature.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.home.generated.resources.*
import yatte.presentation.feature.home.generated.resources.Res as HomeRes

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
        IconButton(onClick = onPreviousDay) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(HomeRes.string.cd_prev_day),
            )
        }
        Text(
            text = selectedDate?.let { formatDate(it) } ?: "",
            modifier = Modifier
                .weight(1f)
                .clickable { onTodayClick() },
            textAlign = TextAlign.Center,
        )
        IconButton(onClick = onNextDay) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = stringResource(HomeRes.string.cd_next_day),
            )
        }
    }
}

/**
 * 日付を「12月25日（水）」形式でフォーマット
 */
@Composable
fun formatDate(date: LocalDate): String {
    val dayOfWeekJapanese = when (date.dayOfWeek) {
        DayOfWeek.MONDAY -> stringResource(HomeRes.string.weekday_mon_short)
        DayOfWeek.TUESDAY -> stringResource(HomeRes.string.weekday_tue_short)
        DayOfWeek.WEDNESDAY -> stringResource(HomeRes.string.weekday_wed_short)
        DayOfWeek.THURSDAY -> stringResource(HomeRes.string.weekday_thu_short)
        DayOfWeek.FRIDAY -> stringResource(HomeRes.string.weekday_fri_short)
        DayOfWeek.SATURDAY -> stringResource(HomeRes.string.weekday_sat_short)
        DayOfWeek.SUNDAY -> stringResource(HomeRes.string.weekday_sun_short)
    }

    return stringResource(
        HomeRes.string.date_header_format,
        date.monthNumber,
        date.dayOfMonth,
        dayOfWeekJapanese,
    )
}
