package com.example.tempstructure.app

import android.Manifest
import android.annotation.SuppressLint

class AppConstant {

    companion object {
        //App
        var ACCOUNT_LINK: String = "https://play.google.com/store/apps/developer?id=Net+Utility"
        var PRIVACY_POLICY_LINK: String = "https://netutilityapps.blogspot.com/2023/12/bulk-app-uninstaller.html"
        var APP_LINK: String = "https://play.google.com/store/apps/details?id=com.example.tempstructure"

        //Sever
        const val BASE_URL = ""

        //Permissions
        const val PERMISSION_CODE: Int = 102203

        //Pref Key
        const val PREF_KEY: String = "pref_key"
        const val AUTH_KEY: String = "auth_key"
        const val FIRST_TIME_KEY: String = "first_time"
        const val LOGIN_KEY: String = "login_key"

        //Remote key
        const val APP_LINK_KEY: String = "account_link"
        const val ACCOUNT_LINK_KEY: String = "account_link"
        const val PRIVACY_LINK_KEY: String = "privacy_policy_link"
        const val VERSION_KEY: String = "version"
        const val ADS_LEVEL: String = "ads_level"
        const val BANNER_ADS_KEY: String = "banner_ads_key"
        const val INTERSTITIAL_ADS_KEY: String = "interstitial_ads_key"
        const val REWARD_ADS_KEY: String = "reward_ads_key"
        const val REWARD_INTERSTITIAL_ADS_KEY: String = "reward_interstitial_ads_key"
        const val NATIVE_ADS_KEY: String = "native_ads_key"
        const val APP_OPEN_ADS_KEY: String = "app_open_ads_key"

        //Ads
        const val NORMAL_LAYOUT_CODE = 111
        const val ADS_LAYOUT_CODE = 112
    }

}