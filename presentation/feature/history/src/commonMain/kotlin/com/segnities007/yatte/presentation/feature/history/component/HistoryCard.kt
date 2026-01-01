package com.segnities007.yatte.presentation.feature.history.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import com.segnities007.yatte.presentation.designsystem.theme.YatteTheme
import com.segnities007.yatte.presentation.designsystem.component.display.YatteText
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.segnities007.yatte.domain.aggregate.history.model.History
import com.segnities007.yatte.presentation.core.formatter.DateFormatter
import com.segnities007.yatte.presentation.designsystem.component.card.YatteActionCard
import com.segnities007.yatte.presentation.designsystem.component.button.YatteIconButton
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.core.generated.resources.common_delete
import yatte.presentation.core.generated.resources.Res as CoreRes
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import com.segnities007.yatte.domain.aggregate.history.model.HistoryId
import com.segnities007.yatte.domain.aggregate.history.model.HistoryStatus
import kotlin.time.Clock
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone

/**
 * 履歴カード
 */
@Composable
fun HistoryCard(
    history: History,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    YatteActionCard(
        title = history.title,
        onClick = {}, // TODO: Detail navigation?
        modifier = modifier.fillMaxWidth(),
        supportingContent = {
            YatteText(
                text = DateFormatter.formatDateTime(history.completedAt),
                style = YatteTheme.typography.bodySmall,
                color = YatteTheme.colors.onSurfaceVariant,
            )
        },
        actions = {
            YatteIconButton(
                icon = Icons.Default.Delete,
                onClick = onDelete,
                contentDescription = stringResource(CoreRes.string.common_delete),
                tint = YatteTheme.colors.error,
            )
        }
    )
}

@Composable
@Preview
fun HistoryCardPreview() {
    YatteTheme {
        HistoryCard(
            history = History(
                id = HistoryId("1"),
                taskId = TaskId("1"),
                title = "Meeting",
                completedAt = Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds()).toLocalDateTime(TimeZone.currentSystemDefault()),
                status = HistoryStatus.COMPLETED
            ),
            onDelete = {},
        )
    }
}
