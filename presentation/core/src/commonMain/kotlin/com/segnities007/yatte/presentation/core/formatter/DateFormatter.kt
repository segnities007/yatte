package com.segnities007.yatte.presentation.core.formatter

import kotlinx.datetime.LocalDateTime

object DateFormatter {
    fun formatDateTime(dateTime: LocalDateTime): String {
        val date = dateTime.date
        val hour = dateTime.hour.toString().padStart(2, '0')
        val minute = dateTime.minute.toString().padStart(2, '0')
        return "$date $hour:$minute"
    }

    fun formatTime(dateTime: LocalDateTime): String {
        val hour = dateTime.hour.toString().padStart(2, '0')
        val minute = dateTime.minute.toString().padStart(2, '0')
        return "$hour:$minute"
    }
}
