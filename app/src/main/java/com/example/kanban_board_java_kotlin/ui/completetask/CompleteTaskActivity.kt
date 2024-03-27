package com.example.kanban_board_java_kotlin.ui.completetask

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kanban_board_java_kotlin.R
import com.example.kanban_board_java_kotlin.app.AppConstant
import com.example.kanban_board_java_kotlin.app.BaseActivity
import com.example.kanban_board_java_kotlin.app.KanbanBoardApp
import com.example.kanban_board_java_kotlin.data.response.TaskResponse
import com.example.kanban_board_java_kotlin.databinding.ActivityCompleteTaskBinding
import com.example.kanban_board_java_kotlin.ui.completetask.adapter.CompleteTaskShowAdapter
import com.example.kanban_board_java_kotlin.utils.formatSecondToString
import com.example.kanban_board_java_kotlin.utils.formatString
import com.example.kanban_board_java_kotlin.utils.resource.Status
import com.example.kanban_board_java_kotlin.viewmodel.TaskViewModel
import com.opencsv.CSVWriter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter
import java.io.IOException


@OptIn(ExperimentalCoroutinesApi::class)
@AndroidEntryPoint
class CompleteTaskActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityCompleteTaskBinding

    private val viewModel: TaskViewModel by viewModels()

    private var completeTaskShowAdapter: CompleteTaskShowAdapter? = null

    private var completedTaskList = arrayListOf<TaskResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompleteTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observer()
        initView()
        setClick()
    }

    private fun initView() {
        binding.rvCompleteTask.layoutManager = LinearLayoutManager(this)

        viewModel.getTasksByStatus(AppConstant.COMPLETED_STATUS_VALUE)
    }

    private fun setClick() {
        binding.btnBack.setOnClickListener(this)
        binding.btnExport.setOnClickListener(this)
    }

    private fun observer() {
        viewModel.completedTaskListState().observe(this) {
            Log.e("TAG", "observer: "+it.status)
            when (it.status) {
                Status.SUCCESS -> {
                    if (completeTaskShowAdapter == null){
                        completeTaskShowAdapter = CompleteTaskShowAdapter(this)
                        binding.rvCompleteTask.adapter = completeTaskShowAdapter
                    }

                    if (it.data == null){
                        binding.vaCompleteTask.displayedChild = 1
                    }else{
                        completedTaskList = it.data
                        completeTaskShowAdapter!!.setData(completedTaskList)
                        binding.vaCompleteTask.displayedChild = if (completedTaskList.isEmpty()) {1} else {2}
                    }
                }
                Status.LOADING -> { binding.vaCompleteTask.displayedChild = 0 }
                Status.ERROR -> { binding.vaCompleteTask.displayedChild = 3 }
                Status.EXCEPTION -> { binding.vaCompleteTask.displayedChild = 3 }
            }
        }
    }

    override fun onClick(p0: View?) {
        when(p0){
            binding.btnBack -> {
                onBackPressedDispatcher.onBackPressed()
            }
            binding.btnExport -> {
                if (completedTaskList.isNotEmpty()) {
                    generateCSV(completedTaskList)
                } else {
                    KanbanBoardApp.getAppInstance().showToast("Not found completed task")
                }
            }
        }
    }

    private fun generateCSV(taskList: ArrayList<TaskResponse>){
        showProgress()
        CoroutineScope(Dispatchers.IO).launch {
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/completedTask_" + System.currentTimeMillis() + ".csv")
            try {
                val writer = CSVWriter(FileWriter(file))

                val header = arrayOf("Title", "Description", "Created Date", "Created Time", "Completed Date", "Completed Time", "Spent Time")
                writer.writeNext(header)

                taskList.forEach {
                    val data1 = arrayOf(
                        it.title,
                        it.description,
                        (it.createdTime!!/1000).formatString("yyyy.MM.dd"),
                        (it.createdTime!!/1000).formatString("HH:mm:ss"),
                        (it.completedTime!!/1000).formatString("yyyy.MM.dd"),
                        (it.completedTime!!/1000).formatString("HH:mm:ss"),
                        it.spentTime.formatSecondToString(),
                    )
                    writer.writeNext(data1)
                }
                writer.close()
                runOnUiThread {
                    hideProgress()
                    KanbanBoardApp.getAppInstance().showToast("File save in download folder")
                }
            } catch (e: IOException) {
                hideProgress()
                KanbanBoardApp.getAppInstance().showToast(getString(R.string.something_went_wrong))
                e.printStackTrace()
            }
        }

    }

}

