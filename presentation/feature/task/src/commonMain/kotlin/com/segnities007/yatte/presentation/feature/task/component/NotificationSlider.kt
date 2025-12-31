package com.segnities007.yatte.presentation.feature.task.component

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.segnities007.yatte.presentation.designsystem.theme.YatteTheme
import com.segnities007.yatte.presentation.designsystem.component.display.YatteText
import androidx.compose.ui.Modifier
import com.segnities007.yatte.presentation.designsystem.component.input.YatteSlider
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.task.generated.resources.notification_minutes_before
import yatte.presentation.feature.task.generated.resources.Res as TaskRes

/**
 * 通知時間スライダー（何分前に通知するか）
 */
@Composable
fun NotificationSlider(
    minutesBefore: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        YatteText(
            text = stringResource(TaskRes.string.notification_minutes_before, minutesBefore),
            style = YatteTheme.typography.labelLarge,
        )
        YatteSlider(
            value = minutesBefore.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = 0f..60f,
            steps = 11,
        )
    }
}
