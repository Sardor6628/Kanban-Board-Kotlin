package com.example.kanban_board_java_kotlin.ui.completetask.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kanban_board_java_kotlin.data.response.TaskResponse
import com.example.kanban_board_java_kotlin.databinding.RcvItemCompleteShowTaskBinding
import com.example.kanban_board_java_kotlin.databinding.RcvItemCompleteTaskBinding
import com.example.kanban_board_java_kotlin.utils.formatSecondToString
import com.example.kanban_board_java_kotlin.utils.formatString

class CompleteTaskShowAdapter(var context: Context) : RecyclerView.Adapter<CompleteTaskShowAdapter.MyViewHolder>(){

    private var list = ArrayList<TaskResponse>()

    var onItemClick: ((TaskResponse, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RcvItemCompleteShowTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val data = list[position]

        holder.binding.apply {
            tvTitle.text = data.title
            tvDescription.text = data.description
            tvSpent.text = data.spentTime.formatSecondToString()
            tvCompleteTime.text = (data.completedTime!!/1000).formatString("yyyy/MM/dd, HH:mm:ss")
        }
    }

    fun setData(data: ArrayList<TaskResponse>) {
        list = data
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class MyViewHolder(var binding: RcvItemCompleteShowTaskBinding) : RecyclerView.ViewHolder(binding.root)

}