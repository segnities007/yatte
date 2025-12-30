package com.segnities007.yatte.presentation.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ============================================================
// Yatte Typography
// Design Principle: Nunito - 丸みがあり柔らかい、フレンドリー
// ============================================================

/**
 * Nunitoフォントファミリー
 * 
 * セットアップ手順:
 * 1. Google Fonts から Nunito をダウンロード
 * 2. 以下のファイルを配置:
 *    presentation/designsystem/src/commonMain/composeResources/font/
 *    - nunito_regular.ttf
 *    - nunito_medium.ttf
 *    - nunito_semibold.ttf
 *    - nunito_bold.ttf
 * 3. 以下のコメントを解除
 * 
 * TODO: フォントファイル追加後にコメント解除
 */
// val NunitoFontFamily = FontFamily(
//     Font(Res.font.nunito_regular, FontWeight.Normal),
//     Font(Res.font.nunito_medium, FontWeight.Medium),
//     Font(Res.font.nunito_semibold, FontWeight.SemiBold),
//     Font(Res.font.nunito_bold, FontWeight.Bold),
// )

/**
 * 現在使用するフォントファミリー
 * Nunitoフォント追加後は NunitoFontFamily に変更
 */
val YatteFontFamily: FontFamily = FontFamily.Default

/**
 * Yatteアプリのタイポグラフィトークン
 *
 * 特徴:
 * - ゆったりとした行間 (lineHeight)
 * - 読みやすい文字間隔 (letterSpacing)
 */
@Immutable
data class YatteTypographyTokens(
    // Display - ゲームオーバー的な大きな表示
    val displayLarge: TextStyle = TextStyle(
        fontFamily = YatteFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
    ),
    val displayMedium: TextStyle = TextStyle(
        fontFamily = YatteFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
    ),
    val displaySmall: TextStyle = TextStyle(
        fontFamily = YatteFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
    ),

    // Headline - 画面タイトル、セクションタイトル
    val headlineLarge: TextStyle = TextStyle(
        fontFamily = YatteFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
    ),
    val headlineMedium: TextStyle = TextStyle(
        fontFamily = YatteFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),
    val headlineSmall: TextStyle = TextStyle(
        fontFamily = YatteFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),

    // Title - カードタイトル、リスト項目タイトル
    val titleLarge: TextStyle = TextStyle(
        fontFamily = YatteFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    val titleMedium: TextStyle = TextStyle(
        fontFamily = YatteFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    ),
    val titleSmall: TextStyle = TextStyle(
        fontFamily = YatteFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),

    // Body - 本文（ゆったりした行間）
    val bodyLarge: TextStyle = TextStyle(
        fontFamily = YatteFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 26.sp, // ゆったり: 1.6em
        letterSpacing = 0.5.sp,
    ),
    val bodyMedium: TextStyle = TextStyle(
        fontFamily = YatteFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp, // ゆったり
        letterSpacing = 0.25.sp,
    ),
    val bodySmall: TextStyle = TextStyle(
        fontFamily = YatteFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.4.sp,
    ),

    // Label - ラベル、バッジ
    val labelLarge: TextStyle = TextStyle(
        fontFamily = YatteFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    val labelMedium: TextStyle = TextStyle(
        fontFamily = YatteFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    val labelSmall: TextStyle = TextStyle(
        fontFamily = YatteFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
)

/**
 * Material3 Typography変換
 */
fun YatteTypographyTokens.toMaterial3Typography(): Typography = Typography(
    displayLarge = displayLarge,
    displayMedium = displayMedium,
    displaySmall = displaySmall,
    headlineLarge = headlineLarge,
    headlineMedium = headlineMedium,
    headlineSmall = headlineSmall,
    titleLarge = titleLarge,
    titleMedium = titleMedium,
    titleSmall = titleSmall,
    bodyLarge = bodyLarge,
    bodyMedium = bodyMedium,
    bodySmall = bodySmall,
    labelLarge = labelLarge,
    labelMedium = labelMedium,
    labelSmall = labelSmall,
)

/**
 * デフォルトタイポグラフィ
 */
val YatteTypography = YatteTypographyTokens()

/**
 * CompositionLocal for typography tokens
 */
val LocalYatteTypography = staticCompositionLocalOf { YatteTypographyTokens() }

/**
 * 現在のタイポグラフィトークンを取得
 */
val typography: YatteTypographyTokens
    @Composable
    get() = LocalYatteTypography.current

