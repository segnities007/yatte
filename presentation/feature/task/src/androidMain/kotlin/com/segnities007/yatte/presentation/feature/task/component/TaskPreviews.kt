package com.segnities007.yatte.presentation.feature.task.component

import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalTime
import androidx.compose.ui.tooling.preview.Preview



@Preview
@Composable
private fun TaskTimePickerSheetPreview() {
    TaskTimePickerSheet(
        initialTime = LocalTime(10, 0),
        onDismiss = {},
        onConfirm = {}
    )
}

