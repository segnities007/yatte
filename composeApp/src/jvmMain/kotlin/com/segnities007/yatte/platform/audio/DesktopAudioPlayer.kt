package com.segnities007.yatte.platform.audio

import java.awt.Toolkit
import java.io.File
import java.util.logging.Level
import java.util.logging.Logger
import javax.sound.sampled.AudioSystem

object DesktopAudioPlayer {
    private val logger =
        Logger.getLogger(DesktopAudioPlayer::class.java.name)

    fun play(uri: String?) {
        try {
            if (uri != null) {
                // URIがファイルパスであると仮定
                val file = File(uri)
                if (file.exists()) {
                    val audioInputStream = AudioSystem.getAudioInputStream(file)
                    val clip = AudioSystem.getClip()
                    clip.open(audioInputStream)
                    clip.start()
                } else {
                    logger.warning("Audio file not found: $uri")
                    playDefaultBeep()
                }
            } else {
                playDefaultBeep()
            }
        } catch (e: Exception) {
            logger.log(Level.SEVERE, "Failed to play audio", e)
            // フォーマット非対応などの場合はビープ音にフォールバック
            playDefaultBeep()
        }
    }

    private fun playDefaultBeep() {
        Toolkit
            .getDefaultToolkit()
            .beep()
    }
}
