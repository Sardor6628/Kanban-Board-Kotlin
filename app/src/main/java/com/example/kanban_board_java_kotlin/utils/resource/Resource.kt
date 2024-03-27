package com.example.kanban_board_java_kotlin.utils.resource

import com.example.kanban_board_java_kotlin.utils.errorbean.ErrorBean

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val error: Any?,
    val errorBean: ErrorBean?,
) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null, null)
        }

        fun <T> success(data: T?, systemApp: Int?): Resource<T> {
            return Resource(Status.SUCCESS, data, systemApp, null)
        }

        fun <T> error(error: Any): Resource<T> {
            return Resource(Status.ERROR, null, error, null)
        }

        fun <T> exception(error: Throwable): Resource<T> {
            return Resource(Status.EXCEPTION, null, error, null)
        }

        fun <T> exceptionBean(error: ErrorBean): Resource<T> {
            return Resource(Status.EXCEPTION, null, null, error)
        }

        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING, null, null, null)
        }
    }
}