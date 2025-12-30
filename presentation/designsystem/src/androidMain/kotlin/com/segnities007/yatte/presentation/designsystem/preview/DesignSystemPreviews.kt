package com.segnities007.yatte.presentation.designsystem.preview

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.component.button.YatteButton
import com.segnities007.yatte.presentation.designsystem.component.card.YatteCard
import com.segnities007.yatte.presentation.designsystem.component.button.YatteIconButton

@Preview
@Composable
private fun YatteButtonPreview() {
    MaterialTheme {
        YatteButton(
            text = "Yatte Button",
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun YatteCardPreview() {
    MaterialTheme {
        YatteCard(
            onClick = {},
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Yatte Card Content",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun YatteIconButtonPreview() {
    MaterialTheme {
        YatteIconButton(
            icon = Icons.Default.Add,
            onClick = {}
        )
    }
}
