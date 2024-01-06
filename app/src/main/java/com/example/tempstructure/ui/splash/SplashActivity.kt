package com.example.tempstructure.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.example.tempstructure.app.BaseActivity
import com.example.tempstructure.ui.intro.IntroActivity
import com.example.tempstructure.ui.home.HomeActivity
import com.example.tempstructure.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startTimer()
    }

    private fun startTimer() {
        val thread: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(1000)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    if (prefsUtils.isLogin()) {
                        val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this@SplashActivity, IntroActivity::class.java)
                        startActivity(intent)
                    }
                    finish()
                }
            }
        }
        thread.start()
    }

}