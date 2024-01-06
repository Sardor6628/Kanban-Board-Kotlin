package com.example.tempstructure.utils.extensions

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

///////////////////////////////////////////////////////////////////////////
// functions used in this project
///////////////////////////////////////////////////////////////////////////

fun TextView.showBirthDatePickerDialog(
    dateFormat: String,
    yearInPast: Int? = null,
    doAfterDatePick: ((date: String) -> Unit)? = null,
) {
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, monthOfYear, dayOfMonth ->
            try {
                val date = convertDate(
                    year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth,
                    "yyyy-MM-dd",
                    dateFormat,
                    false
                )
                if (doAfterDatePick != null) {
                    doAfterDatePick(date)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DATE)
    )
    if (yearInPast != null) {
        datePickerDialog.updateDate(
            calendar.get(Calendar.YEAR) - yearInPast,
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DATE),
        )
    }
    datePickerDialog.datePicker.maxDate = calendar.timeInMillis
    datePickerDialog.show()
}

@SuppressLint("NewApi")
fun Long.formatString(format: String = "dd/MM/yyyy") : String {
    return try {
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        simpleDateFormat.format(this)
    } catch (e: Exception) {
        ""
    }
}

@SuppressLint("NewApi")
fun String.formatLong(format: String = "dd/MM/yyyy") : Long {
    return try {
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        simpleDateFormat.parse(this)?.time ?: 0
    } catch (e: Exception) {
        0
    }
}

fun TextView.showDatePickerDialog(
    dateFormatTo: String,
    defaultDate: String? = null,
    doAfterDatePick: ((date: String) -> Unit)? = null,
) {
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, monthOfYear, dayOfMonth ->
            try {
                val date = convertDate(
                    year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth,
                    "yyyy-MM-dd",
                    dateFormatTo,
                    false
                )
                if (doAfterDatePick != null) {
                    doAfterDatePick(date)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DATE)
    )
    var date: Date? = null
    val sdf = SimpleDateFormat(dateFormatTo, Locale.getDefault())
    try {
        date = defaultDate?.let { sdf.parse(it) }
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    val updateCalendar = Calendar.getInstance()
    if (date != null) {
        updateCalendar.time = date
        datePickerDialog.updateDate(
            updateCalendar.get(Calendar.YEAR),
            updateCalendar.get(Calendar.MONTH),
            updateCalendar.get(Calendar.DATE),
        )
    }
    datePickerDialog.datePicker.maxDate = calendar.timeInMillis
    datePickerDialog.show()
}

fun getCurrentDate(dateFormat: String, isUTC: Boolean): String {
    val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
    if (isUTC) {
        sdf.timeZone = TimeZone.getTimeZone("UTC")
    }
    val calendar = Calendar.getInstance()
    return sdf.format(calendar.time)
}

fun getFutureDate(dateFormat: String, amountInYear: Int, isUTC: Boolean): String {
    val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
    if (isUTC) {
        sdf.timeZone = TimeZone.getTimeZone("UTC")
    }
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.YEAR, amountInYear)
    return sdf.format(calendar.time)
}

fun String.getAge(dateFormat: String): Int {
    var date: Date? = null
    val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
    try {
        date = sdf.parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    val dob = Calendar.getInstance()
    dob.time = date!!
    val today = Calendar.getInstance()
    var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
    if (today[Calendar.DAY_OF_MONTH] < dob[Calendar.DAY_OF_MONTH]) {
        age--
    }
    return age + 1
}

fun String.convertDateFormat(from: String, to: String, isUTC: Boolean): String {
    val oldFormat = SimpleDateFormat(from, Locale.getDefault())
    if (isUTC) {
        oldFormat.timeZone = TimeZone.getTimeZone("UTC")
    }
    val newFormat = SimpleDateFormat(to, Locale.getDefault())
    newFormat.timeZone = TimeZone.getDefault()

    val date: Date
    var str = this

    try {
        date = oldFormat.parse(str) as Date
        str = newFormat.format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return str.uppercase()
}

fun TimeZone.daysBetween(from: Date, to: Date): Int {
    val offset = rawOffset + dstSavings
    return ((to.time + offset) / 86400000).toInt() - ((from.time + offset) / 86400000).toInt()
}

fun String.covertTimeToText(fromDateFormat: String, isUTC: Boolean): String? {
    var convTime: String? = null
    val prefix = ""
    var suffix1 = "Ago"

    try {
        val dateFormat = SimpleDateFormat(fromDateFormat, Locale.getDefault())
        if (isUTC) {
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        }
        val pasTime = dateFormat.parse(this)
        val nowTime = Date()
        var dateDiff = nowTime.time - (pasTime?.time ?: 0)
        if (dateDiff > 0) {
            suffix1 = "Ago"

            val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)

            if (second < 60) {
                convTime = "$second Seconds $suffix1"
            } else if (minute < 60) {
                convTime = "$minute Minutes $suffix1"
            } else if (hour < 24) {
                convTime = "$hour Hours $suffix1"
            } else if (day >= 7) {
                convTime = if (day > 360) {
                    (day / 360).toString() + " Years " + suffix1
                } else if (day > 30) {
                    (day / 30).toString() + " Months " + suffix1
                } else {
                    (day / 7).toString() + " Week " + suffix1
                }
            } else if (day < 7) {
                convTime = "$day Days $suffix1"
            }
        } else {
            dateDiff *= -1
            suffix1 = "to go"
            val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)

            if (second < 60) {
                convTime = "$second Seconds $suffix1"
            } else if (minute < 60) {
                convTime = "$minute Minutes $suffix1"
            } else if (hour < 24) {
                convTime = "$hour Hours $suffix1"
            } else if (day >= 7) {
                convTime = if (day > 360) {
                    (day / 360).toString() + " Years " + suffix1
                } else if (day > 30) {
                    (day / 30).toString() + " Months " + suffix1
                } else {
                    (day / 7).toString() + " Week " + suffix1
                }
            } else if (day < 7) {
                convTime = "$day Days $suffix1"
            }
        }
    } catch (e: ParseException) {
        e.printStackTrace()
        Log.e("ConvTimeE", e.message!!)
    }
    return convTime
}

///////////////////////////////////////////////////////////////////////////
// LUCIFER //
///////////////////////////////////////////////////////////////////////////


fun convertDate(tempDate: String, from: String, to: String, isUTC: Boolean): String {
    val oldFormat = SimpleDateFormat(from, Locale.getDefault())
    if (isUTC) {
        oldFormat.timeZone = TimeZone.getTimeZone("UTC")
    }
    val newFormat = SimpleDateFormat(to, Locale.getDefault())
    newFormat.timeZone = TimeZone.getDefault()

    val date: Date
    var str = tempDate

    try {
        date = oldFormat.parse(tempDate) as Date
        str = newFormat.format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return str.uppercase()
}

fun convertDateInstance(tempDate: String, from: String, isUTC: Boolean): Date {
    val oldFormat = SimpleDateFormat(from, Locale.ENGLISH)
    if (isUTC) {
        oldFormat.timeZone = TimeZone.getTimeZone("UTC")
    }
    var date: Date? = null
    try {
        date = oldFormat.parse(tempDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return date!!
}

fun convertCalendarInstance(tempDate: String, from: String, isUTC: Boolean): Calendar {
    val oldFormat = SimpleDateFormat(from, Locale.ENGLISH)
    if (isUTC) {
        oldFormat.timeZone = TimeZone.getTimeZone("UTC")
    }
    val calendar = Calendar.getInstance()
    try {
        val date = oldFormat.parse(tempDate)
        if (date != null) {
            calendar.time = date
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return calendar
}

fun getNextDate(dateString: String, dateFormat: String): String? {
    val format = SimpleDateFormat(dateFormat, Locale.getDefault())
    val date = format.parse(dateString)
    val calendar = Calendar.getInstance()
    calendar.time = date!!
    calendar.add(Calendar.DAY_OF_YEAR, 1)
    return format.format(calendar.time)
}

fun getPreviousDate(dateString: String, dateFormat: String): String? {
    val format = SimpleDateFormat(dateFormat, Locale.getDefault())
    val date = format.parse(dateString)
    val calendar = Calendar.getInstance()
    calendar.time = date!!
    calendar.add(Calendar.DAY_OF_YEAR, -1)
    return format.format(calendar.time)
}

fun convertDateFromCalendarInstance(calendar: Calendar, dateFormat: String): String {
    val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
    return sdf.format(calendar.time)
}

fun convertDateInstanceIntoString(date: Date, dateFormat: String, isUTC: Boolean): String {
    val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
    if (isUTC) {
        sdf.timeZone = TimeZone.getTimeZone("UTC")
    }
    val calendar = Calendar.getInstance()
    calendar.time = date
    return sdf.format(calendar.time)
}

fun showDatePickerDialogForET(context: Context, editText: EditText, dateFormat: String) {
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, monthOfYear, dayOfMonth ->
            try {
                val date = convertDate(
                    year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth,
                    "yyyy-MM-dd",
                    dateFormat,
                    false
                )
                editText.setText(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DATE)
    )
    datePickerDialog.datePicker.minDate = calendar.timeInMillis
    datePickerDialog.show()
}

fun showDatePickerDialogForTV(context: Context, editText: TextView, dateFormat: String) {
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, monthOfYear, dayOfMonth ->
            try {

                val date = convertDate(
                    year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth,
                    "yyyy-MM-dd",
                    dateFormat,
                    false
                )



                editText.text = date

            } catch (e: Exception) {
                e.printStackTrace()
            }
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DATE)
    )
    datePickerDialog.datePicker.minDate = calendar.timeInMillis
    datePickerDialog.show()
}

fun showTimePickerDialogForTV(context: Context, editText: EditText) {
    val currentTime = Calendar.getInstance()
    val hour = currentTime.get(Calendar.HOUR_OF_DAY)
    val minute = currentTime.get(Calendar.MINUTE)
    val mTimePicker = TimePickerDialog(
        context,
        { _, selectedHour, selectedMinute ->

            val date = convertDate(
                "$selectedHour:$selectedMinute",
                "HH:mm",
                "hh:mm a",
                false
            )
            editText.setText(date)
        }, hour, minute, false
    )
    mTimePicker.setTitle("Select Time")
    mTimePicker.show()
}

fun showTimePickerDialogForTV(context: Context, editText: TextView) {
    val currentTime = Calendar.getInstance()
    val hour = currentTime.get(Calendar.HOUR_OF_DAY)
    val minute = currentTime.get(Calendar.MINUTE)
    val mTimePicker = TimePickerDialog(
        context,
        { _, selectedHour, selectedMinute ->

            val date = convertDate(
                "$selectedHour:$selectedMinute",
                "HH:mm",
                "hh:mm a",
                false
            )
            editText.text = date
        }, hour, minute, false
    )
    mTimePicker.setTitle("Select Time")
    mTimePicker.show()
}

fun getTimeDifference(srcDate1: String, srcDate2: String, dateFormat: String): String {
    val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
    var date1: Date? = null
    var date2: Date? = null
    try {
        date1 = simpleDateFormat.parse(srcDate1)
        date2 = simpleDateFormat.parse(srcDate2)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    if (date1 != null && date2 != null) {
        val difference = date2.time - date1.time
        val days = (difference / (1000 * 60 * 60 * 24)).toInt()
        val hours = ((difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60)).toInt()
        val min =
            (difference - (1000 * 60 * 60 * 24 * days).toLong() - (1000 * 60 * 60 * hours).toLong()).toInt() / (1000 * 60)
        val seconds = (difference / 1000 % 60).toInt()
        val strHours = if (hours <= 9) "0$hours" else "" + hours
        val strMinutes = if (min <= 9) "0$min" else "" + min
        val strSeconds = if (seconds <= 9) "0$seconds" else "" + seconds
        Log.e("Time", "getTimeDifference: $strMinutes $strHours")
        return if (hours <= 0) {
            "$strMinutes:$strSeconds"
        } else {
            "$strHours:$strMinutes:$strSeconds"
        }

    }
    return "00:00"
}
//
//fun TextView.showAndSetBirthDatePickerDialog(
//    context: Context,
//    dateFormat: String,
//) {
//    val calendar = Calendar.getInstance()
//    val datePickerDialog = DatePickerDialog(
//        context,
//        { datePicker, year, monthOfYear, dayOfMonth ->
//            try {
//                val date = convertDate(
//                    year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth,
//                    "yyyy-MM-dd",
//                    dateFormat,
//                    false
//                )
//                if (date.getAge(dateFormat) >= 18) {
//                    text = date
//                } else {
//                    //context.showToast("Age should be grater than 18.")
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        },
//        calendar.get(Calendar.YEAR),
//        calendar.get(Calendar.MONTH),
//        calendar.get(Calendar.DATE)
//    )
//    datePickerDialog.datePicker.maxDate = calendar.timeInMillis
//    datePickerDialog.show()
//}

fun isValidTime(
    srcDate1: String,
    srcDate2: String,
    dateFormat: String,
    isUTC: Boolean,
): Boolean {

    val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
    if (isUTC) {
        simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
    }
    var date1: Date? = null
    var date2: Date? = null
    try {
        date1 = simpleDateFormat.parse(srcDate1)
        date2 = simpleDateFormat.parse(srcDate2)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    var difference = 0.toLong()
    if (date1 != null && date2 != null) {
        difference = date2.time - date1.time

    }

    return difference > 0
}


fun convertSecondsToTime(time: Int): String {
    val hr = ((time / 3600))
    val min = (((time / 60)) % 60).toInt()
    val sec = (time % 60)


    val hrStr = if (hr > 9) {
        hr.toString()
    } else {
        "0$hr"
    }

    val minStr = if (min > 9) {
        min.toString()
    } else {
        "0$min"
    }

    val secStr = if (sec > 9) {
        sec.toString()
    } else {
        "0$sec"
    }
    return "$hrStr:$minStr:$secStr"
}