package com.segnities007.yatte.presentation.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.segnities007.yatte.domain.aggregate.settings.model.ThemeMode

private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = GreenPrimary,
    onPrimaryContainer = LightOnPrimary,
    secondary = CreamSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = CreamSecondaryVariant,
    onSecondaryContainer = LightOnSecondary,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
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

/**
 * Yatteアプリのテーマ
 *
 * @param themeMode テーマモード（ライト/ダーク/システム）
 * @param content コンテンツ
 */
@Composable
fun YatteTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit,
) {
    val isDarkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}
