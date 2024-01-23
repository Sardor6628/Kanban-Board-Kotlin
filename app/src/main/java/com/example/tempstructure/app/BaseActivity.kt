package com.example.tempstructure.app

import android.Manifest
import android.R
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Display
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tempstructure.BuildConfig
import com.example.tempstructure.dialog.LoadingDialog
import com.example.tempstructure.dialog.UpdateAppDialog
import com.example.tempstructure.utils.AdsActionInterface
import com.example.tempstructure.utils.PreferenceUtils
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var prefsUtils: PreferenceUtils

    private var dialog: LoadingDialog? = null

    private var isShowBannerAds = true
    private var isShowInterstitialAds = true
    private var isShowRewardInterstitialAds = true
    private var isShowRewardAds = true
    private var isShowNativeAds = true

    private var interstitialAds: InterstitialAd? = null
    private var rewardAds: RewardedAd? = null
    private var rewardInterstitialAds: RewardedInterstitialAd? = null

    private fun initDialog() {
        if (!isFinishing) {
            dialog = LoadingDialog(this)
            dialog?.show()
        }
    }

    fun hideProgress() {
        if (!isFinishing && dialog?.isShowing == true) {
            dialog?.dismiss()
        }
    }

    fun showProgress() {
        if (dialog == null) {
            initDialog()
        } else {
            if (dialog != null && dialog?.isShowing == true) {
                dialog?.dismiss()
                initDialog()
            } else {
                initDialog()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (FirebaseRemoteConfig.getInstance().getLong(AppConstant.VERSION_KEY) > BuildConfig.VERSION_CODE.toLong()) {
            UpdateAppDialog(this).show()
        }

        AppConstant.APP_LINK = FirebaseRemoteConfig.getInstance().getString(AppConstant.APP_LINK_KEY)
        AppConstant.PRIVACY_POLICY_LINK = FirebaseRemoteConfig.getInstance().getString(AppConstant.PRIVACY_LINK_KEY)
        AppConstant.ACCOUNT_LINK = FirebaseRemoteConfig.getInstance().getString(AppConstant.ACCOUNT_LINK_KEY)

        isShowBannerAds = false
        isShowInterstitialAds = false
        isShowRewardInterstitialAds = false
        isShowRewardAds = false
        isShowNativeAds = false

        when (FirebaseRemoteConfig.getInstance().getLong(AppConstant.ADS_LEVEL).toInt()) {
            0 -> {
                isShowBannerAds = false
                isShowInterstitialAds = false
                isShowRewardInterstitialAds = false
                isShowRewardAds = false
            }

            1 -> {
                isShowBannerAds = true
            }

            2 -> {
                isShowBannerAds = true
                isShowInterstitialAds = true
            }

            3 -> {
                isShowBannerAds = true
                isShowInterstitialAds = true
                isShowRewardInterstitialAds = true
                isShowRewardAds = true
                isShowNativeAds = true
            }
        }
    }

    fun loadBanner(view: RelativeLayout) {
        if (isShowBannerAds) {
            val adView = AdView(this)
            adView.setAdSize(getAdSize())
            adView.adUnitId =
                FirebaseRemoteConfig.getInstance().getString(AppConstant.BANNER_ADS_KEY)
            view.addView(adView)
            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
        }
    }

    fun loadBannerItem(view: RelativeLayout, adsSize: AdSize = getAdSize()): Boolean {
        if (isShowBannerAds) {
            val adView = AdView(this)
            adView.setAdSize(adsSize)
            adView.adUnitId =
                FirebaseRemoteConfig.getInstance().getString(AppConstant.BANNER_ADS_KEY)
            view.addView(adView)
            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
        }
        return isShowBannerAds
    }

    fun loadInterstitialAds() {
        if (isShowInterstitialAds) {
            val adRequest = AdRequest.Builder().build()
            InterstitialAd.load(this,
                FirebaseRemoteConfig.getInstance().getString(AppConstant.INTERSTITIAL_ADS_KEY),
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {}
                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        interstitialAds = interstitialAd
                    }
                })
        }
    }

    fun loadRewardAds() {
        if (isShowRewardAds) {
            val adRequest = AdRequest.Builder().build()
            RewardedAd.load(this,
                FirebaseRemoteConfig.getInstance().getString(AppConstant.REWARD_ADS_KEY),
                adRequest,
                object : RewardedAdLoadCallback() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {}
                    override fun onAdLoaded(ad: RewardedAd) {
                        rewardAds = ad
                    }
                })
        }
    }

    fun loadRewardInterstitialAd() {
        RewardedInterstitialAd.load(this,
            FirebaseRemoteConfig.getInstance().getString(AppConstant.REWARD_INTERSTITIAL_ADS_KEY),
            AdRequest.Builder().build(),
            object : RewardedInterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {}
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    rewardInterstitialAds = ad
                }
            })
    }

    fun showInterstitialAds(adsAction: AdsActionInterface) {
        if (isShowInterstitialAds) {
            if (interstitialAds != null) {
                interstitialAds?.show(this)
                interstitialAds?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdClicked() {}
                    override fun onAdDismissedFullScreenContent() {
                        adsAction.onClose()
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        adsAction.onError()
                    }

                    override fun onAdImpression() {
                        adsAction.onSuccess()
                    }

                    override fun onAdShowedFullScreenContent() {
                        adsAction.onSuccess()
                    }
                }
            } else {
                adsAction.onError()
            }
        } else {
            adsAction.onError()
        }
    }

    fun showRewardAds(adsAction: AdsActionInterface) {
        if (isShowRewardAds) {
            if (rewardAds != null) {
                rewardAds?.show(this) {}
                rewardAds?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdClicked() {}
                    override fun onAdDismissedFullScreenContent() {
                        adsAction.onClose()
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        adsAction.onError()
                    }

                    override fun onAdImpression() {
                        adsAction.onSuccess()
                    }

                    override fun onAdShowedFullScreenContent() {
                        adsAction.onSuccess()
                    }
                }
            } else {
                adsAction.onError()
            }
        } else {
            adsAction.onError()
        }
    }

    fun showRewardInterstitialAd(adsAction: AdsActionInterface) {
        if (isShowRewardInterstitialAds) {
            if (rewardInterstitialAds != null) {
                rewardInterstitialAds?.show(this) {}
                rewardInterstitialAds?.fullScreenContentCallback =
                    object : FullScreenContentCallback() {
                        override fun onAdClicked() {}
                        override fun onAdDismissedFullScreenContent() {
                            adsAction.onClose()
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            adsAction.onError()
                        }

                        override fun onAdImpression() {
                            adsAction.onSuccess()
                        }

                        override fun onAdShowedFullScreenContent() {
                            adsAction.onSuccess()
                        }
                    }
            } else {
                adsAction.onError()
            }
        } else {
            adsAction.onError()
        }
    }

    private fun getAdSize(): AdSize {
        val display: Display = this.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels.toFloat()
        val density = outMetrics.density
        val adWidth = (widthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
    }

    fun requestPermission(context: Activity, permission: ArrayList<String>) {
        /*val notGrantedList = arrayListOf<String>()

        permission.forEach{
            if (ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED) {
                notGrantedList.add(it)
            }
        }*/

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS), AppConstant.PERMISSION_CODE
                )
            };
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}