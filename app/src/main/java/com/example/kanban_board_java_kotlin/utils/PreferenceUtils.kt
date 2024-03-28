package com.example.kanban_board_java_kotlin.utils

import android.content.SharedPreferences
import com.example.kanban_board_java_kotlin.app.AppConstant

class PreferenceUtils(private val sharedPreferences: SharedPreferences) {

    fun isLogin(): Boolean{
        return sharedPreferences.getBoolean(AppConstant.LOGIN_KEY, false)
    }

    fun setLogin(login: Boolean) {
        return sharedPreferences.edit().putBoolean(AppConstant.LOGIN_KEY, login).apply()
    }

    fun getUserId(): String{
        return sharedPreferences.getString(AppConstant.USER_KEY, "").toString()
    }

    fun setUserId(login: String) {
        return sharedPreferences.edit().putString(AppConstant.USER_KEY, login).apply()
    }

}
