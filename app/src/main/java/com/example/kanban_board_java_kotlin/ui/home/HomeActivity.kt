package com.example.kanban_board_java_kotlin.ui.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kanban_board_java_kotlin.R
import com.example.kanban_board_java_kotlin.app.AppConstant
import com.example.kanban_board_java_kotlin.app.BaseActivity
import com.example.kanban_board_java_kotlin.app.KanbanBoardApp
import com.example.kanban_board_java_kotlin.data.response.TaskResponse
import com.example.kanban_board_java_kotlin.databinding.ActivityHomeBinding
import com.example.kanban_board_java_kotlin.dialog.AddTaskDialog
import com.example.kanban_board_java_kotlin.dialog.LogoutDialog
import com.example.kanban_board_java_kotlin.ui.completetask.CompleteTaskActivity
import com.example.kanban_board_java_kotlin.ui.home.adapter.CompleteTaskAdapter
import com.example.kanban_board_java_kotlin.ui.home.adapter.ProgressTaskAdapter
import com.example.kanban_board_java_kotlin.ui.home.adapter.TodoTaskAdapter
import com.example.kanban_board_java_kotlin.ui.login.LoginActivity
import com.example.kanban_board_java_kotlin.utils.resource.Status
import com.example.kanban_board_java_kotlin.viewmodel.TaskViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


@OptIn(ExperimentalCoroutinesApi::class)
@AndroidEntryPoint
class HomeActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityHomeBinding

    private val viewModel: TaskViewModel by viewModels()

    private var todoTaskAdapter: TodoTaskAdapter? = null
    private var progressTaskAdapter: ProgressTaskAdapter? = null
    private var completedTaskAdapter: CompleteTaskAdapter? = null

    private var todoTaskList = arrayListOf<TaskResponse>()
    private var progressTaskList = arrayListOf<TaskResponse>()
    private var completedTaskList = arrayListOf<TaskResponse>()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observer()
        initView()
        setClick()
    }

    private fun initView() {
        binding.rvTodoTask.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvProgressTask.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvCompleteTask.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        viewModel.getTasksByStatus(AppConstant.TODO_STATUS_VALUE)
        viewModel.getTasksByStatus(AppConstant.PROGRESS_STATUS_VALUE)
        viewModel.getTasksByStatus(AppConstant.COMPLETED_STATUS_VALUE)
    }

    private fun setClick() {
        binding.btnAddTask.setOnClickListener(this)
        binding.btnAddNewTask.setOnClickListener(this)
        binding.btnViewAll.setOnClickListener(this)
        binding.btnLogout.setOnClickListener(this)
    }

    private fun observer() {
        viewModel.todoTaskListState().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (todoTaskAdapter == null){
                        todoTaskAdapter = TodoTaskAdapter(this)
                        binding.rvTodoTask.adapter = todoTaskAdapter

                        todoTaskAdapter!!.onItemClick = { data, pos ->
                            data.currentStatus = AppConstant.PROGRESS_STATUS_VALUE
                            data.startedTime = System.currentTimeMillis() * 1000
                            viewModel.updateTodoTask(data)
                            todoTaskList.removeAt(pos)
                            todoTaskAdapter!!.setData(todoTaskList)
                            binding.vaTodoTask.displayedChild = if (todoTaskList.isEmpty()) {1} else {2}
                        }

                        todoTaskAdapter!!.onItemMenu = { data, id ->
                            menuClick(id, data)
                        }
                    }

                    if (it.data == null){
                        binding.vaTodoTask.displayedChild = 1
                    }else{
                        todoTaskList = it.data
                        todoTaskAdapter!!.setData(todoTaskList)
                        binding.vaTodoTask.displayedChild = if (todoTaskList.isEmpty()) {1} else {2}
                    }
                }
                Status.LOADING -> { binding.vaTodoTask.displayedChild = 0 }
                Status.ERROR -> { binding.vaTodoTask.displayedChild = 3 }
                Status.EXCEPTION -> { binding.vaTodoTask.displayedChild = 3 }
            }
        }

        viewModel.progressTaskListState().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (progressTaskAdapter == null){
                        progressTaskAdapter = ProgressTaskAdapter(this)
                        binding.rvProgressTask.adapter = progressTaskAdapter

                        progressTaskAdapter!!.onItemClickComplete = { data, pos ->
                            data.currentStatus = AppConstant.COMPLETED_STATUS_VALUE
                            data.completedTime = System.currentTimeMillis() * 1000
                            viewModel.updateTodoTask(data)
                            progressTaskList.removeAt(pos)
                            progressTaskAdapter!!.setData(progressTaskList)
                            binding.vaProgressTask.displayedChild = if (progressTaskList.isEmpty()) {1} else {2}
                        }

                        progressTaskAdapter!!.onItemClickPause = { data, pos ->
                            data.currentStatus = AppConstant.TODO_STATUS_VALUE
                            viewModel.updateTodoTask(data)
                            progressTaskList.removeAt(pos)
                            progressTaskAdapter!!.setData(progressTaskList)
                            binding.vaProgressTask.displayedChild = if (progressTaskList.isEmpty()) {1} else {2}
                        }
                    }

                    if (it.data == null){
                        binding.vaProgressTask.displayedChild = 1
                    }else{
                        progressTaskList = it.data
                        progressTaskAdapter!!.setData(progressTaskList)
                        binding.vaProgressTask.displayedChild = if (progressTaskList.isEmpty()) {1} else {2}
                    }
                }
                Status.LOADING -> { binding.vaProgressTask.displayedChild = 0 }
                Status.ERROR -> { binding.vaProgressTask.displayedChild = 3 }
                Status.EXCEPTION -> { binding.vaProgressTask.displayedChild = 3 }
            }
        }

        viewModel.completedTaskListState().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (completedTaskAdapter == null){
                        completedTaskAdapter = CompleteTaskAdapter(this)
                        binding.rvCompleteTask.adapter = completedTaskAdapter

                        completedTaskAdapter!!.onItemMenu = { data, id ->
                            menuClick(id, data)
                        }
                    }

                    if (it.data == null){
                        binding.vaCompleteTask.displayedChild = 1
                    }else{
                        completedTaskList = it.data
                        completedTaskAdapter!!.setData(completedTaskList)
                        binding.vaCompleteTask.displayedChild = if (completedTaskList.isEmpty()) {1} else {2}
                    }
                }
                Status.LOADING -> { binding.vaCompleteTask.displayedChild = 0 }
                Status.ERROR -> { binding.vaCompleteTask.displayedChild = 3 }
                Status.EXCEPTION -> { binding.vaCompleteTask.displayedChild = 3 }
            }
        }
    }

    private fun menuClick(id: Int, data: TaskResponse){
        when(id){
            R.id.edit -> {
                val addTaskDialog = AddTaskDialog(this)
                addTaskDialog.show()

                addTaskDialog.binding.tvTitle.text = getString(R.string.update)
                addTaskDialog.binding.tvAdd.text = getString(R.string.update)

                addTaskDialog.binding.etTitle.setText(data.title)
                addTaskDialog.binding.etDescription.setText(data.description)

                addTaskDialog.binding.btnAdd.setOnClickListener {
                    if (addTaskDialog.binding.etTitle.text.toString().isNotEmpty()){
                        if (addTaskDialog.binding.etDescription.text.toString().isNotEmpty()){
                            data.title = addTaskDialog.binding.etTitle.text.toString()
                            data.description = addTaskDialog.binding.etDescription.text.toString()
                            addTaskDialog.dismiss()
                            viewModel.updateTodoTask(data, -1)
                        }else{
                            KanbanBoardApp.getAppInstance().showToast("Please enter description")
                        }
                    }else{
                        KanbanBoardApp.getAppInstance().showToast("Please enter title")
                    }
                }
            }
            R.id.delete -> {
                viewModel.deleteTodoTask(data)
            }
            R.id.reset -> {
                data.spentTime = 0L
                viewModel.updateTodoTask(data, -1)
            }
        }
    }

    override fun onClick(p0: View?) {
        when(p0){
            binding.btnAddNewTask,
            binding.btnAddTask -> {
                val addTaskDialog = AddTaskDialog(this)
                addTaskDialog.show()

                addTaskDialog.binding.btnAdd.setOnClickListener {
                    if (addTaskDialog.binding.etTitle.text.toString().isNotEmpty()){
                        if (addTaskDialog.binding.etDescription.text.toString().isNotEmpty()){
                            val taskResponse = TaskResponse()
                            taskResponse.title = addTaskDialog.binding.etTitle.text.toString()
                            taskResponse.description = addTaskDialog.binding.etDescription.text.toString()
                            taskResponse.userId = prefsUtils.getUserId()
                            taskResponse.createdTime = System.currentTimeMillis() * 1000
                            viewModel.addTodoTask(taskResponse)
                            addTaskDialog.dismiss()
                        }else{
                            KanbanBoardApp.getAppInstance().showToast("Please enter description")
                        }
                    }else{
                        KanbanBoardApp.getAppInstance().showToast("Please enter title")
                    }
                }
            }

            binding.btnViewAll -> {
                startActivity(Intent(this, CompleteTaskActivity::class.java))
            }

            binding.btnLogout -> {
                val logOutDialog = LogoutDialog(this,
                    FirebaseAuth.getInstance().currentUser?.email.toString()
                )
                logOutDialog.show()
                logOutDialog.binding.btnLogout.setOnClickListener {
                    logOutDialog.dismiss()
                    logout()
                }
            }
        }
    }

    private fun logout() {
        showProgress()
        val firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        if (user != null) {
            prefsUtils.setLogin(false)
            prefsUtils.setUserId("")
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        hideProgress()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTasksByStatus(AppConstant.TODO_STATUS_VALUE, false)
        viewModel.getTasksByStatus(AppConstant.PROGRESS_STATUS_VALUE, false)
        viewModel.getTasksByStatus(AppConstant.COMPLETED_STATUS_VALUE, false)
    }

}