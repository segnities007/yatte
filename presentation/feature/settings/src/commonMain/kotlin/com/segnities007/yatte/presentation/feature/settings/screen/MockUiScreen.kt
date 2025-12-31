package com.segnities007.yatte.presentation.feature.settings.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.component.input.YatteSegmentedButtonRow
import com.segnities007.yatte.presentation.designsystem.theme.YatteBrushes
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing

@Composable
fun MockUiScreen() {
    val options = listOf("Normal", "Short", "Long")
    var selectedIndex by remember { mutableIntStateOf(0) }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                "UI Debug: Segmented Button Fix",
                style = MaterialTheme.typography.headlineSmall
            )

            HorizontalDivider()

            Text("❌ Legacy (Problematic)", style = MaterialTheme.typography.labelLarge, color = Color.Red)
            Text("Placeholder for the buggy usage.", style = MaterialTheme.typography.bodySmall)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.Red.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                    .border(1.dp, Color.Red, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("Legacy Code Removed (Was Swelling)", color = Color.Red)
            }

            HorizontalDivider()

            Text("✅ Fixed (Custom Implementation)", style = MaterialTheme.typography.labelLarge, color = Color(0xFF2E7D32))
            Text("Static layout, clean 3D bevel.", style = MaterialTheme.typography.bodySmall)
            YatteSegmentedButtonRow(
                options = options,
                selectedIndex = selectedIndex,
                onOptionSelected = { i, _ -> selectedIndex = i }
            ) { Text(it) }
        }
    }
}
