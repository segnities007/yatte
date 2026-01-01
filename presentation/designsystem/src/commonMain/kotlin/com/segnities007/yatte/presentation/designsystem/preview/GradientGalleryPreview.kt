package com.segnities007.yatte.presentation.designsystem.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.theme.YatteBrushes
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * グラデーションギャラリー - Green と Yellow のバリエーション
 * すべて 1:8:1 比率で統一
 */
@Preview(name = "Gradient Gallery", showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun GradientGalleryPreview() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("🎨 グラデーションギャラリー (1:8:1)", style = MaterialTheme.typography.titleLarge)
        Text("Highlight 10% → Body 80% → Shadow 10%", style = MaterialTheme.typography.bodySmall)
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // === Green ===
        SectionTitle("🌿 Green (テーマカラー)")
        GradientItem("Main (標準)", YatteBrushes.Green.Main)
        GradientItem("Light (明るい)", YatteBrushes.Green.Light)
        GradientItem("Vivid (鮮やか)", YatteBrushes.Green.Vivid)
        GradientItem("Dark (暗い)", YatteBrushes.Green.Dark)
        GradientItem("Muted (彩度低)", YatteBrushes.Green.Muted)
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // === Yellow ===
        SectionTitle("🌟 Yellow (アクセント)")
        GradientItem("Main (標準)", YatteBrushes.Yellow.Main)
        GradientItem("Light (明るい)", YatteBrushes.Yellow.Light)
        GradientItem("Vivid (鮮やか)", YatteBrushes.Yellow.Vivid)
        GradientItem("Dark (暗い)", YatteBrushes.Yellow.Dark)
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
private fun GradientItem(label: String, brush: Brush) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.width(120.dp)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(36.dp)
                .background(brush, RoundedCornerShape(8.dp))
        )
    }
}

/**
 * ボタンスタイルプレビュー - Green
 */
@Preview(name = "Green Button Styles", showBackground = true, widthDp = 400)
@Composable
fun GreenButtonStylePreview() {
    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Green ボタンスタイル", style = MaterialTheme.typography.titleMedium)
        
        listOf(
            "Main" to YatteBrushes.Green.Main,
            "Light" to YatteBrushes.Green.Light,
            "Vivid" to YatteBrushes.Green.Vivid,
            "Dark" to YatteBrushes.Green.Dark,
            "Muted" to YatteBrushes.Green.Muted,
        ).forEach { (label, brush) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(brush, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(label, color = Color.White, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

/**
 * ボタンスタイルプレビュー - Yellow
 */
@Preview(name = "Yellow Button Styles", showBackground = true, widthDp = 400)
@Composable
fun YellowButtonStylePreview() {
    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Yellow ボタンスタイル", style = MaterialTheme.typography.titleMedium)
        
        listOf(
            "Main" to YatteBrushes.Yellow.Main,
            "Light" to YatteBrushes.Yellow.Light,
            "Vivid" to YatteBrushes.Yellow.Vivid,
            "Dark" to YatteBrushes.Yellow.Dark,
        ).forEach { (label, brush) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(brush, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(label, color = Color.White, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
