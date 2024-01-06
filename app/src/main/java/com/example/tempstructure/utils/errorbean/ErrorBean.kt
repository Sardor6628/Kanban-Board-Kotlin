package com.example.tempstructure.utils.errorbean

data class ErrorBean(
    val throwable: Throwable,
    val error: Int = 0,
    val errorImage: Int = 0,
    val code: Int = 0,
)