package com.example.tempstructure.utils.errorbean

import android.content.Context
import com.google.gson.JsonSyntaxException
import com.example.tempstructure.R
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ErrorUtils(private val context: Context) {
    fun handleErrorImage(e: Throwable): ErrorBean {
        when (e) {
            is IllegalStateException -> {
                return ErrorBean(
                    e,
                    ErrorType.ERROR_INVALID_RESPONSE.errorMessage,
                    R.drawable.ic_api_unexpected_response
                )
            }
            is SocketTimeoutException -> {
                return ErrorBean(
                    e,
                    ErrorType.ERROR_REQUEST_TIME_OUT.errorMessage,
                    R.drawable.ic_api_request_time_out
                )
            }
            is UnknownHostException -> {
                return ErrorBean(
                    e,
                    ErrorType.ERROR_CONNECTIVITY.errorMessage,
                    R.drawable.ic_api_connectivity
                )
            }
            is ConnectException -> {
                return ErrorBean(
                    e,
                    ErrorType.ERROR_CONNECTIVITY.errorMessage,
                    R.drawable.ic_api_connectivity
                )
            }
            is HttpException -> {
                return ErrorBean(
                    e,
                    ErrorType.ERROR_INVALID_RESPONSE.errorMessage,
                    R.drawable.ic_api_unexpected_response
                )
            }
            is JsonSyntaxException -> {
                return ErrorBean(
                    e,
                    ErrorType.ERROR_INVALID_RESPONSE.errorMessage,
                    R.drawable.ic_api_unexpected_response
                )
            }
            else -> {
                return ErrorBean(
                    e,
                    ErrorType.PARSING_ERROR.errorMessage,
                    R.drawable.ic_api_parsing_error
                )
            }
        }

    }

}