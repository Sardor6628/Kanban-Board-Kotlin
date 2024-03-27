package com.example.kanban_board_java_kotlin.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.example.kanban_board_java_kotlin.R
import com.example.kanban_board_java_kotlin.databinding.DialogAddTaskBinding

class AddTaskDialog(context: Context) : Dialog(context, R.style.Theme_Dialog) {

    lateinit var binding: DialogAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(true)
        binding = DialogAddTaskBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

}