package com.example.tempstructure.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.math.roundToInt
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


object ViewExtension {

    inline fun <T : ViewBinding> Fragment.viewBinding(
        crossinline viewBindingFactory: (View) -> T,
        noinline cleanUp: ((T?) -> Unit)? = null,
    ): FragmentViewBindingDelegate<T> =
        FragmentViewBindingDelegate(this, { v -> viewBindingFactory(v) }, cleanUp)


    class FragmentViewBindingDelegate<T : ViewBinding>(
        val fragment: Fragment,
        val viewBindingFactory: (View) -> T,
        val cleanUp: ((T?) -> Unit)?,
    ) : ReadOnlyProperty<Fragment, T> {

        // A backing property to hold our value
        private var binding: T? = null

        init {
            fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
                val viewLifecycleOwnerObserver =
                    androidx.lifecycle.Observer<LifecycleOwner?> { owner ->
                        if (owner == null) {
                            cleanUp?.invoke(binding)
                            binding = null
                        }
                    }

                override fun onCreate(owner: LifecycleOwner) {
                    fragment.viewLifecycleOwnerLiveData.observeForever(
                        viewLifecycleOwnerObserver
                    )
                }

                override fun onDestroy(owner: LifecycleOwner) {
                    fragment.viewLifecycleOwnerLiveData.removeObserver(
                        viewLifecycleOwnerObserver
                    )
                }
            })
        }

        override fun getValue(
            thisRef: Fragment,
            property: KProperty<*>,
        ): T {
            val binding = binding
            if (binding != null && binding.root === thisRef.view) {
                return binding
            }

            val view = thisRef.view
                ?: throw IllegalStateException("Should not attempt to get bindings when Fragment's view is null.")

            return viewBindingFactory(view).also { this.binding = it }
        }


    }

    fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this.requireContext(), message, duration).show()
    }

    fun Fragment.showToast(@StringRes message: Int, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this.requireContext(), message, duration).show()
    }

    fun Activity.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }

    fun Activity.showToast(@StringRes message: Int, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }

    fun Context.asColor(color: Int) = ContextCompat.getColor(this, color)

    fun TextView.setBionicText(isBionic: Boolean, text: String) {
        if (isBionic) {
            var newDes = ""
            val split = text.split(" ")
            for (item in split) {
                var length = (item.length * 0.5).roundToInt()
                if (length <= 0) {
                    length = 1
                }
                val backWord = item.slice(IntRange(length, item.length - 1))
                var boldText = ""
                var bold = ""
                for (j in 0 until length) {
                    val word = item.toCharArray()[j]
                    bold += word
                }
                boldText += "<b>$bold</b>"
                val back = "<span style=\"opacity: 0.1;\">$backWord</span>"
                newDes += "$boldText$back "
            }
            this.text = HtmlCompat.fromHtml(newDes, HtmlCompat.FROM_HTML_MODE_LEGACY)
        } else {
            this.text = text
        }
    }

    fun AppCompatEditText.setShuffleText(text: String) {
        val wordArray = ArrayList(text.split(" "))
        var newString = ""
        wordArray.forEach {
            if (it.length > 3) {
                var list = it.toMutableList()
                val first = list.first()
                val last = list.last()
                val newList = list.subList(1, list.size - 1)
                newList.shuffle()
                list = arrayListOf()
                list.add(first)
                list.addAll(newList)
                list.add(last)
                newString += String(list.toCharArray()) + " "
            } else {
                newString += "$it "
            }
        }
        this.setText(newString)
    }


    fun String.setShuffleText():String {
        var newString = ""
            if (this.length > 3) {
                var list = this.toMutableList()
                val first = list.first()
                val last = list.last()
                val newList = list.subList(1, list.size - 1)
                newList.shuffle()
                list = arrayListOf()
                list.add(first)
                list.addAll(newList)
                list.add(last)
                newString += String(list.toCharArray()) + " "
            } else {
                newString += "$this "
            }

        return newString
    }

    fun TextView.setTextAnimation(text: String, duration: Long = 0, completion: (() -> Unit)? = null) {
        fadOutAnimation(duration) {
            this.text = text
            fadInAnimation(duration) {
                completion?.let {
                    it()
                }
            }
        }
    }
    fun View.fadOutAnimation(duration: Long = 100 , completion: (() -> Unit)? = null) {
        animate()
            .alpha(0.7f)
            .withEndAction {
               // this.visibility = visibili
                completion?.let {
                    it()
                }
            }
    }

    fun View.fadInAnimation(duration: Long = 100, completion: (() -> Unit)? = null) {
        alpha = 0f
        //visibility = View.VISIBLE
        animate()
            .alpha(1f)
            .withEndAction {
                completion?.let {
                    it()
                }
            }
    }

    fun View.visible() {
        this.isVisible = true
    }

    fun View.gone() {
        this.isVisible = false
    }

    interface PermissionCallback {
        fun onAllGranted() {}

        fun onDeny(deniedList: List<String>) {}
    }


    fun <T> Fragment.getListener(fragment: Fragment, listenerClass: Class<T>): T? {
        var listener: T? = null
        if (listenerClass.isInstance(fragment.parentFragment)) {
            listener = listenerClass.cast(fragment.parentFragment)
        } else if (listenerClass.isInstance(fragment.activity)) {
            listener = listenerClass.cast(fragment.activity)
        }
        return listener
    }

    fun <R> CoroutineScope.executeAsyncTask(
        onPreExecute: () -> Unit,
        doInBackground: () -> R,
        onPostExecute: (R) -> Unit
    ) = launch {
        onPreExecute() // runs in Main Thread
        val result = withContext(Dispatchers.IO) {
            doInBackground() // runs in background thread without blocking the Main Thread
        }
        onPostExecute(result) // runs in Main Thread
    }

    inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelable(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelable(key) as? T
    }

    inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableExtra(
            key,
            T::class.java
        )

        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    }

    fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) {
        try {
            var fout = FileOutputStream(this.path)
            bitmap.compress(format, quality, fout)
            fout.flush()
            fout.close()
        } catch (e: Exception) {
            e.printStackTrace();

        }
    }

    fun WebView.toBitmap(): Bitmap {
        this.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        print("\n measuredWidth: ${this.measuredWidth}, measuredHeight: ${this.measuredHeight}")
        val bitmap =
            Bitmap.createBitmap(this.measuredWidth, this.measuredHeight, Bitmap.Config.ARGB_8888)


        // For webView


        val canvas = Canvas(bitmap)
        this.draw(canvas)
        return bitmap
    }

}