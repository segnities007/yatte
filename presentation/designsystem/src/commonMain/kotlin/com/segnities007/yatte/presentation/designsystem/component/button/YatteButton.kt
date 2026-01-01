package com.segnities007.yatte.presentation.designsystem.component.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import com.segnities007.yatte.presentation.designsystem.animation.rememberBounceInteraction
import com.segnities007.yatte.presentation.designsystem.theme.LocalYatteDangerBrush
import com.segnities007.yatte.presentation.designsystem.theme.LocalYatteEmphasisBrush
import com.segnities007.yatte.presentation.designsystem.theme.LocalYatteOnDangerBrushColor
import com.segnities007.yatte.presentation.designsystem.theme.LocalYatteOnEmphasisBrushColor
import com.segnities007.yatte.presentation.designsystem.theme.LocalYatteOnPrimaryBrushColor
import com.segnities007.yatte.presentation.designsystem.theme.LocalYattePrimaryBrush
import com.segnities007.yatte.presentation.designsystem.theme.YatteBrushes
import com.segnities007.yatte.presentation.designsystem.theme.YatteColors


enum class YatteButtonStyle {
    Primary,    // Green Gradient (Theme) - メインアクション
    Emphasis,   // Yellow Gradient (Action) - 強調アクション
    Danger,     // Red Gradient (Danger) - 破壊的アクション
    Secondary   // Outlined with light background - サブアクション
}

@Composable
fun YatteButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: YatteButtonStyle = YatteButtonStyle.Primary,
    enabled: Boolean = true,
    fillContainer: Boolean = false,
) {
    val (interactionSource, bounceModifier) = rememberBounceInteraction()
    val shape = CircleShape

    val primaryBrush = LocalYattePrimaryBrush.current
    val emphasisBrush = LocalYatteEmphasisBrush.current
    val onEmphasisColor = LocalYatteOnEmphasisBrushColor.current
    val dangerBrush = LocalYatteDangerBrush.current
    val onDangerColor = LocalYatteOnDangerBrushColor.current

    val containerColor = Color.Transparent
    val onPrimaryBrushColor = LocalYatteOnPrimaryBrushColor.current
    val contentColor = when (style) {
        YatteButtonStyle.Primary -> onPrimaryBrushColor
        YatteButtonStyle.Emphasis -> onEmphasisColor
        YatteButtonStyle.Danger -> onDangerColor
        YatteButtonStyle.Secondary -> MaterialTheme.colorScheme.primary
    }

    val brush = when (style) {
        YatteButtonStyle.Primary -> primaryBrush
        YatteButtonStyle.Emphasis -> emphasisBrush
        YatteButtonStyle.Danger -> dangerBrush
        YatteButtonStyle.Secondary -> null
    }


    Button(
        onClick = onClick,
        modifier = modifier.then(bounceModifier),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        ),
        enabled = enabled,
        interactionSource = interactionSource,
        contentPadding = PaddingValues(0.dp),
        shape = shape
    ) {
        Box(
            modifier = Modifier
                .then(if (fillContainer) Modifier.fillMaxSize() else Modifier)
                .clip(shape)
                .then(
                    when {
                        brush != null && enabled -> Modifier.background(brush, shape)
                        style == YatteButtonStyle.Secondary && enabled -> Modifier
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f), shape)
                            .border(1.dp, MaterialTheme.colorScheme.primary, shape)
                        else -> Modifier
                    }
                )
                .padding(horizontal = 24.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun YatteButtonPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        YatteButton(text = "Primary Button", onClick = {}, style = YatteButtonStyle.Primary)
        YatteButton(text = "Emphasis Button", onClick = {}, style = YatteButtonStyle.Emphasis)
        YatteButton(text = "Secondary Button", onClick = {}, style = YatteButtonStyle.Secondary)
        YatteButton(text = "Disabled", onClick = {}, enabled = false)
    }
}
