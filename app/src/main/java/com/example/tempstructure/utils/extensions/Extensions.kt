package com.example.tempstructure.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavOptions
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.example.tempstructure.app.TempStructureApp
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.Serializable
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import kotlin.math.roundToInt


///////////////////////////////////////////////////////////////////////////
// LUCIFER //
///////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////
// region ViewBinding


inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T,
) = lazy(LazyThreadSafetyMode.NONE) {
    bindingInflater.invoke(layoutInflater)
}

fun <R> CoroutineScope.executeAsyncTask(
    onPreExecute: () -> Unit,
    doInBackground: () -> R,
    onPostExecute: (R) -> Unit,
) = launch {
    onPreExecute() // runs in Main Thread
    val result = withContext(Dispatchers.IO) {
        doInBackground() // runs in background thread without blocking the Main Thread
    }
    onPostExecute(result) // runs in Main Thread
}


inline fun <T : ViewBinding> Fragment.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T,
) = lazy(LazyThreadSafetyMode.NONE) {
    bindingInflater.invoke(layoutInflater)
}

fun EditText.showKeyBoard(activity: Activity) {
    // val inputMethodManager = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    if (!this.keyboardIsVisible) {
        //inputMethodManager?.toggleSoftInputFromWindow(this.applicationWindowToken, InputMethodManager.SHOW_FORCED, 0)
        val view = activity.currentFocus
        val methodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        assert(view != null)
        methodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }


}

fun scaleBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
    val ratio = bitmap.width.toFloat() / bitmap.height.toFloat()
    val finalWidth = minOf(bitmap.width, maxWidth)
    val finalHeight = (finalWidth / ratio).toInt().coerceAtMost(maxHeight)
    return Bitmap.createScaledBitmap(bitmap, finalWidth, finalHeight, false)
}

fun compressBitmap(bitmap: Bitmap, quality: Int): Bitmap {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    val byteArray = outputStream.toByteArray()
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}

val View.keyboardIsVisible: Boolean
    get() = WindowInsetsCompat
        .toWindowInsetsCompat(rootWindowInsets)
        .isVisible(WindowInsetsCompat.Type.ime())

// endregion ViewBinding
///////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////
// region Colors & Dimensions

fun getColor(context: Context, @ColorRes id: Int): Int {
    return ContextCompat.getColor(context, id)
}

fun getColorStateList(context: Context, @ColorRes id: Int): ColorStateList {
    return ColorStateList.valueOf(ContextCompat.getColor(context, id))
}

fun getColorFromAttr(context: Context, @ColorInt id: Int): Int {
    val typedValue = TypedValue();
    context.theme.resolveAttribute(id, typedValue, true)
    return ContextCompat.getColor(context, typedValue.resourceId)
}


fun MaterialTextView.setTextViewColor(color: String?) {
    if (!color.isNullOrEmpty()) {
        this.setTextColor(Color.parseColor(color))
    } else {
        this.setTextColor(Color.parseColor("#FFFFFF"))
    }
}

//fun capitalizeFirstChar(input: String): String {
//    if (input.isEmpty()) {
//        return input
//    }
//    val firstChar = input[0].uppercaseChar()
//    val restOfString = input.substring(1)
//    return "$firstChar$restOfString"
//}

fun Int.asDrawable() = ContextCompat.getDrawable(TempStructureApp.instance, this)
fun Int.asColor() = ContextCompat.getColor(TempStructureApp.instance, this)
fun View.setBackgroundViewColor(color: String?) {
    if (!color.isNullOrEmpty()) {
        this.setBackgroundColor(Color.parseColor(color))
    } else {
        this.setBackgroundColor(Color.parseColor("#FF38323A"))
    }
}

fun <T> createDiffCallback(
    oldList: List<T>,
    newList: List<T>,
    itemIdSelector: (item: T) -> Any,
    itemContentComparator: (oldItem: T, newItem: T) -> Boolean
) = object : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldPosition: Int, newPosition: Int) =
        itemIdSelector(oldList[oldPosition]) == itemIdSelector(newList[newPosition])

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int) =
        itemContentComparator(oldList[oldPosition], newList[newPosition])
}

fun AppCompatButton.setButtonBackgroundViewColor(color: String?) {

    if (!color.isNullOrEmpty()) {
        this.setBackgroundColor(Color.parseColor(color))
    } else {
        this.setBackgroundColor(Color.parseColor("#FBD106"))
    }
}

fun TextInputEditText.setEdtTextColor(color: String?) {
    if (!color.isNullOrEmpty()) {
        this.setTextColor(Color.parseColor(color))
    } else {
        this.setTextColor(Color.parseColor("#FFFFFF"))
    }
}

//fun TextInputEditText.setEdtHintTextColor(color: String?) {
//    if (!color.isNullOrEmpty()) {
//        this.setHintTextColor(Color.parseColor(color))
//    } else {
//        this.setHintTextColor(Color.parseColor("#CECECE"))
//    }
//}
fun TextInputLayout.setEdtColor(hintColor: String?, textColor: String?) {
    if (!textColor.isNullOrEmpty() && !hintColor.isNullOrEmpty()) {
        this.hintTextColor = ColorStateList.valueOf(Color.parseColor(hintColor))
        this.defaultHintTextColor = ColorStateList.valueOf(Color.parseColor(hintColor))
        this.editText?.setHintTextColor(ColorStateList.valueOf(Color.parseColor(hintColor)))
        this.editText?.setTextColor(ColorStateList.valueOf(Color.parseColor(textColor)))
        this.editText?.backgroundTintList = ColorStateList.valueOf(Color.parseColor(hintColor))
    } else {
        this.hintTextColor = ColorStateList.valueOf(Color.parseColor("#CECECE"))
        this.defaultHintTextColor = ColorStateList.valueOf(Color.parseColor("#CECECE"))
        this.editText?.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#CECECE")))
        this.editText?.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")))
    }
}

fun AppCompatButton.setButtonTextColor(color: String?) {

    if (!color.isNullOrEmpty()) {
        this.setTextColor(Color.parseColor(color))
    } else {
        this.setTextColor(Color.parseColor("#FBD106"))
    }
}

fun getDrawable(context: Context, @DrawableRes id: Int): Drawable? {
    return ContextCompat.getDrawable(context, id)
}

internal val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).roundToInt()
internal val Int.sp: Int
    get() = (this / Resources.getSystem().displayMetrics.scaledDensity).roundToInt()
internal val Int.dpPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()
internal val Int.spPx: Int
    get() = (this * Resources.getSystem().displayMetrics.scaledDensity).roundToInt()

// endregion Colors & Dimensions
///////////////////////////////////////////////////////////////////////////


///////////////////////////////////////////////////////////////////////////
// region Intents & navigation

inline fun <reified T : Activity> Context.launch(block: Intent.() -> Unit = {}) {
    startActivity(Intent(this, T::class.java).apply(block))
}

inline fun <reified T : Activity> Fragment.launch(block: Intent.() -> Unit = {}) {
    startActivity(Intent(requireActivity(), T::class.java).apply(block))
}

inline fun <reified T : Activity> ActivityResultLauncher<Intent>.launch(
    context: Context,
    block: Intent.() -> Unit = {}
) {
    this.launch(Intent(context, T::class.java).apply(block))
}

/*fun Fragment.launch(id: Int, @IdRes finishToFragmentId: Int? = null, block: Bundle.() -> Unit = {}) {
    val navBuilder = NavOptions.Builder()
    val bundle = Bundle()
    navBuilder.apply {
        setEnterAnim(R.anim.slide_in)
        setExitAnim(R.anim.fade_out)
        setPopEnterAnim(R.anim.fade_in)
        setPopExitAnim(R.anim.slide_out)
        if (finishToFragmentId != null) {
            setEnterAnim(R.anim.slide_in)
            setExitAnim(R.anim.fade_out)
            setPopEnterAnim(R.anim.fade_in)
            setPopExitAnim(R.anim.slide_out)
            setPopUpTo(finishToFragmentId, true)
        }
    }
    findNavController().navigate(id, bundle.apply(block), navBuilder.build())
}*/

/** use this with findNavController, pass it in navOptions **/
fun finishFragment(@IdRes destinationId: Int): NavOptions {
    return NavOptions.Builder().setPopUpTo(destinationId, true).build()
}

/*fun AppCompatActivity.launch(activity: Activity, view: Int, id: Int, @IdRes finishToFragmentId: Int? = null, block: Bundle.() -> Unit = {}) {
    val navBuilder = NavOptions.Builder()
    val bundle = Bundle()
    navBuilder.apply {
        setEnterAnim(R.anim.slide_in)
        setExitAnim(R.anim.fade_out)
        setPopEnterAnim(R.anim.fade_in)
        setPopExitAnim(R.anim.slide_out)
        if (finishToFragmentId != null) {
            setEnterAnim(R.anim.fade_in)
            setExitAnim(R.anim.fade_out)
            setPopEnterAnim(R.anim.fade_in)
            setPopExitAnim(R.anim.fade_out)
            setPopUpTo(finishToFragmentId, true)
        }
    }
    activity.findNavController(view).navigate(id, bundle.apply(block), navBuilder.build())
}*/

inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializable(key) as? T
}

inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(
        key,
        T::class.java
    )

    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

inline fun <reified T : Parcelable?> Bundle.parcelableArrayList(key: String): ArrayList<T>? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableArrayList(
        key,
        T::class.java
    )

    else -> @Suppress("DEPRECATION") getParcelableArrayList(key)
}

inline fun <reified T : Parcelable> Intent.parcelableArrayList(key: String): ArrayList<T>? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableArrayListExtra(
        key,
        T::class.java
    )

    else -> @Suppress("DEPRECATION") getParcelableArrayListExtra(key)
}

fun AppCompatActivity.launchShareSimpleTextChooser(
    contentToShare: String,
    subject: String? = null,
    chooserText: String? = null,
    sharePackageName: String? = null,
) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_SUBJECT, subject ?: "")
    intent.putExtra(Intent.EXTRA_TEXT, contentToShare)
    if (sharePackageName.isNullOrBlank()) {
        intent.setPackage(sharePackageName)
    }
    startActivity(Intent.createChooser(intent, chooserText ?: "share using..."))
}

fun Fragment.launchShareSimpleTextChooser(
    contentToShare: String,
    subject: String? = null,
    chooserText: String? = null,
    sharePackageName: String? = null,
) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_SUBJECT, subject ?: "")
    intent.putExtra(Intent.EXTRA_TEXT, contentToShare)
    if (sharePackageName.isNullOrBlank()) {
        intent.setPackage(sharePackageName)
    }
    startActivity(Intent.createChooser(intent, chooserText ?: "share using..."))
}


// endregion Intents & navigation
///////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////
// ImageLoading
///////////////////////////////////////////////////////////////////////////


///////////////////////////////////////////////////////////////////////////
// Toaster
///////////////////////////////////////////////////////////////////////////

fun Context.getFileFromCache(): ArrayList<File> {
    val dir = File(cacheDir.absolutePath)
    val fileNameList = ArrayList<File>()
    if (dir.exists()) {
        val fileList = dir.listFiles()
        if (!fileList.isNullOrEmpty()) {
            for (file in fileList) {
                fileNameList.add(file)
            }
        }
    }
    return fileNameList
}

///////////////////////////////////////////////////////////////////////////
//region Math

fun Double.roundToNearest(nearest: Int): Int {
    return (this / nearest).roundToInt() * nearest
}

fun Float.roundToNearest(nearest: Int): Int {
    return (this / nearest).roundToInt() * nearest
}

fun Double.toBigDecimal(scale: Int = 0): Double {
    return BigDecimal.valueOf(this).setScale(scale, RoundingMode.HALF_EVEN).toPlainString()
        .toDouble()
}

fun Double.toDecimalFormatDouble(): Double {
    return DecimalFormat("#.##", otherSymbols).format(this).toDouble()
}

fun Double.toDecimalFormatString(): String {
    return DecimalFormat("#.##", otherSymbols).format(this)
}

fun Float.toDecimalFormatDouble(): Double {
    return DecimalFormat("#.##", otherSymbols).format(this).toDouble()
}

fun Int.toDecimalFormatDouble(): Double {
    return DecimalFormat("#.##", otherSymbols).format(this).toDouble()
}

//endregion Math
///////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////
//region Window

/** Use this function to fit background in status bar and navigation bar
 * only works if status bar and navigation bar is set transparent
 * **/
fun AppCompatActivity.setWindowInsets(view: View) {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
        val inset = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            leftMargin = inset.left
            bottomMargin = inset.bottom
            rightMargin = inset.right
            topMargin = inset.top
        }
        WindowInsetsCompat.CONSUMED
    }
}

fun FragmentActivity.setWindowInsets(view: View) {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
        val inset = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            leftMargin = inset.left
            bottomMargin = inset.bottom
            rightMargin = inset.right
            topMargin = inset.top
        }
        WindowInsetsCompat.CONSUMED
    }
}

//endregion Window
///////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////
//region KeyBoard

fun EditText.showKeyboard(context: Context) {
    val inputMethodManager: InputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(this, 0)
}

fun EditText.closeKeyboard(context: Context) {
    val inputMethodManager: InputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}

//endregion KeyBoard
///////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////
//region Array

//endregion Array
///////////////////////////////////////////////////////////////////////////

fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith {
    when (val value = this[it]) {
        is JSONArray -> {
            val map = (0 until value.length()).associate { Pair(it.toString(), value[it]) }
            JSONObject(map).toMap().values.toList()
        }

        is JSONObject -> value.toMap()
        JSONObject.NULL -> null
        else -> value
    }
}

//Device Model
///////////////////////////////////////////////////////////////////////////

fun getDeviceName(): String {
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    return if (model.lowercase(Locale.getDefault())
            .startsWith(manufacturer.lowercase(Locale.getDefault()))
    ) {
        capitalize(model)
    } else {
        capitalize(manufacturer) + " " + model
    }
}

fun capitalize(s: String?): String {
    if (s.isNullOrEmpty()) {
        return ""
    }
    val first = s[0]
    return if (Character.isUpperCase(first)) {
        s
    } else {
        first.uppercaseChar().toString() + s.substring(1)
    }
}
