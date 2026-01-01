package com.segnities007.yatte.presentation.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

/**
 * Yatte Design System Theme
 *
 * This theme wraps MaterialTheme and provides additional design tokens (Spacing, etc.)
 */
@Composable
fun YatteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorScheme: ColorScheme = if (darkTheme) YatteGreenDarkColorScheme else YatteGreenLightColorScheme,
    primaryBrush: Brush = if (darkTheme) YatteBrushes.Green.Main else YatteBrushes.Green.Main,
    onPrimaryBrushColor: Color = Color.White,
    emphasisBrush: Brush = YatteBrushes.Yellow.Main,
    onEmphasisBrushColor: Color = Color.White,
    dangerBrush: Brush = YatteBrushes.Error.Main,
    onDangerBrushColor: Color = Color.White,
    content: @Composable () -> Unit
) {
    val typography = YatteTypography
    val spacing = YatteSpacing

    CompositionLocalProvider(
        LocalYatteTypography provides typography,
        LocalYatteSpacing provides spacing.toTokens(),
        LocalYattePrimaryBrush provides primaryBrush,
        LocalYatteOnPrimaryBrushColor provides onPrimaryBrushColor,
        LocalYatteEmphasisBrush provides emphasisBrush,
        LocalYatteOnEmphasisBrushColor provides onEmphasisBrushColor,
        LocalYatteDangerBrush provides dangerBrush,
        LocalYatteOnDangerBrushColor provides onDangerBrushColor,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography.toMaterial3Typography(),
            shapes = YatteShapes,
            content = content
        )
    }
}

private fun YatteSpacing.toTokens(): YatteSpacingTokens {
    return YatteSpacingTokens(
        xxs = this.xxs,
        xs = this.xs,
        sm = this.sm,
        md = this.md,
        lg = this.lg,
        xl = this.xl,
        xxl = this.xxl,
        divider = this.divider
    )
}

/**
 * Accessor for Yatte Design System tokens
 */
object YatteTheme {
    val colors: ColorScheme
        @Composable
        get() = MaterialTheme.colorScheme

    val typography: YatteTypographyTokens
        @Composable
        get() = LocalYatteTypography.current

    val spacing: YatteSpacingTokens
        @Composable
        get() = LocalYatteSpacing.current

    val primaryBrush: Brush
        @Composable
        get() = LocalYattePrimaryBrush.current

    val shapes: Shapes
        @Composable
        get() = MaterialTheme.shapes
}
