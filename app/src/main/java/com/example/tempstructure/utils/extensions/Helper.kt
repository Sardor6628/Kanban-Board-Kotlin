package com.example.tempstructure.utils.extensions


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import android.widget.PopupWindow
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

import com.example.tempstructure.R

import java.util.*

///////////////////////////////////////////////////////////////////////////
//region Bottom sheets and dialogs

@SuppressLint("RestrictedApi")
fun View.showMenu(@MenuRes menuRes: Int, titleMap: HashMap<Int, String>? = null, iconMap: HashMap<Int, Int>? = null): PopupMenu {
    val popup = PopupMenu(context, this)
    popup.menuInflater.inflate(menuRes, popup.menu)
    if (popup.menu is MenuBuilder) {
        val menuBuilder = popup.menu as MenuBuilder
        menuBuilder.setOptionalIconsVisible(true)
        for (item in menuBuilder.visibleItems) {
            val iconMarginPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1f, context.resources.displayMetrics
            ).toInt()
            if (item.icon != null) {
                item.icon = InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0)
            }

            if (!iconMap.isNullOrEmpty() && iconMap.containsKey(item.itemId) && iconMap[item.itemId] != null) {
                item.icon = InsetDrawable(getDrawable(context, iconMap[item.itemId]!!), iconMarginPx, 0, iconMarginPx, 0)
            }

            if (!titleMap.isNullOrEmpty() && titleMap.containsKey(item.itemId)) {
                item.title = titleMap[item.itemId]
            }

            // This adds the divider between groups 0 and 1, but only supported on Android 9.0 and up.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                menuBuilder.isGroupDividerEnabled = true
            }

        }
    }
    popup.show()
    return popup
}

// fun getSchemeCalendar(year: Int, month: Int, day: Int, color: Int, text: String): Calendar? {
//    val calendar = Calendar()
//    calendar.year = year
//    calendar.month = month
//    calendar.day = day
//    calendar.schemeColor = color
//    calendar.scheme = text
//    calendar.addScheme(color, "假")
//    calendar.addScheme(if (day % 2 == 0) -0xff3300 else -0x2ea012, "节")
//    calendar.addScheme(if (day % 2 == 0) -0x9a0000 else -0xbe961f, "记")
//    return calendar
//}

// fun getSchemeCalendar(year: Int, month: Int, day: Int, color: Int, text: String): Calendar? {
//    val calendar = Calendar()
//    calendar.year = year
//    calendar.month = month
//    calendar.day = day
//    calendar.schemeColor = color
//    calendar.scheme = text
//    calendar.addScheme(color, "假")
//    calendar.addScheme(if (day % 2 == 0) -0xff3300 else -0x2ea012, "节")
//    calendar.addScheme(if (day % 2 == 0) -0x9a0000 else -0xbe961f, "记")
//    return calendar
//}

//endregion Bottom sheets and dialogs
///////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////
//region Popups and menus
fun showPopUpMenu(
    @DrawableRes backGroundDrawableRes: Int? = null,
    @LayoutRes resource: Int? = null,
    anchorView: View,
    context: Context,
    popupWindowView: (view: PopupWindow) -> Unit,
) {
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view = inflater.inflate(resource!!, null, false)
    val pw = PopupWindow(
        view,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        true
    )
    popupWindowView(pw)
    pw.isOutsideTouchable = true
    pw.isFocusable = true
    val a = IntArray(2)
    anchorView.getLocationInWindow(a)
    pw.showAtLocation(
        anchorView, Gravity.NO_GRAVITY,
        a[0] - 200,
        a[1] + anchorView.height
    )
    if (backGroundDrawableRes != null)
        pw.setBackgroundDrawable(ContextCompat.getDrawable(context, backGroundDrawableRes))
}
fun View.showListPopup(
    items: ArrayList<String> = arrayListOf(),
    @LayoutRes resource: Int? = null,
    arrayAdapter: ArrayAdapter<String>? = null,
    onItemSelect: (position: Int) -> Unit,
) {
    val listPopupWindow = ListPopupWindow(context, null, androidx.appcompat.R.attr.listPopupWindowStyle)
    listPopupWindow.anchorView = this
    listPopupWindow.isModal = true
  //  listPopupWindow.setBackgroundDrawable(getDrawable(context, R.drawable.custom_popupmenu_background))

   // val adapter = arrayAdapter ?: ArrayAdapter(context, resource ?: R.layout.dropdown_popup_item, items)
    //listPopupWindow.setAdapter(adapter)

    listPopupWindow.setOnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
        if (items.isNotEmpty()) {
            onItemSelect(position)
        }
        listPopupWindow.dismiss()
    }

    if (listPopupWindow.isShowing) {
        listPopupWindow.dismiss()
    } else {
        listPopupWindow.show()
    }
}

//endregion Other Utils
///////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////
//region Other Utils

//fun smoothRecreate(activity: AppCompatActivity) {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//        activity.window.setWindowAnimations(R.style.WindowAnimationFadeInOut)
//        ActivityCompat.recreate(activity)
//    } else {
//        val intent = activity.intent
//        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
//        activity.finish()
//        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
//        activity.startActivity(intent)
//    }
//}
//
//fun smoothRecreate(activity: FragmentActivity) {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//        activity.window.setWindowAnimations(R.style.WindowAnimationFadeInOut)
//        ActivityCompat.recreate(activity)
//    } else {
//        val intent = activity.intent
//        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
//        activity.finish()
//        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
//        activity.startActivity(intent)
//    }
//}

//fun getDeviceInfo(): String {
//    val deviceInfo: StringBuilder = StringBuilder()
//    deviceInfo.append("OS=")
//    deviceInfo.append(Build.VERSION.RELEASE)
//    deviceInfo.append(" | ")
//    deviceInfo.append("Brand=")
//    deviceInfo.append(Build.BRAND)
//    deviceInfo.append(" | ")
//    deviceInfo.append("Model=")
//    deviceInfo.append(Build.MODEL)
//    deviceInfo.append(" | ")
//    deviceInfo.append("AppVersion=")
//    deviceInfo.append("v")
//    deviceInfo.append(BuildConfig.VERSION_NAME)
//    deviceInfo.append("(")
//    deviceInfo.append(BuildConfig.VERSION_CODE)
//    deviceInfo.append(")")
//    deviceInfo.append(" | ")
//    deviceInfo.append("APIVersion=")
//    deviceInfo.append(AppConstant.API_VERSION)
//    return deviceInfo.toString()
//}

fun firebaseSubscribe(vararg topics: String) {
//    topics.forEach { topic ->
//        Firebase.messaging.subscribeToTopic(topic).addOnCompleteListener {
//            if (it.isSuccessful) {
//                Log.e("firebase_topic", "subscribed to notification topic: ${topic}")
//            } else {
//                Log.e("firebase_topic", "subscription to notification failed : ${topic}")
//            }
//        }
//    }
}
//
//fun firebaseUnsubscribe(vararg topics: String) {
//    topics.forEach { topic ->
//        Firebase.messaging.unsubscribeFromTopic(topic).addOnCompleteListener {
//            if (it.isSuccessful) {
//                Log.e("firebase_topic", "un_subscribed to notification topic: ${topic}")
//            } else {
//                Log.e("firebase_topic", "un_subscription to notification failed : ${topic}")
//            }
//        }
//    }
//}
//
//fun firebaseDeleteToken() {
//    FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener {
//        if (it.isSuccessful) {
//            luciferLog("firebase token deleted")
//        } else {
//            luciferLog("firebase token deleted failed")
//        }
//    }
//}

fun <T : View?> findViewsWithType(root: View, type: Class<T>): List<T>? {
    val views: MutableList<T> = ArrayList()
    findViewsWithType(root, type, views)
    return views
}

private fun <T : View?> findViewsWithType(view: View, type: Class<T>, views: MutableList<T>) {
    if (type.isInstance(view)) {
        views.add(type.cast(view))
    }
    if (view is ViewGroup) {
        val viewGroup = view
        for (i in 0 until viewGroup.childCount) {
            findViewsWithType(viewGroup.getChildAt(i), type, views)
        }
    }
}

//endregion Other Utils
///////////////////////////////////////////////////////////////////////////

