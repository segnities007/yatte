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
import com.segnities007.yatte.domain.aggregate.settings.model.VibrationPattern
import com.segnities007.yatte.platform.alarm.AlarmConstants
import com.segnities007.yatte.platform.alarm.AlarmReceiver

internal object AlarmNotification {
    private const val CHANNEL_ID_PREFIX = "yatte_alarm"
    private const val CHANNEL_VERSION = "v2" // Versioning to force update immutable channels
    private const val CHANNEL_NAME = "Yatte Alarms"

    fun show(
        context: Context,
        title: String,
        content: String,
        soundUri: String? = null,
        isSoundEnabled: Boolean = true,
        isVibrationEnabled: Boolean = true,
        snoozeDuration: Int = 10,
        vibrationPattern: VibrationPattern = VibrationPattern.NORMAL,
        alarmId: String,
        taskId: String,
    ) {
        val channelId = getChannelId(soundUri, isSoundEnabled, isVibrationEnabled, vibrationPattern)
        ensureChannel(context, channelId, soundUri, isSoundEnabled, isVibrationEnabled, vibrationPattern)

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

        val intent =
            Intent(context, com.segnities007.yatte.MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        val pendingIntent =
            PendingIntent.getActivity(
                context,
                title.hashCode(),
                intent,
                PendingIntent.FLAG_IMMUTABLE,
            )
        builder.setContentIntent(pendingIntent)

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
        builder.addAction(android.R.drawable.ic_popup_reminder, "スヌーズ (${snoozeDuration}分)", extendPendingIntent)

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
            try {
                builder.setSound(android.net.Uri.parse(soundUri))
            } catch (e: Exception) {
                e.printStackTrace()
                // フォールバックは設定しない（デフォルト音が鳴るか無音になるかは機種依存だが、アプリはクラッシュしない）
            }
        }

        if (isVibrationEnabled) {
            builder.setVibrate(getVibrationPattern(vibrationPattern))

            // Backup: Explicitly trigger vibration service
            // NotificationChannel vibration can be unreliable due to immutability or system settings.
            // We force a manual vibration here as a fallback/reinforcement.
            try {
                val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as android.os.VibratorManager
                    manager.defaultVibrator
                } else {
                    @Suppress("DEPRECATION")
                    context.getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator
                }

                if (vibrator.hasVibrator()) {
                    val pattern = getVibrationPattern(vibrationPattern)
                    // API 26+
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val effect = android.os.VibrationEffect.createWaveform(pattern, -1)
                        
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                             val attributes = android.os.VibrationAttributes.Builder()
                                .setUsage(android.os.VibrationAttributes.USAGE_ALARM)
                                .build()
                            vibrator.vibrate(effect, attributes)
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                             val attributes = android.os.VibrationAttributes.Builder()
                                .setUsage(android.os.VibrationAttributes.USAGE_ALARM)
                                .build()
                            vibrator.vibrate(effect, attributes)
                        } else {
                            val audioAttrs = android.media.AudioAttributes.Builder()
                                .setUsage(android.media.AudioAttributes.USAGE_ALARM)
                                .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                .build()
                            @Suppress("DEPRECATION")
                            vibrator.vibrate(effect, audioAttrs)
                        }
                    } else {
                        // Pre-Oreo
                        @Suppress("DEPRECATION")
                        vibrator.vibrate(pattern, -1)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        NotificationManagerCompat.from(context).notify(title.hashCode(), builder.build())
    }

    private fun getChannelId(
        soundUri: String?,
        isSoundEnabled: Boolean,
        isVibrationEnabled: Boolean,
        vibrationPattern: VibrationPattern,
    ): String {
        val baseId = if (isSoundEnabled && !soundUri.isNullOrBlank()) {
            "custom_${soundUri.hashCode()}"
        } else if (!isSoundEnabled) {
            "silent"
        } else {
            "default"
        }
        
        return "${CHANNEL_ID_PREFIX}_${baseId}_${isVibrationEnabled}_${vibrationPattern.name}_$CHANNEL_VERSION"
    }

    private fun ensureChannel(
        context: Context,
        channelId: String,
        soundUri: String?,
        isSoundEnabled: Boolean,
        isVibrationEnabled: Boolean,
        vibrationPattern: VibrationPattern,
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create new channel
        if (manager.getNotificationChannel(channelId) != null) return

        // Cleanup old channels if necessary (Optional, simplistic approach)
        // In a real app, you might iterate and delete all starting with prefix but not ending with current version
        // manager.deleteNotificationChannel("old_id")

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
            val uri = if (soundUri != null) {
                android.net.Uri.parse(soundUri)
            } else {
                android.provider.Settings.System.DEFAULT_ALARM_ALERT_URI
            }

            try {
                channel.setSound(uri, audioAttributes)
            } catch (e: Exception) {
                e.printStackTrace()
                // フォールバック: デフォルトのアラーム音を設定
                channel.setSound(android.provider.Settings.System.DEFAULT_ALARM_ALERT_URI, audioAttributes)
            }
        } else {
            channel.setSound(null, null)
        }

        channel.enableVibration(isVibrationEnabled)
        if (isVibrationEnabled) {
            channel.vibrationPattern = getVibrationPattern(vibrationPattern)
        }

        manager.createNotificationChannel(channel)
    }

    private fun getVibrationPattern(pattern: VibrationPattern): LongArray =
        when (pattern) {
            VibrationPattern.NORMAL -> longArrayOf(0, 500, 200, 500)
            VibrationPattern.SHORT -> longArrayOf(0, 200, 200, 200)
            VibrationPattern.LONG -> longArrayOf(0, 1000, 500, 1000)
            VibrationPattern.SOS ->
                longArrayOf(
                    0,
                    200,
                    200,
                    200,
                    200,
                    200,
                    500,
                    500,
                    200,
                    500,
                    200,
                    500,
                    500,
                    200,
                    200,
                    200,
                    200,
                    200,
                    200,
                )
            VibrationPattern.HEARTBEAT -> longArrayOf(0, 100, 100, 100)
        }
}
