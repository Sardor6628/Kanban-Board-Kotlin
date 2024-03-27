package com.example.kanban_board_java_kotlin.app

import android.app.Activity
import android.app.Application
import android.widget.Toast
import com.example.kanban_board_java_kotlin.utils.PreferenceUtils
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class KanbanBoardApp : Application() {

    lateinit var activity: Activity

    @Inject
    lateinit var prefsUtils: PreferenceUtils

    var isShowOpenAds = true

    companion object {
        lateinit var instance: KanbanBoardApp
        fun getAppInstance(): KanbanBoardApp {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}