package com.segnities007.yatte.presentation.core.sound

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual object RingtoneUtils : KoinComponent {
    private val context: Context by inject()

    actual fun getRingtoneTitle(uri: String): String? {
        return try {
            val ringtoneUri = Uri.parse(uri)
            val ringtone = RingtoneManager.getRingtone(context, ringtoneUri)
            ringtone?.getTitle(context)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
