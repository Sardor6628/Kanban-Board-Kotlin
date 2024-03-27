package com.example.kanban_board_java_kotlin.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.example.kanban_board_java_kotlin.R
import com.example.kanban_board_java_kotlin.databinding.DialogAddTaskBinding
import com.example.kanban_board_java_kotlin.databinding.DialogLogoutBinding

class LogoutDialog(context: Context, private val email:String) : Dialog(context, R.style.Theme_Dialog) {

    lateinit var binding: DialogLogoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(true)
        binding = DialogLogoutBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        binding.tvNotice.text = "$email are you sure you want to logout?"

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

}