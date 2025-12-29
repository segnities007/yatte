package com.segnities007.yatte.presentation.feature.task.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.datetime.LocalTime


@Preview
@Composable
private fun TaskTimePickerSheetPreview() {
    TaskTimePickerSheet(
        initialTime = LocalTime(10, 0),
        onDismiss = {},
        onConfirm = {}
    )
}

