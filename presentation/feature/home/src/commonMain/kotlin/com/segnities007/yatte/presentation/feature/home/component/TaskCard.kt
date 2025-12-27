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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.segnities007.yatte.presentation.designsystem.component.YatteCard
import com.segnities007.yatte.presentation.designsystem.effect.ConfettiManager
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.home.generated.resources.*
import yatte.presentation.feature.home.generated.resources.Res as HomeRes

/**
 * タスクカードコンポーネント（アニメーション対応）
 *
 * - 完了時: スケール + フェードアウト
 * - スワイプ: 左右スワイプで削除
 */
@Composable
fun TaskCard(
    task: Task,
    onComplete: () -> Unit,
    onClick: () -> Unit,
    onSkip: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    var isCompleting by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(true) }

    // 完了アニメーション
    val scale by animateFloatAsState(
        targetValue = if (isCompleting) 0.8f else 1f,
        animationSpec = tween(durationMillis = 200),
        label = "scale",
    )

    // 完了時の処理
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
            // スワイプ削除対応
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
                            .padding(horizontal = 20.dp),
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
                        onSkipClick = onSkip,
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
                onSkipClick = onSkip,
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
    onSkipClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    YatteCard(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .scale(scale),
        elevation = 2.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${task.time.hour}:${task.time.minute.toString().padStart(2, '0')}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Row {
                if (onSkipClick != null) {
                    IconButton(onClick = onSkipClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = stringResource(HomeRes.string.cd_skip),
                            tint = MaterialTheme.colorScheme.secondary,
                        )
                    }
                }
                IconButton(onClick = onCompleteClick) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = stringResource(HomeRes.string.cd_complete),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}
