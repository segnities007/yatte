package com.segnities007.yatte.platform.notification

import java.awt.Image
import java.awt.SystemTray
import java.awt.TrayIcon
import java.awt.TrayIcon.MessageType

object DesktopNotification {
    private val logger =
        java.util.logging.Logger
            .getLogger(DesktopNotification::class.java.name)
    private var trayIcon: TrayIcon? = null

    init {
        if (SystemTray.isSupported()) {
            val tray = SystemTray.getSystemTray()
            // アイコン画像が必要だが、一旦ダミーで作成するか、リソースから読み込む必要がある。
            // ここでは簡易的に1x1の画像を作成して使用する
            val image: Image = java.awt.image.BufferedImage(1, 1, java.awt.image.BufferedImage.TYPE_INT_ARGB)
            trayIcon = TrayIcon(image, "Yatte")
            trayIcon?.isImageAutoSize = true
            try {
                tray.add(trayIcon)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun show(
        title: String,
        content: String,
    ) {
        if (SystemTray.isSupported()) {
            trayIcon?.displayMessage(title, content, MessageType.INFO)
        } else {
            logger.warning("SystemTray is not supported. Notification: [$title] $content")
        }
    }
}
