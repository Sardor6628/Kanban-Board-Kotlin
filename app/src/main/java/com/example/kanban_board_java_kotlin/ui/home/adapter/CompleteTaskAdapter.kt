package com.example.kanban_board_java_kotlin.ui.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.kanban_board_java_kotlin.R
import com.example.kanban_board_java_kotlin.data.response.TaskResponse
import com.example.kanban_board_java_kotlin.databinding.RcvItemCompleteTaskBinding
import com.example.kanban_board_java_kotlin.utils.formatSecondToString
import com.example.kanban_board_java_kotlin.utils.formatString

class CompleteTaskAdapter(var context: Context) : RecyclerView.Adapter<CompleteTaskAdapter.MyViewHolder>(){

    private var list = ArrayList<TaskResponse>()

    var onItemMenu: ((TaskResponse, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RcvItemCompleteTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

            if (data.completedTime != null){
                tvCompleteTime.isVisible = true
                tvCompleteTime.text = "Completed at: " + (data.completedTime!!/1000).formatString("yyyy/MM/dd, HH:mm:ss")
            }else{
                tvCompleteTime.isVisible = false
            }

            if (data.spentTime == 0L){
                llSpentTime.isVisible = false
            }
            tvSpent.text = data.spentTime.formatSecondToString()

            btnMore.setOnClickListener {
                val popupMenu = PopupMenu(context, btnMore)
                popupMenu.menuInflater.inflate(R.menu.task_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    onItemMenu?.invoke(data, menuItem.itemId)
                    true
                }
                popupMenu.show()
            }
        }
    }

    fun setData(data: ArrayList<TaskResponse>) {
        list = data
        notifyDataSetChanged()
    }

    class MyViewHolder(var binding: RcvItemCompleteTaskBinding) : RecyclerView.ViewHolder(binding.root)

}