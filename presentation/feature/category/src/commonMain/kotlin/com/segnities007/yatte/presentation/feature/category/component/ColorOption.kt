package com.segnities007.yatte.presentation.feature.category.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import com.segnities007.yatte.presentation.designsystem.component.display.YatteIcon
import com.segnities007.yatte.presentation.designsystem.theme.YatteTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.domain.aggregate.category.model.CategoryColor
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing

@Composable
fun ColorOption(
    color: CategoryColor,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(ColorOptionSize)
            .clip(CircleShape)
            .background(Color(color.hex))
            .then(
                if (isSelected) {
                    Modifier.border(ColorOptionBorderWidth, YatteTheme.colors.primary, CircleShape)
                } else {
                    Modifier
                },
            )
            .bounceClick(onTap = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (isSelected) {
            YatteIcon(
                Icons.Default.Check,
                contentDescription = "選択済み",
                tint = Color.Black.copy(alpha = 0.7f), // TODO: High contrast?
                modifier = Modifier.size(ColorOptionIconSize),
            )
        }
    }
}

private val ColorOptionSize = 40.dp
private val ColorOptionIconSize = 20.dp
private val ColorOptionBorderWidth = YatteSpacing.xxs

@Composable
@Preview
fun ColorOptionPreview() {
    YatteTheme {
        ColorOption(
            color = CategoryColor.BLUE,
            isSelected = true,
            onClick = {},
        )
    }
}
