package com.example.kanban_board_java_kotlin.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.example.kanban_board_java_kotlin.app.AppConstant
import com.example.kanban_board_java_kotlin.app.BaseActivity
import com.example.kanban_board_java_kotlin.databinding.ActivitySplashBinding
import com.example.kanban_board_java_kotlin.ui.home.HomeActivity
import com.example.kanban_board_java_kotlin.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefsUtils.setUserId("SIVmFx5GaLcErR9I47mv7gv8yPs2")
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
                    val intent = if (prefsUtils.isLogin()) {
                        Intent(this@SplashActivity, HomeActivity::class.java).apply {
                            putExtra(AppConstant.LOGIN_KEY, 0)
                        }
                    } else {
                        Intent(this@SplashActivity, LoginActivity::class.java)
                    }
                    startActivity(intent)
                    finish()
                }
            }
        }
        thread.start()
    }

}