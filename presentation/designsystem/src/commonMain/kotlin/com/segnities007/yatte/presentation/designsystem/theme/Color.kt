package com.segnities007.yatte.presentation.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Light Colors
val PrimaryLight = Color(0xFF66BB6A) // Soft Green
val SecondaryLight = Color(0xFFFFF176) // Soft Yellow
val SurfaceLight = Color(0xFFFFFFFF)
val BackgroundLight = Color(0xFFFAFAFA) // Off White (Gray 50)
val OnPrimaryLight = Color.White
val OnBackgroundLight = Color(0xFF212121) // Gray 900

// Dark Colors
val PrimaryDark = Color(0xFFA5D6A7) // Pale Green
val SecondaryDark = Color(0xFFFFF59D) // Pale Yellow
val SurfaceDark = Color(0xFF242424)
val BackgroundDark = Color(0xFF121212)
val OnPrimaryDark = Color.Black
val OnBackgroundDark = Color(0xFFEEEEEE)

val YatteLightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    secondary = SecondaryLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
)

val YatteDarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    secondary = SecondaryDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
)
