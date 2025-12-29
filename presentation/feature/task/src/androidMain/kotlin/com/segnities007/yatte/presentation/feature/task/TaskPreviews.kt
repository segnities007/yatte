package com.segnities007.yatte.presentation.feature.task

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.core.sound.SoundPickerLauncher
import kotlinx.datetime.LocalTime

@Preview
@Composable
private fun TaskFormContentPreview() {
    MaterialTheme {
        TaskFormContent(
            state = TaskFormState(
                title = "Buying Groceries",
                time = LocalTime(10, 0),
            ),
            onIntent = {},
            soundPickerLauncher = object : SoundPickerLauncher {
                override fun launch() {}
            },
            contentPadding = PaddingValues(0.dp)
        )
    }
}
