package com.example.tempstructure.utils.extensions

import kotlin.math.log10
import kotlin.math.pow

fun Long.formatFileSize(): String {
    if (this <= 0) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (log10(this.toDouble()) / log10(1024.0)).toInt()
    return String.format(
        "%.0f%s", this / 1024.0.pow(digitGroups.toDouble()),
        units[digitGroups]
    )
}
