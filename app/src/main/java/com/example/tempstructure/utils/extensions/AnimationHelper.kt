package com.example.tempstructure.utils.extensions

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.TextView
import com.example.tempstructure.R

fun TextView.setTextWithAnimation(text: String, duration: Long = 300, completion: (() -> Unit)? = null) {
    fadOutAnimation(duration) {
        this.text = text
        fadInAnimation(duration) {
            completion?.let {
                it()
            }
        }
    }
}

fun View.fadOutAnimation(duration: Long = 100, visibility: Int = View.INVISIBLE, completion: (() -> Unit)? = null) {
    animate().alpha(0.5f).setDuration(duration).withEndAction {
        this.visibility = visibility
        completion?.let {
            it()
        }
    }
}

fun View.fadInAnimation(duration: Long = 500, completion: (() -> Unit)? = null) {
    alpha = 0f
    visibility = View.VISIBLE
    animate().alpha(1f).setDuration(duration).withEndAction {
        completion?.let {
            it()
        }
    }
}


fun View.setRotateAnimation(
    duration: Long = 5000,
    repeatCount: Int = Animation.INFINITE,
) {
    val rotate = RotateAnimation(
        0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
    )
    rotate.repeatCount = repeatCount
    rotate.duration = duration
    rotate.interpolator = LinearInterpolator()
    startAnimation(rotate)
}

fun View.setScaleUp(
    scaleUpDuration: Long = 3000,
    scaleDownDuration: Long = 2000,
) {
    animate().scaleX(1.1f).scaleY(1.1f).setDuration(scaleUpDuration).withEndAction {
        animate().scaleX(1f).scaleY(1f).setDuration(scaleDownDuration).withEndAction {
            setScaleUp(scaleUpDuration, scaleDownDuration)
        }
    }
}

fun View.rotate(angle: Float = 180f, duration: Long = 100) {
    animate().apply {
        rotation(angle)
        this.duration = duration
    }.start()
}