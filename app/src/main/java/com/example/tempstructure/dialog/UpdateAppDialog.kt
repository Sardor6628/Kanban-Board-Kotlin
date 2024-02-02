package com.example.tempstructure.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.content.res.AppCompatResources
import com.example.tempstructure.R
import com.example.tempstructure.app.AppConstant
import com.example.tempstructure.databinding.DialogUpdateAppBinding
import com.example.tempstructure.utils.extensions.openLink

class UpdateAppDialog(context: Context) : Dialog(context, R.style.Theme_Dialog) {

    private lateinit var binding: DialogUpdateAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(AppCompatResources.getDrawable(context, com.google.android.material.R.color.mtrl_btn_transparent_bg_color))
        binding = DialogUpdateAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCancel.setOnClickListener{
            dismiss()
        }

        binding.btnUpdate.setOnClickListener {
            AppConstant.APP_LINK.openLink(context)
        }

    }
}