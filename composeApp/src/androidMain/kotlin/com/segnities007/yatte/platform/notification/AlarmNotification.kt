package com.segnities007.yatte.platform.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.segnities007.yatte.platform.alarm.AlarmConstants
import com.segnities007.yatte.platform.alarm.AlarmReceiver

internal object AlarmNotification {
    private const val CHANNEL_ID = "yatte_alarm"
    private const val CHANNEL_NAME = "Yatte Alarms"

    fun show(
        context: Context,
        title: String,
        content: String,
        soundUri: String? = null,
        isSoundEnabled: Boolean = true,
        isVibrationEnabled: Boolean = true,
        alarmId: String,
        taskId: String,
    ) {
        val channelId = getChannelId(soundUri, isSoundEnabled, isVibrationEnabled)
        ensureChannel(context, channelId, soundUri, isSoundEnabled, isVibrationEnabled)

        // Android 13+ では通知権限が必要
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted =
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED
            if (!granted) return
        }

        val builder =
            NotificationCompat
                .Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

        // Extend Action (10分延長)
        val extendIntent =
            Intent(context, AlarmReceiver::class.java).apply {
                action = AlarmConstants.ACTION_ALARM_EXTEND
                putExtra(AlarmConstants.EXTRA_ALARM_ID, alarmId)
                putExtra(AlarmConstants.EXTRA_TASK_TITLE, taskId) // reusing key for taskId
            }
        val extendPendingIntent =
            PendingIntent.getBroadcast(
                context,
                (alarmId + "extend").hashCode(),
                extendIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
        builder.addAction(android.R.drawable.ic_menu_recent_history, "10分後に通知", extendPendingIntent)

        // Complete Action
        val completeIntent =
            Intent(context, AlarmReceiver::class.java).apply {
                action = AlarmConstants.ACTION_ALARM_COMPLETE
                putExtra(AlarmConstants.EXTRA_ALARM_ID, alarmId)
                putExtra(AlarmConstants.EXTRA_TASK_TITLE, taskId) // reusing key for taskId
            }
        val completePendingIntent =
            PendingIntent.getBroadcast(
                context,
                (alarmId + "complete").hashCode(),
                completeIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
        builder.addAction(android.R.drawable.checkbox_on_background, "完了", completePendingIntent)

        if (isSoundEnabled && soundUri != null) {
            builder.setSound(android.net.Uri.parse(soundUri))
        }

        if (isVibrationEnabled) {
            builder.setVibrate(longArrayOf(0, 500, 200, 500))
        }

        NotificationManagerCompat.from(context).notify(title.hashCode(), builder.build())
    }

    private fun getChannelId(
        soundUri: String?,
        isSoundEnabled: Boolean,
        isVibrationEnabled: Boolean,
    ): String =
        if (isSoundEnabled && soundUri != null) {
            "yatte_alarm_custom_${soundUri.hashCode()}_$isVibrationEnabled"
        } else if (!isSoundEnabled) {
            "yatte_alarm_silent_$isVibrationEnabled"
        } else {
            "yatte_alarm_default_$isVibrationEnabled"
        }

    private fun ensureChannel(
        context: Context,
        channelId: String,
        soundUri: String?,
        isSoundEnabled: Boolean,
        isVibrationEnabled: Boolean,
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 既にチャンネルが存在する場合は何もしない（設定変更検知のためIDを変えているので）
        if (manager.getNotificationChannel(channelId) != null) return

        val channel =
            NotificationChannel(
                channelId,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH,
            )

        val audioAttributes =
            android.media.AudioAttributes
                .Builder()
                .setUsage(android.media.AudioAttributes.USAGE_ALARM)
                .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

        if (isSoundEnabled) {
            if (soundUri != null) {
                channel.setSound(android.net.Uri.parse(soundUri), audioAttributes)
            }
            // defaultはOSデフォルトが使われるのでsetSound不要 or DEFAULT_ALARM_ALERT_URI
        } else {
            channel.setSound(null, null)
        }

        channel.enableVibration(isVibrationEnabled)
        if (isVibrationEnabled) {
            channel.vibrationPattern = longArrayOf(0, 500, 200, 500)
        }

        manager.createNotificationChannel(channel)
    }
}
