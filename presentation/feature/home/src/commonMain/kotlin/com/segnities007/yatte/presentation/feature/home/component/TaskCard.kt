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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
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
import com.segnities007.yatte.presentation.designsystem.component.card.YatteCard
import com.segnities007.yatte.presentation.designsystem.component.button.YatteIconButton
import com.segnities007.yatte.presentation.designsystem.effect.ConfettiManager
import com.segnities007.yatte.presentation.designsystem.theme.YatteColors
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import kotlinx.coroutines.delay
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
        if (onDismiss != null) {
            val dismissState = rememberSwipeToDismissBoxState()

            LaunchedEffect(dismissState.currentValue) {
                if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart ||
                    dismissState.currentValue == SwipeToDismissBoxValue.StartToEnd
                ) {
                    onDismiss()
                }
            }

            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.errorContainer)
                            .padding(horizontal = YatteSpacing.lg),
                        contentAlignment = if (dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) {
                            Alignment.CenterStart
                        } else {
                            Alignment.CenterEnd
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onErrorContainer,
                        )
                    }
                },
                content = {
                    TaskCardContent(
                        task = task,
                        scale = scale,
                        onClick = onClick,
                        onCompleteClick = {
                            isCompleting = true
                            ConfettiManager.burst()
                        },
                        onSnoozeClick = onSnooze,
                        onSkipClick = onSkip,
                        accentColor = accentColor,
                        modifier = modifier,
                    )
                },
            )
        } else {
            TaskCardContent(
                task = task,
                scale = scale,
                onClick = onClick,
                onCompleteClick = {
                    isCompleting = true
                    ConfettiManager.burst()
                },
                onSnoozeClick = onSnooze,
                onSkipClick = onSkip,
                accentColor = accentColor,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun TaskCardContent(
    task: Task,
    scale: Float,
    onClick: () -> Unit,
    onCompleteClick: () -> Unit,
    onSnoozeClick: (() -> Unit)? = null,
    onSkipClick: (() -> Unit)? = null,
    accentColor: Color? = null,
    modifier: Modifier = Modifier,
) {
    YatteCard(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .scale(scale),
        elevation = TaskCardElevation,
        accentColor = accentColor,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(YatteSpacing.md),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(YatteSpacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (onSnoozeClick != null) {
                    YatteIconButton(
                        onClick = onSnoozeClick,
                        icon = Icons.Default.Update,
                        contentDescription = "Snooze",
                        tint = MaterialTheme.colorScheme.secondary,
                    )
                }
                
                if (onSkipClick != null) {
                    YatteIconButton(
                        onClick = onSkipClick,
                        icon = Icons.Default.SkipNext,
                        contentDescription = stringResource(HomeRes.string.cd_skip),
                        tint = YatteColors.sky,
                    )
                }
                
                YatteIconButton(
                    onClick = onCompleteClick,
                    icon = Icons.Default.Check,
                    contentDescription = stringResource(HomeRes.string.cd_complete),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}


