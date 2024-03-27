package com.example.kanban_board_java_kotlin.app

import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.example.kanban_board_java_kotlin.dialog.LoadingDialog
import com.example.kanban_board_java_kotlin.utils.PreferenceUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var prefsUtils: PreferenceUtils

    private var dialog: LoadingDialog? = null

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

    open fun isValidEmail(target: CharSequence?): Boolean {
        return target != null && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}