package com.segnities007.yatte.presentation.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// ============================================================
// Yatte Color Palette
// Design Principle: 「楽しくタスク管理ができる」ゲーム感覚
// ============================================================

// --- Primary Colors (Green Gradients) ---
/** Forest - Dark Green for headers, important actions */
val YatteForest = Color(0xFF2E7D32)
/** Primary - Main action color, success state */
val YattePrimary = Color(0xFF4CAF50)
/** Leaf - Light green for sub-actions, hover */
val YatteLeaf = Color(0xFF81C784)
/** Mint - Pale green for background accents, selection */
val YatteMint = Color(0xFFC8E6C9)

// --- Accent Colors (Bold usage) ---
/** Sunshine - Yellow for highlights, achievements */
val YatteSunshine = Color(0xFFFFC107)
/** Honey - Amber for warnings, in-progress */
val YatteHoney = Color(0xFFFFB300)
/** Sky - Blue for info, links */
val YatteSky = Color(0xFF2196F3)
/** Coral - Orange for notifications, urgent */
val YatteCoral = Color(0xFFFF7043)
/** Berry - Purple for sound, special */
val YatteBerry = Color(0xFFAB47BC)

// --- Light Mode Base Colors ---
val YatteSurfaceLight = Color(0xFFFFFFFF)
val YatteBackgroundLight = Color(0xFFFAFAFA)
val YatteOnPrimaryLight = Color.White
val YatteOnBackgroundLight = Color(0xFF1B2E1B) // Dark green-tinted text

// --- Dark Mode Colors (Forest-tinted) ---
/** Deep Forest - Darkest background */
val YatteDeepForest = Color(0xFF0D1F0F)
/** Night Moss - Surface color */
val YatteNightMoss = Color(0xFF1A2E1A)
/** Shadow Leaf - Card background */
val YatteShadowLeaf = Color(0xFF2D3D2D)
/** Mist - Border, separator */
val YatteMist = Color(0xFF4A5D4A)

val YattePrimaryDark = Color(0xFFA5D6A7)
val YatteOnPrimaryDark = Color(0xFF0D1F0F)
val YatteOnBackgroundDark = Color(0xFFE8F5E9)

// --- Semantic Colors ---
val YatteError = Color(0xFFE53935)
val YatteErrorContainer = Color(0xFFFFCDD2)
val YatteOnError = Color.White
val YatteOnErrorContainer = Color(0xFFB71C1C)

val YatteSuccess = YattePrimary
val YatteWarning = YatteHoney

// ============================================================
// Color Schemes
// ============================================================

val YatteLightColorScheme = lightColorScheme(
    // Primary
    primary = YattePrimary,
    onPrimary = YatteOnPrimaryLight,
    primaryContainer = YatteMint,
    onPrimaryContainer = YatteForest,
    
    // Secondary (Yellow/Sunshine)
    secondary = YatteSunshine,
    onSecondary = Color(0xFF3E2723),
    secondaryContainer = Color(0xFFFFF8E1),
    onSecondaryContainer = Color(0xFF5D4037),
    
    // Tertiary (Sky Blue)
    tertiary = YatteSky,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFBBDEFB),
    onTertiaryContainer = Color(0xFF0D47A1),
    
    // Background & Surface
    background = YatteBackgroundLight,
    onBackground = YatteOnBackgroundLight,
    surface = YatteSurfaceLight,
    onSurface = YatteOnBackgroundLight,
    surfaceVariant = Color(0xFFF1F8E9),
    onSurfaceVariant = Color(0xFF4A5D4A),
    
    // Error
    error = YatteError,
    onError = YatteOnError,
    errorContainer = YatteErrorContainer,
    onErrorContainer = YatteOnErrorContainer,
    
    // Outline
    outline = YatteMist,
    outlineVariant = YatteMint,
    
    // Inverse
    inverseSurface = YatteNightMoss,
    inverseOnSurface = YatteOnBackgroundDark,
    inversePrimary = YatteLeaf,
)

val YatteDarkColorScheme = darkColorScheme(
    // Primary (Brighter for dark mode)
    primary = YattePrimaryDark,
    onPrimary = YatteOnPrimaryDark,
    primaryContainer = YatteForest,
    onPrimaryContainer = YatteMint,
    
    // Secondary
    secondary = Color(0xFFFFD54F),
    onSecondary = Color(0xFF3E2723),
    secondaryContainer = Color(0xFF5D4037),
    onSecondaryContainer = Color(0xFFFFF8E1),
    
    // Tertiary
    tertiary = Color(0xFF64B5F6),
    onTertiary = Color(0xFF0D47A1),
    tertiaryContainer = Color(0xFF0D47A1),
    onTertiaryContainer = Color(0xFFBBDEFB),
    
    // Background & Surface (Forest-tinted dark)
    background = YatteDeepForest,
    onBackground = YatteOnBackgroundDark,
    surface = YatteNightMoss,
    onSurface = YatteOnBackgroundDark,
    surfaceVariant = YatteShadowLeaf,
    onSurfaceVariant = Color(0xFFA5D6A7),
    
    // Error
    error = Color(0xFFEF5350),
    onError = Color.Black,
    errorContainer = Color(0xFFB71C1C),
    onErrorContainer = Color(0xFFFFCDD2),
    
    // Outline
    outline = YatteMist,
    outlineVariant = YatteShadowLeaf,
    
    // Inverse
    inverseSurface = YatteSurfaceLight,
    inverseOnSurface = YatteOnBackgroundLight,
    inversePrimary = YatteForest,
)

// ============================================================
// Extended Colors (For direct usage outside MaterialTheme)
// ============================================================

/**
 * Yatte拡張カラー
 * MaterialThemeのcolorSchemeに含まれない追加のカラーを提供
 */
object YatteColors {
    // Primary Gradients
    val forest = YatteForest
    val primary = YattePrimary
    val leaf = YatteLeaf
    val mint = YatteMint
    
    // Accents
    val sunshine = YatteSunshine
    val honey = YatteHoney
    val sky = YatteSky
    val coral = YatteCoral
    val berry = YatteBerry
    
    // Game-like colors
    val xpGold = Color(0xFFFFD700)
    val streakFire = Color(0xFFFF6B35)
    val badgeBronze = Color(0xFFCD7F32)
    val badgeSilver = Color(0xFFC0C0C0)
    val badgeGold = Color(0xFFFFD700)
}

