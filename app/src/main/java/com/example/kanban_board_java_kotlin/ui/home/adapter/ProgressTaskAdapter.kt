package com.example.kanban_board_java_kotlin.ui.home.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kanban_board_java_kotlin.data.response.TaskResponse
import com.example.kanban_board_java_kotlin.databinding.RcvItemProgressTaskBinding
import com.example.kanban_board_java_kotlin.utils.formatSecondToString
import com.example.kanban_board_java_kotlin.utils.getSecondBetweenTime
import java.util.Timer
import java.util.TimerTask

class ProgressTaskAdapter(var context: Activity) :
    RecyclerView.Adapter<ProgressTaskAdapter.MyViewHolder>() {

    private var list = ArrayList<TaskResponse>()
    private var timersMap = HashMap<Int, Timer?>()

    var onItemClickComplete: ((TaskResponse, Int) -> Unit)? = null

    var onItemClickPause: ((TaskResponse, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            RcvItemProgressTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        val startTime = (data.startedTime) ?: 1L

        holder.binding.apply {
            tvTitle.text = data.title
            tvDescription.text = data.description

            tvSpent.text = startTime.getSecondBetweenTime(System.currentTimeMillis()).plus(data.spentTime).formatSecondToString()

            if (timersMap[position] == null) {
                timersMap[position] = Timer()
                timersMap[position]?.schedule(object : TimerTask() {
                    override fun run() {
                        context.runOnUiThread {
                            val totalSecond = startTime.getSecondBetweenTime(System.currentTimeMillis()).plus(data.spentTime)
                            tvSpent.text = totalSecond.formatSecondToString()
                        }
                    }
                }, 1000, 1000)
            }

            btnComplete.setOnClickListener {
                timersMap[position]?.purge()
                timersMap[position]?.cancel()
                timersMap[position] = null
                data.spentTime = startTime.getSecondBetweenTime(System.currentTimeMillis()).plus(data.spentTime)
                onItemClickComplete?.invoke(data, position)
            }

            btnPause.setOnClickListener {
                timersMap[position]?.purge()
                timersMap[position]?.cancel()
                timersMap[position] = null
                data.spentTime = startTime.getSecondBetweenTime(System.currentTimeMillis()).plus(data.spentTime)
                onItemClickPause?.invoke(data, position)
            }
        }
    }

    fun setData(data: ArrayList<TaskResponse>) {
        timersMap.forEach {
            it.value?.purge()
            it.value?.cancel()
            timersMap[it.key] = null
        }
        list = data
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class MyViewHolder(var binding: RcvItemProgressTaskBinding) :
        RecyclerView.ViewHolder(binding.root)

}