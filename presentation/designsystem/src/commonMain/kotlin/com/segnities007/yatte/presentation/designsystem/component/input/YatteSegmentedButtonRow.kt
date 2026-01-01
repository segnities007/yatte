package com.segnities007.yatte.presentation.designsystem.component.input

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.animation.rememberBounceInteraction
import com.segnities007.yatte.presentation.designsystem.theme.LocalYattePrimaryBrush
import com.segnities007.yatte.presentation.designsystem.theme.YatteBrushes
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Yatte統一セグメントボタンコンポーネント（カスタム実装）
 * 
 * Material 3 SegmentedButtonの代わりにRow + Boxで実装
 * これによりBrush（グラデーション）を正しくサポート可能
 * 
 * 使用場面:
 * - 単発/週次の切り替え
 * - 表示モード切り替え
 * - フィルター切り替え
 */
@Composable
fun <T> YatteSegmentedButtonRow(
    options: List<T>,
    selectedIndex: Int,
    onOptionSelected: (Int, T) -> Unit,
    modifier: Modifier = Modifier,
    brush: Brush = LocalYattePrimaryBrush.current,
    content: @Composable (T) -> Unit,
) {
    val cornerRadius = 8.dp
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(cornerRadius))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(cornerRadius)),
    ) {
        options.forEachIndexed { index, option ->
            val isSelected = selectedIndex == index
            val isFirst = index == 0
            val isLast = index == options.size - 1
            
            val shape = when {
                isFirst -> RoundedCornerShape(topStart = cornerRadius, bottomStart = cornerRadius)
                isLast -> RoundedCornerShape(topEnd = cornerRadius, bottomEnd = cornerRadius)
                else -> RoundedCornerShape(0.dp)
            }
            
            val (interactionSource, bounceModifier) = rememberBounceInteraction()
            
            Box(
                modifier = Modifier
                    .weight(1f)
                    .then(bounceModifier)
                    .clip(shape)
                    .then(
                        if (isSelected) {
                            Modifier.background(brush, shape)
                        } else {
                            Modifier.background(Color.Transparent)
                        }
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = ripple(),
                        onClick = { onOptionSelected(index, option) }
                    )
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.runtime.CompositionLocalProvider(
                    androidx.compose.material3.LocalContentColor provides 
                        if (isSelected) com.segnities007.yatte.presentation.designsystem.theme.LocalYatteOnPrimaryBrushColor.current else MaterialTheme.colorScheme.onSurfaceVariant
                ) {
                    content(option)
                }
            }

            // Divider between items (if not last)
            if (index < options.size - 1) {
                // Determine if divider should be visible (neither adjacent item is selected)
                val nextSelected = selectedIndex == index + 1
                val showDivider = !isSelected && !nextSelected
                
                if (showDivider) {
                    androidx.compose.material3.VerticalDivider(
                        modifier = Modifier
                            .height(24.dp)
                            .align(Alignment.CenterVertically),
                        color = MaterialTheme.colorScheme.outline
                    )
                } else {
                    // Invisible spacer to maintain layout consistency if needed, 
                    // but for weight(1f) items, 1dp divider usually doesn't shift much.
                    // However, to keep total width consistent, we might want a 1dp transparent items.
                    // For now, let's just skip it as weight distribution absorbs the 1dp difference.
                }
            }
        }
    }
}

@Preview
@Composable
private fun YatteSegmentedButtonRowPreview() {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("Single", "Weekly")
    Column(modifier = Modifier.padding(16.dp)) {
        YatteSegmentedButtonRow(
            options = options,
            selectedIndex = selectedIndex,
            onOptionSelected = { index, _ -> selectedIndex = index },
            content = { Text(it) },
        )
    }
}

@Preview(name = "Brush Variants", showBackground = true, widthDp = 400)
@Composable
private fun YatteSegmentedButtonRowBrushPreview() {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("Main", "Vivid")
    
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Green Main", style = MaterialTheme.typography.labelMedium)
        YatteSegmentedButtonRow(
            options = options,
            selectedIndex = selectedIndex,
            onOptionSelected = { index, _ -> selectedIndex = index },
            brush = YatteBrushes.Green.Main,
            content = { Text(it) },
        )
        
        Text("Green Vivid", style = MaterialTheme.typography.labelMedium)
        YatteSegmentedButtonRow(
            options = options,
            selectedIndex = 1,
            onOptionSelected = { _, _ -> },
            brush = YatteBrushes.Green.Vivid,
            content = { Text(it) },
        )
        
        Text("Yellow Main", style = MaterialTheme.typography.labelMedium)
        YatteSegmentedButtonRow(
            options = options,
            selectedIndex = 0,
            onOptionSelected = { _, _ -> },
            brush = YatteBrushes.Yellow.Main,
            content = { Text(it) },
        )
    }
}

/**
 * グラデーションスタイル比較プレビュー
 */
@Preview(name = "Gradient Style Comparison", showBackground = true, widthDp = 400)
@Composable
private fun GradientStyleComparisonPreview() {
    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Green バリエーション", style = MaterialTheme.typography.titleMedium)
        
        Text("Main (標準)", style = MaterialTheme.typography.labelMedium)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(YatteBrushes.Green.Main, RoundedCornerShape(8.dp))
        )
        
        Text("Light (明るめ)", style = MaterialTheme.typography.labelMedium)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(YatteBrushes.Green.Light, RoundedCornerShape(8.dp))
        )
        
        Text("Vivid (鮮やか)", style = MaterialTheme.typography.labelMedium)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(YatteBrushes.Green.Vivid, RoundedCornerShape(8.dp))
        )
        
        Text("Dark (暗め)", style = MaterialTheme.typography.labelMedium)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(YatteBrushes.Green.Dark, RoundedCornerShape(8.dp))
        )
    }
}
