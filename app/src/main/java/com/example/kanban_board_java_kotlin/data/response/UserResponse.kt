package com.example.kanban_board_java_kotlin.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(
    @SerializedName("uid")
    var uid: String? = null,
    @SerializedName("name")
    val name: String? = null
) : Parcelable  
