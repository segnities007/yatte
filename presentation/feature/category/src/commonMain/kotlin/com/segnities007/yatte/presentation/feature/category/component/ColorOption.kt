package com.segnities007.yatte.presentation.feature.category.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.domain.aggregate.category.model.CategoryColor
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick

@Composable
fun ColorOption(
    color: CategoryColor,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(ColorOptionSize)
            .clip(CircleShape)
            .background(Color(color.hex))
            .then(
                if (isSelected) {
                    Modifier.border(ColorOptionBorderWidth, MaterialTheme.colorScheme.primary, CircleShape)
                } else {
                    Modifier
                },
            )
            .bounceClick(onTap = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (isSelected) {
            Icon(
                Icons.Default.Check,
                contentDescription = "選択済み",
                tint = Color.Black.copy(alpha = 0.7f),
                modifier = Modifier.size(ColorOptionIconSize),
            )
        }
    }
}

private val ColorOptionSize = 40.dp
private val ColorOptionIconSize = 20.dp
private val ColorOptionBorderWidth = 2.dp
