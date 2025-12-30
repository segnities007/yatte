package com.segnities007.yatte.presentation.designsystem.component.input

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick

/**
 * Yatte統一セグメントボタンコンポーネント
 */
@Composable
fun <T> YatteSegmentedButtonRow(
    options: List<T>,
    selectedIndex: Int,
    onOptionSelected: (Int, T) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit,
) {
    SingleChoiceSegmentedButtonRow(modifier = modifier.fillMaxWidth()) {
        options.forEachIndexed { index, option ->
            SegmentedButton(
                selected = selectedIndex == index,
                onClick = { onOptionSelected(index, option) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                modifier = Modifier.bounceClick(),
                label = { content(option) }
            )
        }
    }
}

@Preview
@Composable
private fun YatteSegmentedButtonRowPreview() {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("Single", "Weekly")
    Column(modifier = Modifier.padding(16.dp)) {
        YatteSegmentedButtonRow(
            options = options,
            selectedIndex = selectedIndex,
            onOptionSelected = { index, _ -> selectedIndex = index },
            content = { Text(it) },
        )
    }
}
