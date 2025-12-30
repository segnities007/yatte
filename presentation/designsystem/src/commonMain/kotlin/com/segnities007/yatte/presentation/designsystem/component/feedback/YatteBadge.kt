package com.segnities007.yatte.presentation.designsystem.component.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun YatteBadge(
    text: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(
                color = containerColor,
                shape = RoundedCornerShape(4.dp),
            )
            .padding(horizontal = YatteSpacing.sm, vertical = YatteSpacing.xxs),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = contentColor,
        )
    }
}

@Preview
@Composable
private fun YatteBadgePreview() {
    YatteBadge(
        text = "COMPLETED",
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    )
}
