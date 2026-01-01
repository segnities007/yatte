package com.segnities007.yatte.presentation.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Yatte Primary Brush Provider
 */
val LocalYattePrimaryBrush = staticCompositionLocalOf<Brush> {
    YatteBrushes.Green.Main
}

val LocalYatteEmphasisBrush = staticCompositionLocalOf<Brush> {
    YatteBrushes.Yellow.Main
}
val LocalYatteOnPrimaryBrushColor = staticCompositionLocalOf<Color> {
    Color.White
}

val LocalYatteOnEmphasisBrushColor = staticCompositionLocalOf<Color> {
    Color.White
}

val LocalYatteDangerBrush = staticCompositionLocalOf<Brush> {
    YatteBrushes.Error.Main
}

val LocalYatteOnDangerBrushColor = staticCompositionLocalOf<Color> {
    Color.White
}

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
val YatteSurfaceLight = Color(0xFFF1F8E9) // Light Green 50
val YatteBackgroundLight = Color(0xFFF1F8E9)
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

val YatteBaselineLightColorScheme = lightColorScheme(
    primary = Color(0xFF6750A4), // M3 Baseline Purple
    onPrimary = Color.Black, // Strict: Dark content in Light mode
    primaryContainer = Color(0xFFEADDFF),
    onPrimaryContainer = Color(0xFF21005D),
    secondary = Color(0xFF625B71),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFFE8DEF8),
    onSecondaryContainer = Color(0xFF1D192B),
    tertiary = Color(0xFF7D5260),
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFFFFD8E4),
    onTertiaryContainer = Color(0xFF31111D),
    background = Color(0xFFFFFBFE),
    onBackground = Color.Black,
    surface = Color(0xFFFFFBFE),
    onSurface = Color.Black,
    error = Color(0xFFB3261E),
    onError = Color.Black,
    outline = Color(0xFF79747E),
)

val YatteBaselineDarkColorScheme = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    onPrimary = Color.White, // Strict: Light content in Dark mode
    primaryContainer = Color(0xFF4F378B),
    onPrimaryContainer = Color(0xFFEADDFF),
    secondary = Color(0xFFCCC2DC),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF4A4458),
    onSecondaryContainer = Color(0xFFE8DEF8),
    tertiary = Color(0xFFEFB8C8),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF633B48),
    onTertiaryContainer = Color(0xFFFFD8E4),
    background = Color(0xFF1C1B1F),
    onBackground = Color.White,
    surface = Color(0xFF1C1B1F),
    onSurface = Color.White,
    error = Color(0xFFF2B8B5),
    onError = Color.White,
    outline = Color(0xFF938F99),
)

val YatteGreenLightColorScheme = lightColorScheme(
    // Primary
    primary = YattePrimary,
    onPrimary = Color(0xFF1B2E1B), // Dark green-tinted text
    primaryContainer = YatteMint,
    onPrimaryContainer = YatteForest,
    
    // Secondary (Yellow/Sunshine)
    secondary = YatteSunshine,
    onSecondary = Color(0xFF3E2723),
    secondaryContainer = Color(0xFFFFF8E1),
    onSecondaryContainer = Color(0xFF5D4037),
    
    // Tertiary (Sky Blue)
    tertiary = YatteSky,
    onTertiary = Color(0xFF0D1F2D),
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
    onError = Color.Black,
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

val YatteGreenDarkColorScheme = darkColorScheme(
    // Primary (Brighter for dark mode)
    primary = YattePrimaryDark,
    onPrimary = Color.White, // Force light text in dark mode
    primaryContainer = YatteForest,
    onPrimaryContainer = YatteMint,
    
    // Secondary
    secondary = Color(0xFFFFD54F),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF5D4037),
    onSecondaryContainer = Color(0xFFFFF8E1),
    
    // Tertiary
    tertiary = Color(0xFF64B5F6),
    onTertiary = Color.White,
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
    onError = Color.White,
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

val YatteYellowLightColorScheme = lightColorScheme(
    // Primary (Yellow)
    primary = YatteSunshine,
    onPrimary = Color(0xFF3E2723), // Dark Brown
    primaryContainer = Color(0xFFFFF8E1),
    onPrimaryContainer = Color(0xFF5D4037),
    
    // Secondary (Green/Primary)
    secondary = YattePrimary,
    onSecondary = Color(0xFF1B2E1B),
    secondaryContainer = YatteMint,
    onSecondaryContainer = YatteForest,
    
    // Tertiary (Sky Blue)
    tertiary = YatteSky,
    onTertiary = Color(0xFF0D1F2D),
    tertiaryContainer = Color(0xFFBBDEFB),
    onTertiaryContainer = Color(0xFF0D47A1),
    
    // Background & Surface
    background = Color(0xFFFFFDE7), // Yellow 50
    onBackground = Color(0xFF3E2723),
    surface = Color(0xFFFFF9C4),    // Yellow 100
    onSurface = Color(0xFF3E2723),
    surfaceVariant = Color(0xFFFFFDE7),
    onSurfaceVariant = Color(0xFF5D4037),
    
    // Error
    error = YatteError,
    onError = Color.Black,
    errorContainer = YatteErrorContainer,
    onErrorContainer = YatteOnErrorContainer,
    
    // Outline
    outline = Color(0xFFD7CCC8),
    outlineVariant = Color(0xFFFFF8E1),
    
    // Inverse
    inverseSurface = YatteDeepForest,
    inverseOnSurface = YatteOnBackgroundDark,
    inversePrimary = YatteSunshine,
)

val YatteYellowDarkColorScheme = darkColorScheme(
    // Primary (Yellow - brighter)
    primary = Color(0xFFFFD54F),
    onPrimary = Color.White, // Force light text in dark mode
    primaryContainer = Color(0xFF5D4037),
    onPrimaryContainer = Color(0xFFFFF8E1),
    
    // Secondary (Green)
    secondary = YattePrimaryDark,
    onSecondary = Color.White,
    secondaryContainer = YatteForest,
    onSecondaryContainer = YatteMint,
    
    // Tertiary
    tertiary = Color(0xFF64B5F6),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF0D47A1),
    onTertiaryContainer = Color(0xFFBBDEFB),
    
    // Background & Surface
    background = Color(0xFF212121),
    onBackground = Color.White,
    surface = Color(0xFF323232),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF424242),
    onSurfaceVariant = Color(0xFFFFD54F),
    
    // Error
    error = Color(0xFFEF5350),
    onError = Color.White,
    errorContainer = Color(0xFFB71C1C),
    onErrorContainer = Color(0xFFFFCDD2),
    
    // Outline
    outline = Color(0xFF757575),
    outlineVariant = Color(0xFF424242),
    
    // Inverse
    inverseSurface = Color(0xFFFFFDE7),
    inverseOnSurface = Color(0xFF3E2723),
    inversePrimary = Color(0xFFFFA000),
)

val YatteBlueLightColorScheme = lightColorScheme(
    primary = YatteSky,
    onPrimary = Color(0xFF0D1F2D),
    primaryContainer = Color(0xFFE3F2FD),
    onPrimaryContainer = Color(0xFF1565C0),
    secondary = YatteLeaf,
    onSecondary = Color(0xFF1B2E1B),
    background = Color(0xFFF0F7FF),
    onBackground = Color(0xFF0D1F2D),
    surface = Color(0xFFF0F7FF),
    onSurface = Color(0xFF0D1F2D),
    error = YatteError,
    onError = Color.Black,
)

val YatteBlueDarkColorScheme = darkColorScheme(
    primary = Color(0xFF90CAF9),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1565C0),
    onPrimaryContainer = Color(0xFFE3F2FD),
    secondary = YattePrimaryDark,
    onSecondary = Color.White,
    background = Color(0xFF0A1929),
    onBackground = Color.White,
    surface = Color(0xFF132F4C),
    onSurface = Color.White,
    error = Color(0xFFEF5350),
    onError = Color.White,
)

val YatteOrangeLightColorScheme = lightColorScheme(
    primary = YatteCoral,
    onPrimary = Color(0xFF2E1B0D),
    primaryContainer = Color(0xFFFFF3E0),
    onPrimaryContainer = Color(0xFFE65100),
    secondary = YatteLeaf,
    onSecondary = Color(0xFF1B2E1B),
    background = Color(0xFFFFFBF0),
    onBackground = Color(0xFF2E1B0D),
    surface = Color(0xFFFFFBF0),
    onSurface = Color(0xFF2E1B0D),
    error = YatteError,
    onError = Color.Black,
)

val YatteOrangeDarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFAB91),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE64A19),
    onPrimaryContainer = Color(0xFFFFF3E0),
    secondary = YattePrimaryDark,
    onSecondary = Color.White,
    background = Color(0xFF1F0D0D),
    onBackground = Color.White,
    surface = Color(0xFF2E1A1A),
    onSurface = Color.White,
    error = Color(0xFFEF5350),
    onError = Color.White,
)

val YattePurpleLightColorScheme = lightColorScheme(
    primary = YatteBerry,
    onPrimary = Color(0xFF1F0D2E),
    primaryContainer = Color(0xFFF3E5F5),
    onPrimaryContainer = Color(0xFF7B1FA2),
    secondary = YatteLeaf,
    onSecondary = Color(0xFF1B2E1B),
    background = Color(0xFFFBF0FF),
    onBackground = Color(0xFF1F0D2E),
    surface = Color(0xFFFBF0FF),
    onSurface = Color(0xFF1F0D2E),
    error = YatteError,
    onError = Color.Black,
)

val YattePurpleDarkColorScheme = darkColorScheme(
    primary = Color(0xFFCE93D8),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF7B1FA2),
    onPrimaryContainer = Color(0xFFF3E5F5),
    secondary = YattePrimaryDark,
    onSecondary = Color.White,
    background = Color(0xFF1A0D1F),
    onBackground = Color.White,
    surface = Color(0xFF2E1A3D),
    onSurface = Color.White,
    error = Color(0xFFEF5350),
    onError = Color.White,
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

/**
 * Yatte Gradient Brushes
 * "Colors are not increased, but expressions are varied using Brushes."
 */
object YatteBrushes {
    
    // ============================================================
    // Unified 1:8:1 Ratio System
    // Highlight (10%) -> Body (80%) -> Shadow (10%)
    // 
    // 「心地よいフィードバック」を実現するための
    // 統一された3Dボタン効果システム
    // ============================================================
    
    /**
     * 1:8:1比率でグラデーションを生成するヘルパー関数
     * @param highlight 上部のハイライト色（10%）
     * @param body メインのボディ色（80%）
     * @param shadow 下部のシャドウ色（10%）
     */
    private fun create181Gradient(
        highlight: Color,
        body: Color,
        shadow: Color
    ) = Brush.verticalGradient(
        0.0f to highlight,
        0.1f to body,
        0.9f to body,
        1.0f to shadow
    )
    
    // ============================================================
    // Baseline Variants (デフォルトテーマ)
    // 
    // 用途: デフォルトのアプリ外観、ニュートラルな状態
    // ============================================================
    object Baseline {
        val Main = create181Gradient(
            highlight = Color(0xFFEADDFF),
            body = Color(0xFF6750A4),
            shadow = Color(0xFF4F378B)
        )
        val Light = create181Gradient(
            highlight = Color(0xFFF3EDFF),
            body = Color(0xFFD0BCFF),
            shadow = Color(0xFF6750A4)
        )
        val Dark = create181Gradient(
            highlight = Color(0xFF6750A4),
            body = Color(0xFF4F378B),
            shadow = Color(0xFF381E72)
        )
    }

    // ============================================================
    // Green Variants (テーマカラー / プライマリ)
    // 
    // 用途: メインアクション、成功状態、肯定的なフィードバック
    // Docs: "緑を基調としつつ、黄色で活気を出す"
    // ============================================================
    object Green {
        /**
         * Main - 標準グリーン
         * 
         * 使用場面:
         * - プライマリボタン (YatteButton)
         * - セグメントボタンの選択状態
         * - タスク完了チェックボックス
         * - メインのCTA (Call to Action)
         * 
         * @see YatteColors.primary (#4CAF50)
         */
        val Main = create181Gradient(
            highlight = YatteColors.mint,
            body = YatteColors.primary,
            shadow = Color(0xFF3A8F3E)
        )
        
        /**
         * Light - 明るく柔らかい
         * 
         * 使用場面:
         * - ホバー状態 / サブアクション
         * - セカンダリボタン
         * - 背景アクセント
         * - 選択状態のハイライト
         * 
         * @see YatteColors.leaf (#81C784)
         */
        val Light = create181Gradient(
            highlight = Color(0xFFF1F8E9),
            body = YatteColors.leaf,
            shadow = YatteColors.primary
        )
        
        /**
         * Vivid - 鮮やかで目立つ
         * 
         * 使用場面:
         * - 重要アクションの強調
         * - プロモーション的なボタン
         * - ゲーミフィケーション要素（レベルアップなど）
         * - 注目を集めたいUI要素
         * 
         * 注意: 控えめに使用すること
         */
        val Vivid = create181Gradient(
            highlight = YatteColors.leaf,
            body = Color(0xFF43A047),
            shadow = YatteColors.forest
        )
        
        /**
         * Dark - 深いフォレスト
         * 
         * 使用場面:
         * - ヘッダーやナビゲーション
         * - 重要アクションの背景
         * - ダークモードでの強調要素
         * - プレミアム感を出したい場面
         * 
         * @see YatteColors.forest (#2E7D32)
         */
        val Dark = create181Gradient(
            highlight = YatteColors.primary,
            body = YatteColors.forest,
            shadow = Color(0xFF1B5E20)
        )
        
        /**
         * Muted - 彩度低め、落ち着いた
         * 
         * 使用場面:
         * - 無効状態（Disabled）のボタン
         * - 控えめなセカンダリ要素
         * - 読み込み中やプレースホルダー
         * - 背景に溶け込ませたい要素
         * 
         * 注意: コントラストが低いため、アクセシビリティに注意
         */
        val Muted = create181Gradient(
            highlight = Color(0xFFE8F5E9),
            body = Color(0xFF81C784).copy(alpha = 0.8f),
            shadow = Color(0xFF66BB6A)
        )
    }
    
    // ============================================================
    // Yellow Variants (アクセントカラー)
    // 
    // 用途: 注意、ハイライト、達成感、ゲーミフィケーション
    // Docs: "Sunshine - 注意、ハイライト、達成"
    // ============================================================
    object Yellow {
        /**
         * Main - 標準イエロー
         * 
         * 使用場面:
         * - 達成・報酬のハイライト（XP獲得、バッジなど）
         * - 注意を引きたい非破壊的アクション
         * - ストリーク継続の表示
         * - 「新着」「おすすめ」バッジ
         * 
         * @see YatteColors.sunshine (#FFC107)
         */
        val Main = create181Gradient(
            highlight = Color(0xFFFFF9C4),
            body = YatteColors.sunshine,
            shadow = YatteColors.honey
        )
        
        /**
         * Light - 明るくソフト
         * 
         * 使用場面:
         * - 控えめなハイライト
         * - 背景アクセント
         * - ホバー状態
         * - 軽い注意を促す要素
         * 
         * 注意: 白背景では見えにくいため、ボーダー併用推奨
         */
        val Light = create181Gradient(
            highlight = Color(0xFFFFFDE7),
            body = Color(0xFFFFF59D),
            shadow = YatteColors.sunshine
        )
        
        /**
         * Vivid - 鮮やかで注目を引く
         * 
         * 使用場面:
         * - 重要な報酬・達成の強調（レベルアップ、新記録）
         * - CTAボタン（ただしGreenがメイン）
         * - ゲーミフィケーション要素の強調
         * - 期間限定・特別なアクション
         * 
         * 注意: 多用すると効果が薄れる、ここぞという場面で使用
         */
        val Vivid = create181Gradient(
            highlight = Color(0xFFFFFF8D), // Fixed: was same as body, now lighter yellow
            body = Color(0xFFFFC107),
            shadow = Color(0xFFFF8F00)
        )
        
        /**
         * Dark - 深いアンバー
         * 
         * 使用場面:
         * - 警告状態（Warning）
         * - 進行中タスクの強調
         * - 期限が近いことを示す
         * - オレンジ寄りのアクセント
         * 
         * @see YatteColors.honey (#FFB300)
         */
        val Dark = create181Gradient(
            highlight = YatteColors.honey,
            body = Color(0xFFFF8F00),
            shadow = Color(0xFFE65100)
        )
        
        // 後方互換性のためのエイリアス
        /** @deprecated Use Main instead */
        val Action = Main
        /** @deprecated Use Vivid instead */
        val Glossy = Vivid
    }

    object Blue {
        val Main = create181Gradient(
            highlight = Color(0xFFBBDEFB),
            body = YatteColors.sky,
            shadow = Color(0xFF1976D2)
        )
        val Light = create181Gradient(
            highlight = Color(0xFFE3F2FD),
            body = Color(0xFF90CAF9),
            shadow = YatteColors.sky
        )
        val Dark = create181Gradient(
            highlight = YatteColors.sky,
            body = Color(0xFF1565C0),
            shadow = Color(0xFF0D47A1)
        )
    }

    object Orange {
        val Main = create181Gradient(
            highlight = Color(0xFFFFCCBC),
            body = YatteColors.coral,
            shadow = Color(0xFFE64A19)
        )
        val Light = create181Gradient(
            highlight = Color(0xFFFFF3E0),
            body = Color(0xFFFFAB91),
            shadow = YatteColors.coral
        )
        val Dark = create181Gradient(
            highlight = YatteColors.coral,
            body = Color(0xFFD84315),
            shadow = Color(0xFFBF360C)
        )
    }

    object Purple {
        val Main = create181Gradient(
            highlight = Color(0xFFE1BEE7),
            body = YatteColors.berry,
            shadow = Color(0xFF7B1FA2)
        )
        val Light = create181Gradient(
            highlight = Color(0xFFF3E5F5),
            body = Color(0xFFCE93D8),
            shadow = YatteColors.berry
        )
        val Dark = create181Gradient(
            highlight = YatteColors.berry,
            body = Color(0xFF6A1B9A),
            shadow = Color(0xFF4A148C)
        )
    }

    
    // ============================================================
    // Horizontal Variants (ヘッダー/ナビゲーション用)
    // 
    // 用途: 横方向のグラデーションが必要な場面
    // ============================================================
    // ============================================================
    // Error Variants (エラー・警告・破壊的アクション)
    // 
    // 用途: エラー状態、削除、データリセット
    // ============================================================
    object Error {
        val Main = create181Gradient(
            highlight = Color(0xFFFFCDD2),
            body = YatteError,
            shadow = Color(0xFFC62828)
        )
        val Light = create181Gradient(
            highlight = Color(0xFFFFEBEE),
            body = Color(0xFFEF9A9A),
            shadow = YatteError
        )
        val Dark = create181Gradient(
            highlight = YatteError,
            body = Color(0xFFC62828),
            shadow = Color(0xFFB71C1C)
        )
    }

    object Horizontal {
        /**
         * Header - ヘッダー/ナビゲーション用の水平グラデーション
         * 
         * 使用場面:
         * - YatteFloatingHeader（グラデーションモード）
         * - 横長バナー
         * - プログレスバー
         * 
         * @see YatteColors.forest, YatteColors.primary
         */
        val Header = Brush.horizontalGradient(
            colors = listOf(
                YatteColors.forest,
                YatteColors.primary,
            )
        )
    }
}

