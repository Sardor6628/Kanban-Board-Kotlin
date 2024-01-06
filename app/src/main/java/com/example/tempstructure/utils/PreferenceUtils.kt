package com.example.tempstructure.utils

import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Build
import com.example.tempstructure.app.AppConstant

class PreferenceUtils(private val sharedPreferences: SharedPreferences) {

    // authToken
    fun getAuthToken(): String? {
        return sharedPreferences.getString(AppConstant.AUTH_KEY, "")
    }

    fun saveAuthToken(token: String?) {
        sharedPreferences.edit().putString(AppConstant.AUTH_KEY, token).apply()
    }

    fun isFirstTime(): Boolean{
        return sharedPreferences.getBoolean(AppConstant.FIRST_TIME_KEY, true)
    }

    fun setFirstTime(value: Boolean) {
        return sharedPreferences.edit().putBoolean(AppConstant.FIRST_TIME_KEY, value).apply()
    }

    fun isLogin(): Boolean{
        return sharedPreferences.getBoolean(AppConstant.LOGIN_KEY, false)
    }

    fun setLogin(login: Boolean) {
        return sharedPreferences.edit().putBoolean(AppConstant.LOGIN_KEY, login).apply()
    }

}
