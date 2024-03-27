package com.example.kanban_board_java_kotlin.ui.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.kanban_board_java_kotlin.R
import com.example.kanban_board_java_kotlin.data.response.TaskResponse
import com.example.kanban_board_java_kotlin.databinding.RcvItemTodoTaskBinding
import com.example.kanban_board_java_kotlin.utils.formatSecondToString


class TodoTaskAdapter(var context: Context) : RecyclerView.Adapter<TodoTaskAdapter.MyViewHolder>() {

    private var list = ArrayList<TaskResponse>()

    var onItemClick: ((TaskResponse, Int) -> Unit)? = null

    var onItemMenu: ((TaskResponse, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            RcvItemTodoTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val data = list[position]

        holder.binding.apply {
            tvTitle.text = data.title
            tvDescription.text = data.description

            if (data.spentTime != 0L) {
                llSpentTime.isVisible = true
                tvStart.text = context.getString(R.string.resume)
                tvSpent.text = data.spentTime.formatSecondToString()
            } else {
                llSpentTime.isVisible = false
                tvStart.text = context.getString(R.string.start)
            }

            btnStart.setOnClickListener {
                onItemClick?.invoke(data, position)
            }

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

    class MyViewHolder(var binding: RcvItemTodoTaskBinding) : RecyclerView.ViewHolder(binding.root)

}