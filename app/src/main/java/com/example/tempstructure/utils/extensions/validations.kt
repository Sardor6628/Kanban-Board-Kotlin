package com.example.tempstructure.utils.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.icu.util.TimeZone
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.util.Base64.*
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.annotation.RequiresApi
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


@RequiresApi(Build.VERSION_CODES.P)
fun String.isValidPassword(): Boolean {
    val passwordPattern = Pattern.compile(
        "^" + "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{8,}" +               //at least 8 characters
                "$"
    )
    return passwordPattern.matcher(this).matches()
}
fun isLocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
        LocationManager.NETWORK_PROVIDER
    )
}
fun validationColor(color: String?): Int? {
    return try {
        Color.parseColor(color)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun openDialPad(phoneNumber: String, context: Context) {
    val dialUri = Uri.parse("tel:$phoneNumber")
    val dialIntent = Intent(Intent.ACTION_DIAL, dialUri)
    context.startActivity(dialIntent)
}


fun openWithRouteMap(
    startLat: Double,
    startLong: Double,
    destLat: Double,
    destLong: Double, context: Context,
) {
    try {
        // Uri.parse("http://maps.google.com/maps?saddr=$startLat,$startLong+&daddr=$destLat,$destLong")
        val uri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=$startLat,$startLong&destination=$destLat,$destLong&travelmode=driving")
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        mapIntent.setPackage("com.google.android.apps.maps")
        context.startActivity(mapIntent)
    } catch (e: Exception) {
        val gmmIntentUri = Uri.parse("google.navigation:q=$destLat,$destLong")
        val intent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        intent.setPackage("com.google.android.apps.maps")
        context.startActivity(intent)

    }
}

fun getScreenShotFromView(view: View): Bitmap? {

    var screenshot: Bitmap? = null
    try {
        screenshot = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(screenshot)
        val bgDrawable: Drawable? = view.background
        bgDrawable?.draw(canvas)
        view.draw(canvas)
    } catch (e: Exception) {
        Log.e("TAG", "Failed to capture screenshot because:" + e.message)
    }
    return screenshot
}

fun Bitmap?.bitmapToBase64(): String? {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this?.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return encodeToString(byteArray, DEFAULT)
}


fun base64ToBase30(base64Input: String): String {
    val base30Chars = "0123456789ABCDEFGHJKLMNPRTVWXYZ"
    // Decode the Base64 input into bytes
    val decodedBytes = decode(base64Input, DEFAULT)
    val base10Value = BigInteger(1, decodedBytes)
    val base30Output = StringBuilder()
    var remainingValue = base10Value
    while (remainingValue > BigInteger.ZERO) {
        val remainder = remainingValue % BigInteger.valueOf(30)
        base30Output.insert(0, base30Chars[remainder.toInt()])
        remainingValue /= BigInteger.valueOf(30)
    }
    return base30Output.toString()
}

//fun base64ToBase30(base64: String): String {
//    val base64Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
//    val base30Chars = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZ"
//
//    val result = StringBuilder()
//    var accumulator = 0
//    var bits = 0
//
//    for (char in base64) {
//        val value = base64Chars.indexOf(char)
//        if (value != -1) {
//            accumulator = (accumulator shl 6) or value
//            bits += 6
//
//            while (bits >= 5) {
//                val index = (accumulator shr (bits - 5)) and 0x1F
//                result.append(base30Chars[index])
//                accumulator = accumulator and ((1 shl (bits - 5)) - 1)
//                bits -= 5
//            }
//        }
//    }
//
//    if (bits > 0) {
//        val index = accumulator shl (5 - bits)
//        result.append(base30Chars[index and 0x1F])
//    }
//
//    return result.toString()
//}


/*@RequiresApi(Build.VERSION_CODES.N)
fun convertDateFormat(
    dateToFormat: String?,
    haveFormat: String,
    wantFormat: String,
): String? {
    try {
        val haveFormatter = SimpleDateFormat(haveFormat, Locale.getDefault())
        val wantFormatter = SimpleDateFormat(wantFormat, Locale.getDefault())
        wantFormatter.timeZone = TimeZone.getDefault()
        val date = haveFormatter.parse(dateToFormat)
        return wantFormatter.format(date)
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return dateToFormat
}*/

@SuppressLint("ResourceAsColor")


fun String.isValidMobile(): Boolean {
    return if (!Pattern.matches("[a-zA-Z]+", this)) {
        this.length == 10
    } else false
}

fun String.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun doesPasswordMatch(oldPass: String, newPass: String): Boolean {
    return oldPass.isTextValid() && newPass.isTextValid() && oldPass.contentEquals(newPass)
}

fun String.isTextValid(): Boolean {
    return !isNullOrBlank()
}

fun String.isTextNotValid(): Boolean {
    return isNullOrBlank()
}

fun isDateValidForDating(dobString: String): Boolean {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
    val d = dateFormat.parse(dobString)
    val cal18 = Calendar.getInstance()
    cal18.add(Calendar.YEAR, -18)
    val age18 = cal18.time
    val cal80 = Calendar.getInstance()
    cal80.add(Calendar.YEAR, -80)
    val age80 = cal80.time
    return when {
        age80.after(d) || age18.before(d) -> {
            false
        }

        else -> {
            true
        }
    }
}