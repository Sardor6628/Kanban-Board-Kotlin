package com.example.kanban_board_java_kotlin.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.content.res.AppCompatResources
import com.example.kanban_board_java_kotlin.R
import com.example.kanban_board_java_kotlin.databinding.DialogLoadingBinding

class LoadingDialog(context: Context) : Dialog(context, R.style.Theme_Dialog) {

    lateinit var binding: DialogLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(AppCompatResources.getDrawable(context, R.color.transparent))
        setCancelable(false)
        binding = DialogLoadingBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
    }

}