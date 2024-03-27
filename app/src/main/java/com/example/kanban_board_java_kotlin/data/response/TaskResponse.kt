package com.example.kanban_board_java_kotlin.data.response

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskResponse(
    var id: String = System.currentTimeMillis().toString(),
    @get:Exclude var documentId: String = "",
    var title: String = "",
    var userId: String = "",
    var description: String = "",
    var createdTime: Long? = null,
    var completedTime: Long? = null,
    var startedTime: Long? = null,
    var spentTime: Long = 0,
    var currentStatus: String = "todo"
) : Parcelable