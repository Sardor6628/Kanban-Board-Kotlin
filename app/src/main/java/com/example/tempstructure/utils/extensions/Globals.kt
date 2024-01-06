package com.example.tempstructure.utils.extensions

import android.content.Context
import com.example.tempstructure.R
import java.text.DecimalFormatSymbols
import java.util.Locale

val Context.selectedCardStrokeWidth: Int
    get() = resources.getDimension(R.dimen._2dp).toInt()

val Context.unSelectedCardStrokeWidth: Int
    get() = resources.getDimension(R.dimen._1dp).toInt()

val Context.focusedCardStrokeWidth: Int
    get() = resources.getDimension(R.dimen._2dp).toInt()

val Context.unFocusedCardStrokeWidth: Int
    get() = resources.getDimension(R.dimen._1dp).toInt()


//durations
var imageCrossFadeSmall = 300
var imageCrossFadeMedium = 600
var imageCrossFadeLarge = 900
var textFadeLarge = 500L
var textFadeMedium = 300L
var textFadeShort = 200L
var fragmentLoadDelay = 100L
var viewPagerScrollDelay = 3000L
var splashDelay = 1000L
var searchDelay = 400L

var isUTC = false

//number formatting
val otherSymbols = DecimalFormatSymbols(Locale.US)

var isPremiumDebug = true

var dummyImage = "https://source.unsplash.com/featured/300x201"