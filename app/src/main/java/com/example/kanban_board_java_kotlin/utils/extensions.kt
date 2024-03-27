package com.example.kanban_board_java_kotlin.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Locale

fun Long.formatString(format: String = "dd/MM/yyyy"): String {
    return try {
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        simpleDateFormat.format(this)
    } catch (e: Exception) {
        ""
    }
}

fun Long.formatSecondToString(): String {
    return try {
        val seconds = (this / 1000) % 60
        val minutes = ((this / (1000 * 60)) % 60)
        val hours = ((this / (1000 * 60 * 60)) % 24)

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    } catch (e: Exception) {
        ""
    }
}

fun Long.getSecondBetweenTime(time2: Long): Long {
    return try {
        (time2 - (this/1000));
    } catch (e: Exception) {
        0
    }
}

fun String.formatLong(format: String = "dd/MM/yyyy"): Long {
    return try {
        val simpleDateFormat = SimpleDateFormat(format)
        simpleDateFormat.parse(this)?.time ?: 0
    } catch (e: Exception) {
        Log.e("Error", "String.formatLong ->> $this ->> $format")
        0
    }
}