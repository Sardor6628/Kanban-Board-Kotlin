package com.example.tempstructure.utils.extensions

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.text.toSpannable

/** color string between given char **/
fun String.makeSpanColorBetween(startChar: Char, endChar: Char, color: Int): Spannable {
    val startIndexChar: Int = indexOf(startChar)
    val endIndexChar: Int = indexOf(endChar)
    val subString = substring((startIndexChar.plus(1)), (endIndexChar))
    val newString = replace(
        startChar.toString(), ""
    ).replace(
        endChar.toString(), ""
    )
    val startIndex: Int = newString.indexOf(subString)
    val endIndex: Int = newString.indexOf(subString).plus(subString.length)

    val spannableString: Spannable = SpannableString(newString)
    spannableString.setSpan(
        ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannableString
}

/** color string between given char **/
fun String.makeSpanUnderlineBetween(startChar: Char? = null, endChar: Char? = null): Spannable {
    val startIndexChar: Int
    val endIndexChar: Int

    if (startChar == null || endChar == null) {
        startIndexChar = -1
        endIndexChar = length
    } else {
        startIndexChar = indexOf(startChar)
        endIndexChar = indexOf(endChar)
    }

    if (startIndexChar == -1 || endIndexChar == -1) {
//        luciferLog("character not found")
        return toSpannable()
    }

    val subString = substring((startIndexChar.plus(1)), (endIndexChar))
    val newString = replace(
        startChar.toString(), ""
    ).replace(
        endChar.toString(), ""
    )
    val startIndex: Int = newString.indexOf(subString)
    val endIndex: Int = newString.indexOf(subString).plus(subString.length)
    val spannableString: Spannable = SpannableString(newString)
    spannableString.setSpan(
        UnderlineSpan(), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    return spannableString
}

/** color string between given char **/
fun String.makeTwoSpanColorBetween(startChar1: Char, endChar1: Char, startChar2: Char, endChar2: Char, color: Int): Spannable {
    val startIndexChar: Int = indexOf(startChar1)
    val endIndexChar: Int = indexOf(endChar1)
    val startIndex2Char: Int = indexOf(startChar2)
    val endIndex2Char: Int = indexOf(endChar2)
    val subString = substring((startIndexChar.plus(1)), (endIndexChar))
    val subString2 = substring((startIndex2Char.plus(1)), (endIndex2Char))
    val newString = replace(
        startChar1.toString(), ""
    ).replace(
        endChar1.toString(), ""
    ).replace(
        startChar2.toString(), ""
    ).replace(
        endChar2.toString(), ""
    )

    val startIndex: Int = newString.indexOf(subString)
    val endIndex: Int = newString.indexOf(subString).plus(subString.length)

    val startIndex2: Int = newString.indexOf(subString2)
    val endIndex2: Int = newString.indexOf(subString2).plus(subString2.length)

    val spannableString: Spannable = SpannableString(newString)
    spannableString.setSpan(
        ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannableString.setSpan(
        ForegroundColorSpan(color), startIndex2, endIndex2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    return spannableString
}

/** color string between given char and make it clickable **/
fun TextView.makeSpanClickableBetween(string: String, startChar: Char, endChar: Char, color: Int, doOnClick: (() -> Unit)? = null) {
    val startIndexChar: Int = string.indexOf(startChar)
    val endIndexChar: Int = string.indexOf(endChar)
    val subString = string.substring((startIndexChar.plus(1)), (endIndexChar))
    val newString = string.replace(
        startChar.toString(), ""
    ).replace(
        endChar.toString(), ""
    )
    val startIndex: Int = newString.indexOf(subString)
    val endIndex: Int = newString.indexOf(subString).plus(subString.length)

    val spannableString: Spannable = SpannableString(newString)
    spannableString.setSpan(
        ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    val clickableSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(textView: View) {
            if (doOnClick != null) {
                doOnClick()
            }
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
            if (isPressed) {
                ds.color = Color.BLUE
            } else {
                ds.color = color
            }
            invalidate()
        }
    }

    spannableString.setSpan(
        clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    text = spannableString
    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = Color.TRANSPARENT
}

fun TextView.setGradiant(@ColorRes startColor: Int, @ColorRes endColor: Int) {
//    setTextColor(getColor(context, startColor))
    val textShader: Shader = LinearGradient(
        0f, 0f,
        paint.measureText(text.toString()),
        textSize,
        intArrayOf(
            getColor(context, endColor),
            getColor(context, startColor),
        ),
        null,
        Shader.TileMode.REPEAT,
    )
    this.paint.shader = textShader
}