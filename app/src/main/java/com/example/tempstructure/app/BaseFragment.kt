package com.example.tempstructure.app

import androidx.fragment.app.Fragment
import com.example.tempstructure.dialog.LoadingDialog
import com.example.tempstructure.utils.PreferenceUtils
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
open class BaseFragment : Fragment() {


    @Inject
    lateinit var prefsUtils: PreferenceUtils


    private var dialog: LoadingDialog? = null

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

    private fun initDialog() {
        if (context != null && isVisible) {
            dialog = LoadingDialog(requireContext())
            dialog?.show()
        }
    }

    fun hideProgress() {
        if (context != null && isVisible && dialog?.isShowing == true) {
            dialog?.dismiss()
        }
    }




}