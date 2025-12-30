package com.segnities007.yatte.presentation.core.util

import androidx.compose.runtime.Composable
import kotlinx.datetime.DayOfWeek
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.core.generated.resources.Res
import yatte.presentation.core.generated.resources.common_weekday_fri_short
import yatte.presentation.core.generated.resources.common_weekday_mon_short
import yatte.presentation.core.generated.resources.common_weekday_sat_short
import yatte.presentation.core.generated.resources.common_weekday_sun_short
import yatte.presentation.core.generated.resources.common_weekday_thu_short
import yatte.presentation.core.generated.resources.common_weekday_tue_short
import yatte.presentation.core.generated.resources.common_weekday_wed_short

@Composable
fun DayOfWeek.toDisplayString(): String = when (this) {
    DayOfWeek.MONDAY -> stringResource(Res.string.common_weekday_mon_short)
    DayOfWeek.TUESDAY -> stringResource(Res.string.common_weekday_tue_short)
    DayOfWeek.WEDNESDAY -> stringResource(Res.string.common_weekday_wed_short)
    DayOfWeek.THURSDAY -> stringResource(Res.string.common_weekday_thu_short)
    DayOfWeek.FRIDAY -> stringResource(Res.string.common_weekday_fri_short)
    DayOfWeek.SATURDAY -> stringResource(Res.string.common_weekday_sat_short)
    DayOfWeek.SUNDAY -> stringResource(Res.string.common_weekday_sun_short)
}
