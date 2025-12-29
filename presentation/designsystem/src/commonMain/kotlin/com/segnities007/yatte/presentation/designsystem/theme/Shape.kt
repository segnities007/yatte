package com.segnities007.yatte.presentation.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// "Nintendo Quality" - Playful & Soft shapes
val YatteShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp), // Buttons, Cards
    large = RoundedCornerShape(24.dp), // Large Cards, Dialogs
    extraLarge = RoundedCornerShape(32.dp)
)

// TODO: Implement actual Squircle shape implementation if strict "Super Ellipse" is required.
// For now, using RoundedCornerShape with generous radii to approximate the feel.
val SquircleShape = RoundedCornerShape(20) // Percentage or specific logic can be added here
