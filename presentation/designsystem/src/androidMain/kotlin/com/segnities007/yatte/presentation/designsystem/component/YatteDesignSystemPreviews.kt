package com.segnities007.yatte.presentation.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
private fun YatteCardPreview() {
    YatteCard(
        onClick = {},
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Yatte Card Preview",
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun YatteButtonPreview() {
    YatteButton(
        text = "Yatte Button",
        onClick = {}
    )
}
