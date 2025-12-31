package com.segnities007.yatte.presentation.feature.history.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import com.segnities007.yatte.domain.aggregate.history.model.HistoryStatus
import com.segnities007.yatte.presentation.designsystem.component.feedback.YatteBadge
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.history.generated.resources.status_completed
import yatte.presentation.feature.history.generated.resources.status_expired
import yatte.presentation.feature.history.generated.resources.status_skipped
import yatte.presentation.feature.history.generated.resources.Res as HistoryRes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.unit.dp

@Composable
fun HistoryStatusBadge(
    status: HistoryStatus,
    modifier: Modifier = Modifier,
) {
    val (backgroundColor, text) = when (status) {
        HistoryStatus.COMPLETED -> Pair(
            MaterialTheme.colorScheme.primaryContainer,
            stringResource(HistoryRes.string.status_completed),
        )
        HistoryStatus.SKIPPED -> Pair(
            MaterialTheme.colorScheme.tertiaryContainer,
            stringResource(HistoryRes.string.status_skipped),
        )
        HistoryStatus.EXPIRED -> Pair(
            MaterialTheme.colorScheme.errorContainer,
            stringResource(HistoryRes.string.status_expired),
        )
    }

    val textColor = when (status) {
        HistoryStatus.COMPLETED -> MaterialTheme.colorScheme.onPrimaryContainer
        HistoryStatus.SKIPPED -> MaterialTheme.colorScheme.onTertiaryContainer
        HistoryStatus.EXPIRED -> MaterialTheme.colorScheme.onErrorContainer
    }

    YatteBadge(
        text = text,
        containerColor = backgroundColor,
        contentColor = textColor,
        modifier = modifier,
    )
}

@Composable
@Preview
fun HistoryStatusBadgePreview() {
    MaterialTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            HistoryStatusBadge(status = HistoryStatus.COMPLETED)
            HistoryStatusBadge(status = HistoryStatus.SKIPPED)
            HistoryStatusBadge(status = HistoryStatus.EXPIRED)
        }
    }
}
