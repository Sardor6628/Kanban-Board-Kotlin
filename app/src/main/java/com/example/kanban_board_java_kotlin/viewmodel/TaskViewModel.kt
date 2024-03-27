package com.example.kanban_board_java_kotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kanban_board_java_kotlin.app.AppConstant
import com.example.kanban_board_java_kotlin.app.KanbanBoardApp
import com.example.kanban_board_java_kotlin.data.response.TaskResponse
import com.example.kanban_board_java_kotlin.utils.PreferenceUtils
import com.example.kanban_board_java_kotlin.utils.resource.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.Objects
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class TaskViewModel @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val preferenceUtils: PreferenceUtils
) : ViewModel() {

    fun todoTaskListState(): LiveData<Resource<ArrayList<TaskResponse>>> { return todoTaskListState }
    fun progressTaskListState(): LiveData<Resource<ArrayList<TaskResponse>>> { return progressTaskListState }
    fun completedTaskListState(): LiveData<Resource<ArrayList<TaskResponse>>> { return completedTaskListState }

    private val todoTaskListState: MutableLiveData<Resource<ArrayList<TaskResponse>>> = MutableLiveData()
    private val progressTaskListState: MutableLiveData<Resource<ArrayList<TaskResponse>>> = MutableLiveData()
    private val completedTaskListState: MutableLiveData<Resource<ArrayList<TaskResponse>>> = MutableLiveData()

    fun getTasksByStatus(taskStatus: String, isLoading: Boolean = true){
        val taskListState = when (taskStatus) {
            AppConstant.TODO_STATUS_VALUE -> todoTaskListState
            AppConstant.PROGRESS_STATUS_VALUE -> progressTaskListState
            AppConstant.COMPLETED_STATUS_VALUE -> completedTaskListState
            else -> null
        }

        viewModelScope.launch {
            if (isLoading) {
                taskListState?.value = Resource.loading()
            }

            val taskList = arrayListOf<TaskResponse>()
            val taskCollection = fireStore.collection("tasks").whereEqualTo("userId", preferenceUtils.getUserId()).whereEqualTo("currentStatus", taskStatus)

            taskCollection.get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        val taskResponse = setTaskResponse(document.id, document.data)
                        taskList.add(taskResponse)
                    }
                    taskList.sortByDescending {
                        when(taskStatus) {
                            AppConstant.TODO_STATUS_VALUE -> it.createdTime
                            AppConstant.PROGRESS_STATUS_VALUE -> it.createdTime
                            AppConstant.COMPLETED_STATUS_VALUE -> it.completedTime
                            else -> null
                        }
                    }
                    taskListState?.value = if (taskList.isEmpty()) {
                        Resource.success(null)
                    } else {
                        Resource.success(taskList)
                    }
                }.addOnFailureListener { exception ->
                    taskListState?.value = Resource.error(exception)
                }
        }
    }

    fun addTodoTask(taskResponse: TaskResponse) {
        fireStore.collection("tasks").add(setTaskResponseToFirebase(taskResponse)).addOnCompleteListener {
            taskResponse.documentId = it.result.id
            if (todoTaskListState.value?.data == null) {
                val list = arrayListOf<TaskResponse>()
                list.add(taskResponse)
                todoTaskListState.value = Resource.success(list)
            } else {
                todoTaskListState.value?.data?.add(taskResponse)
                todoTaskListState.value?.data?.sortByDescending { data ->
                    data.createdTime
                }
                todoTaskListState.value = Resource.success(todoTaskListState.value?.data)
            }
            KanbanBoardApp.getAppInstance().showToast("Task Added")
        }
    }

    fun updateTodoTask(taskResponse: TaskResponse, type: Int = 0) {
        val taskListState = when (taskResponse.currentStatus) {
            AppConstant.TODO_STATUS_VALUE -> todoTaskListState
            AppConstant.PROGRESS_STATUS_VALUE -> progressTaskListState
            AppConstant.COMPLETED_STATUS_VALUE -> completedTaskListState
            else -> null
        }

        taskListState?.value?.data?.let { list ->
            if (type == -1) {
                val pos = list.indexOfFirst { it.id == taskResponse.id }
                if (pos != -1) {
                    list[pos] = taskResponse
                } else {
                    getTasksByStatus(AppConstant.TODO_STATUS_VALUE)
                }
            } else {
                list.add(taskResponse)
            }
            list.sortByDescending {
                when(taskResponse.currentStatus) {
                    AppConstant.TODO_STATUS_VALUE -> it.createdTime
                    AppConstant.PROGRESS_STATUS_VALUE -> it.createdTime
                    AppConstant.COMPLETED_STATUS_VALUE -> it.completedTime
                    else -> null
                }
            }
            taskListState.value = Resource.success(list)
        } ?: run {
            val newList = arrayListOf(taskResponse)
            taskListState?.value = Resource.success(newList)
        }

        fireStore.collection("tasks").document(taskResponse.documentId).set(setTaskResponseToFirebase(taskResponse))
            .addOnCompleteListener {
                KanbanBoardApp.getAppInstance().showToast("Task Updated")
            }
            .addOnFailureListener {
                KanbanBoardApp.getAppInstance().showToast(it.message.toString())
            }
    }

    fun deleteTodoTask(taskResponse: TaskResponse) {
        val taskListState = when (taskResponse.currentStatus) {
            AppConstant.TODO_STATUS_VALUE -> todoTaskListState
            AppConstant.PROGRESS_STATUS_VALUE -> progressTaskListState
            AppConstant.COMPLETED_STATUS_VALUE -> completedTaskListState
            else -> null
        }

        taskListState?.value?.data?.let { list ->
            val pos = list.indexOfFirst { it.id == taskResponse.id }
            if (pos != -1) {
                list.removeAt(pos)
                taskListState.value = Resource.success(list)
            } else {
                KanbanBoardApp.getAppInstance().showToast("Task not deleted")
            }
        }

        fireStore.collection("tasks").document(taskResponse.documentId).delete()
            .addOnCompleteListener {
                KanbanBoardApp.getAppInstance().showToast("Task Deleted")
            }
            .addOnFailureListener {
                KanbanBoardApp.getAppInstance().showToast(it.message.toString())
            }
    }

    private fun setTaskResponse(documentId: String, data: MutableMap<String, Any>?): TaskResponse {
        val taskResponse = TaskResponse()
        taskResponse.documentId = documentId

        data.let {
            taskResponse.id = data?.getValue("id").toString()
            taskResponse.title = data?.getValue("title").toString()
            taskResponse.description = data?.getValue("description").toString()
            taskResponse.currentStatus = data?.getValue("currentStatus").toString()
            taskResponse.userId = data?.getValue("userId").toString()
            taskResponse.startedTime = data?.getValue("startedTime") as? Long
            taskResponse.createdTime = data?.getValue("createdTime") as? Long
            taskResponse.completedTime = data?.getValue("completedTime") as? Long
            taskResponse.spentTime = data?.getValue("spentTime") as? Long ?: 0
        }

        return taskResponse
    }

    private fun setTaskResponseToFirebase(taskResponse: TaskResponse): Any {
        return hashMapOf(
            "id" to taskResponse.id,
            "title" to taskResponse.title,
            "userId" to taskResponse.userId,
            "description" to taskResponse.description,
            "createdTime" to taskResponse.createdTime,
            "completedTime" to taskResponse.completedTime,
            "startedTime" to taskResponse.startedTime,
            "spentTime" to taskResponse.spentTime,
            "currentStatus" to taskResponse.currentStatus
        ) as Any
    }

}