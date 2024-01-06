package com.example.tempstructure.app

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.example.tempstructure.R
import com.example.tempstructure.utils.PreferenceUtils
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class TempStructureApp : MultiDexApplication(), Application.ActivityLifecycleCallbacks,
    LifecycleObserver {

    lateinit var activity: Activity

    @Inject
    lateinit var prefsUtils: PreferenceUtils

    var isShowOpenAds = true

    companion object {
        lateinit var instance: TempStructureApp
        fun getAppInstance(): TempStructureApp {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        loadFirebaseConfig()
        initLifeCycle()
        loadMobileAds()
    }

    private fun initLifeCycle() {
        registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        MultiDex.install(this)
    }

    private fun loadMobileAds() {
        MobileAds.initialize(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        showOpenAds(activity)
    }

    private fun showOpenAds(activity: Activity) {
        if (isShowOpenAds) {
            AppOpenAd.load(
                this,
                FirebaseRemoteConfig.getInstance().getString(AppConstant.APP_OPEN_ADS_KEY),
                AdRequest.Builder().build(),
                object : AppOpenAd.AppOpenAdLoadCallback() {
                    override fun onAdLoaded(ad: AppOpenAd) {
                        super.onAdLoaded(ad)
                        if (prefsUtils.isFirstTime()){
                            prefsUtils.setFirstTime(false)
                        }else{
                            if (FirebaseRemoteConfig.getInstance().getLong(AppConstant.ADS_LEVEL).toInt() == 3) {
                                ad.show(activity)
                                isShowOpenAds = false
                            }
                        }
                    }
                })
        }
    }

    private fun loadFirebaseConfig() {
        FirebaseApp.initializeApp(this)
        FirebaseRemoteConfig.getInstance().apply {
            val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(5)
                .build()
            setConfigSettingsAsync(configSettings)
            setDefaultsAsync(R.xml.remote_config_defaults)
        }
        FirebaseRemoteConfig.getInstance().fetchAndActivate()

        AppConstant.APP_LINK = FirebaseRemoteConfig.getInstance().getString(AppConstant.APP_LINK_KEY)
        AppConstant.PRIVACY_POLICY_LINK = FirebaseRemoteConfig.getInstance().getString(AppConstant.PRIVACY_LINK_KEY)
        AppConstant.ACCOUNT_LINK = FirebaseRemoteConfig.getInstance().getString(AppConstant.ACCOUNT_LINK_KEY)
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        this.activity = activity
    }

    override fun onActivityResumed(p0: Activity) {}

    override fun onActivityPaused(p0: Activity) {}

    override fun onActivityStopped(p0: Activity) {}

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}

    override fun onActivityDestroyed(p0: Activity) {}


}