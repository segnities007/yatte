package com.segnities007.yatte.presentation.designsystem.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

// ============================================================
// Yatte Shapes
// Design Principle: 「少し〜大きく丸め」柔らかくて遊び心のある形状
// ============================================================

/**
 * Material3 Shapes - コンポーネント自動適用用
 */
val YatteShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),   // Chips, badges
    small = RoundedCornerShape(12.dp),       // Small cards, buttons
    medium = RoundedCornerShape(16.dp),      // Cards, text fields
    large = RoundedCornerShape(24.dp),       // Dialogs, large cards
    extraLarge = RoundedCornerShape(32.dp),  // Floating navigation, sheets
)

/**
 * Yatte拡張シェイプトークン
 * MaterialTheme.shapes に含まれない追加のシェイプを提供
 */
object YatteShapeTokens {
    /** 8dp - チップ、バッジ */
    val small: Shape = RoundedCornerShape(8.dp)
    
    /** 12dp - 小さなカード、ボタン */
    val medium: Shape = RoundedCornerShape(12.dp)
    
    /** 16dp - カード、テキストフィールド */
    val large: Shape = RoundedCornerShape(16.dp)
    
    /** 24dp - ダイアログ、大きなカード */
    val extraLarge: Shape = RoundedCornerShape(24.dp)
    
    /** 32dp - フローティングナビゲーション */
    val navigation: Shape = RoundedCornerShape(32.dp)
    
    /** 完全な円 - アバター、丸ボタン */
    val circle: Shape = CircleShape
    
    /** カプセル型 - ピル型ボタン */
    val capsule: Shape = RoundedCornerShape(50)
}

