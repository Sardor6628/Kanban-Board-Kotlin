package com.example.kanban_board_java_kotlin.utils.errorbean

data class ErrorBean(
    val throwable: Throwable,
    val error: Int = 0,
    val errorImage: Int = 0,
    val code: Int = 0,
)