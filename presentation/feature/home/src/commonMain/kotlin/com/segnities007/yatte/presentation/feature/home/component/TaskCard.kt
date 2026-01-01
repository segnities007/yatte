package com.segnities007.yatte.presentation.feature.home.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.presentation.designsystem.component.card.YatteActionCard
import com.segnities007.yatte.presentation.designsystem.theme.YatteTheme
import com.segnities007.yatte.presentation.designsystem.component.card.YatteCard
import com.segnities007.yatte.presentation.designsystem.component.button.YatteFilledIconButton
import com.segnities007.yatte.presentation.designsystem.component.button.YatteIconButton
import com.segnities007.yatte.presentation.designsystem.effect.ConfettiManager
import com.segnities007.yatte.presentation.designsystem.theme.YatteBrushes
import com.segnities007.yatte.presentation.designsystem.theme.YatteColors
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import kotlinx.coroutines.delay
import kotlin.time.Clock
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.home.generated.resources.cd_complete
import yatte.presentation.feature.home.generated.resources.cd_skip
import yatte.presentation.feature.home.generated.resources.Res as HomeRes

private val TaskCardElevation = 2.dp

/**
 * タスクカードコンポーネント
 */
@Composable
fun TaskCard(
    task: Task,
    onComplete: () -> Unit,
    onClick: () -> Unit,
    onSnooze: (() -> Unit)? = null,
    onSkip: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    accentColor: Color? = null,
    modifier: Modifier = Modifier,
) {
    var isCompleting by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(true) }

    val scale by animateFloatAsState(
        targetValue = if (isCompleting) 0.8f else 1f,
        animationSpec = tween(durationMillis = 200),
        label = "scale",
    )

    LaunchedEffect(isCompleting) {
        if (isCompleting) {
            delay(200)
            isVisible = false
            delay(150)
            onComplete()
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        exit = scaleOut(animationSpec = tween(150)) + fadeOut(animationSpec = tween(150)),
    ) {
        YatteActionCard(
            title = task.title,
            onClick = onClick,
            onDismiss = onDismiss,
            accentColor = accentColor,
            elevation = TaskCardElevation,
            modifier = modifier.scale(scale),
            actions = {
                if (onSnooze != null) {
                    YatteIconButton(
                        onClick = onSnooze,
                        icon = Icons.Default.Update,
                        contentDescription = "Snooze",
                        tint = YatteTheme.colors.secondary,
                    )
                }

                if (onSkip != null) {
                    YatteIconButton(
                        onClick = onSkip,
                        icon = Icons.Default.SkipNext,
                        contentDescription = stringResource(HomeRes.string.cd_skip),
                        tint = YatteTheme.colors.secondary,
                    )
                }

                YatteIconButton(
                    onClick = {
                        isCompleting = true
                        ConfettiManager.burst()
                    },
                    icon = Icons.Default.Check,
                    contentDescription = stringResource(HomeRes.string.cd_complete),
                    tint = YatteTheme.colors.primary,
                )
            }
        )
    }
}

@Composable
@Preview
fun TaskCardPreview() {
    YatteTheme {
        TaskCard(
            task = Task(
                id = TaskId("1"),
                title = "Design Mockup",
                time = LocalTime(10, 0),
                createdAt = Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds()).toLocalDateTime(TimeZone.currentSystemDefault()),
            ),
            onComplete = {},
            onClick = {},
            onSnooze = {},
            onSkip = {},
            onDismiss = {},
        )
    }
}


