package com.segnities007.yatte.presentation.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import com.segnities007.yatte.domain.aggregate.settings.model.ThemeMode
import com.segnities007.yatte.presentation.designsystem.theme.YatteBrushes
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
 * @param themeMode テーマモード（ライト/ダーク/システム/グリーン/イエロー）
 * @param content コンテンツ
 */
@Composable
fun YatteTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit,
) {
    val isDarkTheme = when (themeMode) {
        ThemeMode.LIGHT, ThemeMode.GREEN, ThemeMode.YELLOW -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = when (themeMode) {
        ThemeMode.YELLOW -> YellowColorScheme
        else -> if (isDarkTheme) DarkColorScheme else LightColorScheme
    }

    val primaryBrush: Brush = when (themeMode) {
        ThemeMode.YELLOW -> YatteBrushes.Yellow.Main
        ThemeMode.DARK -> YatteBrushes.Green.Dark // Use Darker for Dark mode
        else -> YatteBrushes.Green.Main
    }

    DesignSystemYatteTheme(
        colorScheme = colorScheme,
        primaryBrush = primaryBrush,
        content = content
    )
}
