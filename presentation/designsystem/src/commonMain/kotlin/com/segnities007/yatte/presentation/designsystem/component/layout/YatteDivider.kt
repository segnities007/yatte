package com.segnities007.yatte.presentation.designsystem.component.layout

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.segnities007.yatte.presentation.designsystem.theme.YatteTheme

@Composable
fun YatteDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = YatteTheme.spacing.divider,
    color: Color = YatteTheme.colors.outlineVariant,
) {
    HorizontalDivider(
        modifier = modifier,
        thickness = thickness,
        color = color
    )
}

@Composable
fun YatteVerticalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = YatteTheme.spacing.divider,
    color: Color = YatteTheme.colors.outlineVariant,
) {
    VerticalDivider(
        modifier = modifier,
        thickness = thickness,
        color = color
    )
}
