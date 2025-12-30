package com.segnities007.yatte.presentation.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.component.button.YatteButton
import com.segnities007.yatte.presentation.designsystem.component.button.YatteFloatingActionButton
import com.segnities007.yatte.presentation.feature.home.component.BouncyCard

@Composable
fun MockDesignScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header
            Text(
                text = "Today",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 24.dp)
            )

            // Bouncy Card List (Refactored Component)
            BouncyCard(
                title = "牛乳を買う",
                time = "10:00",
                isCompleted = false
            )

            BouncyCard(
                title = "ミーティング",
                time = "14:00",
                isCompleted = true
            )

            // Interactive Button
            YatteButton(
                text = "Tap to Feel Physics",
                onClick = { /* Haptic & Sound would play here */ },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Floating Action Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            YatteFloatingActionButton(
                isVisible = true,
                onClick = {},
                contentDescription = "Add Task"
            )
        }
    }
}
