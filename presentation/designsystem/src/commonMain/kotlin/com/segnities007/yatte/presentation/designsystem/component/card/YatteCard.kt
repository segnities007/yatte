package com.segnities007.yatte.presentation.designsystem.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick
import com.segnities007.yatte.presentation.designsystem.theme.YatteColors

import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing

private val AccentBarWidth = 6.dp

/**
 * Yatte統一カード
 * accentColorを指定すると左端にカラフルなアクセントバーが表示される
 */
@Composable
fun YatteCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = CardDefaults.shape,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    accentColor: Color? = null,
    elevation: Dp = 2.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.then(
            if (onClick != null) Modifier.bounceClick(onTap = onClick) else Modifier,
        ),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
    ) {
        if (accentColor != null) {
            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                Box(
                    modifier = Modifier
                        .width(AccentBarWidth)
                        .fillMaxHeight()
                        .background(accentColor)
                )
                Column(modifier = Modifier.weight(1f)) {
                    content()
                }
            }
        } else {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun YatteCardPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        YatteCard(onClick = {}) {
            Text("Normal Card", modifier = Modifier.padding(16.dp))
        }
        Box(modifier = Modifier.height(16.dp))
        YatteCard(onClick = {}, accentColor = YatteColors.primary) {
            Text("Card with Accent", modifier = Modifier.padding(16.dp))
        }
    }
}

