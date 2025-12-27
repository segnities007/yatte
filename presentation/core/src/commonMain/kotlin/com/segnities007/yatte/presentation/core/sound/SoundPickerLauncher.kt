package com.segnities007.yatte.presentation.core.sound

import androidx.compose.runtime.Composable

interface SoundPickerLauncher {
    fun launch()
}

@Composable
expect fun rememberSoundPickerLauncher(
    onResult: (String?) -> Unit,
): SoundPickerLauncher
