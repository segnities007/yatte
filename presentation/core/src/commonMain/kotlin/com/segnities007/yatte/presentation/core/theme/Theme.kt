package com.segnities007.yatte.presentation.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.segnities007.yatte.domain.aggregate.settings.model.ThemeColor
import com.segnities007.yatte.domain.aggregate.settings.model.ThemeMode
import com.segnities007.yatte.presentation.designsystem.theme.YatteBaselineDarkColorScheme
import com.segnities007.yatte.presentation.designsystem.theme.YatteBaselineLightColorScheme
import com.segnities007.yatte.presentation.designsystem.theme.YatteBlueDarkColorScheme
import com.segnities007.yatte.presentation.designsystem.theme.YatteBlueLightColorScheme
import com.segnities007.yatte.presentation.designsystem.theme.YatteBrushes
import com.segnities007.yatte.presentation.designsystem.theme.YatteGreenDarkColorScheme
import com.segnities007.yatte.presentation.designsystem.theme.YatteGreenLightColorScheme
import com.segnities007.yatte.presentation.designsystem.theme.YatteOrangeDarkColorScheme
import com.segnities007.yatte.presentation.designsystem.theme.YatteOrangeLightColorScheme
import com.segnities007.yatte.presentation.designsystem.theme.YattePurpleDarkColorScheme
import com.segnities007.yatte.presentation.designsystem.theme.YattePurpleLightColorScheme
import com.segnities007.yatte.presentation.designsystem.theme.YatteYellowDarkColorScheme
import com.segnities007.yatte.presentation.designsystem.theme.YatteYellowLightColorScheme
import com.segnities007.yatte.presentation.designsystem.theme.YatteTheme as DesignSystemYatteTheme

private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = GreenPrimary,
    onPrimaryContainer = LightOnPrimary,
    secondary = CreamSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = CreamSecondaryVariant,
    onSecondaryContainer = LightOnSecondary,
    background = GreenBackground, // Green tinted background
    onBackground = GreenOnBackground,
    surface = GreenSurface,       // Green tinted surface
    onSurface = GreenOnBackground,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
)

private val DarkColorScheme = darkColorScheme(
    primary = LightGreenPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = LightGreenPrimary,
    onPrimaryContainer = DarkOnPrimary,
    secondary = GoldenSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = GoldenSecondaryVariant,
    onSecondaryContainer = DarkOnSecondary,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
)

private val YellowColorScheme = lightColorScheme(
    primary = YellowPrimary,
    onPrimary = YellowOnPrimary,
    primaryContainer = YellowPrimaryContainer,
    onPrimaryContainer = YellowOnPrimaryContainer,
    secondary = GreenPrimary,
    onSecondary = LightOnPrimary,
    secondaryContainer = GreenPrimaryVariant,
    onSecondaryContainer = LightOnPrimary,
    background = YellowBackground, // Yellow tinted background
    onBackground = YellowOnBackground,
    surface = YellowSurface,       // Yellow tinted surface
    onSurface = YellowOnBackground,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
)

/**
 * Yatteアプリのテーマ
 *
 * @param themeMode テーマモード（ライト/ダーク/システム）
 * @param themeColor テーマカラー（グリーン/イエロー）
 * @param content コンテンツ
 */
@Composable
fun YatteTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    themeColor: ThemeColor = ThemeColor.GREEN,
    content: @Composable () -> Unit,
) {
    val isDarkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = when (themeColor) {
        ThemeColor.DEFAULT -> if (isDarkTheme) YatteBaselineDarkColorScheme else YatteBaselineLightColorScheme
        ThemeColor.GREEN -> if (isDarkTheme) YatteGreenDarkColorScheme else YatteGreenLightColorScheme
        ThemeColor.YELLOW -> if (isDarkTheme) YatteYellowDarkColorScheme else YatteYellowLightColorScheme
        ThemeColor.BLUE -> if (isDarkTheme) YatteBlueDarkColorScheme else YatteBlueLightColorScheme
        ThemeColor.ORANGE -> if (isDarkTheme) YatteOrangeDarkColorScheme else YatteOrangeLightColorScheme
        ThemeColor.PURPLE -> if (isDarkTheme) YattePurpleDarkColorScheme else YattePurpleLightColorScheme
    }

    val primaryBrush: Brush = when (themeColor) {
        ThemeColor.DEFAULT -> if (isDarkTheme) YatteBrushes.Baseline.Dark else YatteBrushes.Baseline.Main
        ThemeColor.GREEN -> if (isDarkTheme) YatteBrushes.Green.Dark else YatteBrushes.Green.Main
        ThemeColor.YELLOW -> if (isDarkTheme) YatteBrushes.Yellow.Dark else YatteBrushes.Yellow.Main
        ThemeColor.BLUE -> if (isDarkTheme) YatteBrushes.Blue.Dark else YatteBrushes.Blue.Main
        ThemeColor.ORANGE -> if (isDarkTheme) YatteBrushes.Orange.Dark else YatteBrushes.Orange.Main
        ThemeColor.PURPLE -> if (isDarkTheme) YatteBrushes.Purple.Dark else YatteBrushes.Purple.Main
    }


    val onPrimaryBrushColor: Color = if (isDarkTheme) {
        when (themeColor) {
            ThemeColor.YELLOW -> Color(0xFF3E2723)
            else -> colorScheme.primary
        }
    } else {
        colorScheme.onPrimary
    }

    val emphasisBrush: Brush = when (themeColor) {
        ThemeColor.DEFAULT -> if (isDarkTheme) YatteBrushes.Yellow.Dark else YatteBrushes.Yellow.Main
        ThemeColor.GREEN -> if (isDarkTheme) YatteBrushes.Yellow.Main else YatteBrushes.Yellow.Main
        ThemeColor.YELLOW -> if (isDarkTheme) YatteBrushes.Green.Dark else YatteBrushes.Green.Main
        ThemeColor.BLUE -> if (isDarkTheme) YatteBrushes.Orange.Dark else YatteBrushes.Orange.Main
        ThemeColor.ORANGE -> if (isDarkTheme) YatteBrushes.Yellow.Dark else YatteBrushes.Yellow.Main
        ThemeColor.PURPLE -> if (isDarkTheme) YatteBrushes.Blue.Dark else YatteBrushes.Blue.Main
    }

    val onEmphasisBrushColor: Color = when (themeColor) {
        ThemeColor.YELLOW -> if (isDarkTheme) Color.Black else Color.White
        else -> Color(0xFF3E2723) // Dark Brown for Yellow background in Green/Purple themes
    }

    val dangerBrush: Brush = if (isDarkTheme) YatteBrushes.Error.Dark else YatteBrushes.Error.Main


    DesignSystemYatteTheme(
        colorScheme = colorScheme,
        primaryBrush = primaryBrush,
        onPrimaryBrushColor = onPrimaryBrushColor,
        emphasisBrush = emphasisBrush,
        onEmphasisBrushColor = onEmphasisBrushColor,
        dangerBrush = dangerBrush,
        onDangerBrushColor = Color.White,
        content = content
    )
}
