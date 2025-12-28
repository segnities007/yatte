package com.segnities007.yatte.presentation.core.sound

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberSoundPickerLauncher(
    onResult: (String?) -> Unit,
): SoundPickerLauncher {
    return remember {
        object : SoundPickerLauncher {
            override fun launch() {
                // iOS implementation pending
                println("SoundPicker not implemented for iOS")
            }
        }
    }
}
