package com.segnities007.yatte.presentation.designsystem.component.display

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing

enum class MetricStyle {
    /**
     * メイン表示（縦並び、強調）
     * アイコン(大) -> 数値(大) -> ラベル(小)
     */
    Primary,

    /**
     * リスト表示（横並び、コンパクト）
     * アイコン(中) -> [数値(中) -> ラベル(極小)]
     */
    List,
}

@Composable
fun YatteMetric(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    color: Color = MaterialTheme.colorScheme.primary,
    style: MetricStyle = MetricStyle.List,
) {
    val valueStyle: TextStyle
    val labelStyle: TextStyle
    val iconSize: Int

    when (style) {
        MetricStyle.Primary -> {
            valueStyle = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Black)
            labelStyle = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
            iconSize = 64
        }
        MetricStyle.List -> {
            valueStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            labelStyle = MaterialTheme.typography.labelSmall
            iconSize = 20
        }
    }

    when (style) {
        MetricStyle.Primary -> {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = color,
                            modifier = Modifier.size(iconSize.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(YatteSpacing.md))
                }
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = value,
                        style = valueStyle,
                        color = color.copy(alpha = 0.9f)
                    )
                    Text(
                        text = label,
                        style = labelStyle,
                        color = color
                    )
                }
            }
        }

        MetricStyle.List -> {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(iconSize.dp)
                    )
                    Spacer(modifier = Modifier.width(YatteSpacing.xs))
                }
                Column {
                    Text(
                        text = value,
                        style = valueStyle,
                        color = color
                    )
                    Text(
                        text = label,
                        style = labelStyle,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@org.jetbrains.compose.ui.tooling.preview.Preview
@Composable
private fun YatteMetricPreview() {
    MaterialTheme {
        Column {
            YatteMetric(
                value = "100",
                label = "Primary",
                style = MetricStyle.Primary,
                icon = Icons.Default.Home
            )
            YatteMetric(
                value = "50",
                label = "List",
                style = MetricStyle.List,
                icon = Icons.Default.Settings
            )
        }
    }
}
