package com.example.tempstructure.repository

import android.util.Log
import com.example.tempstructure.api.AppApi
import com.example.tempstructure.data.response.TimeResponse
import com.example.tempstructure.utils.resource.Resource
import com.example.tempstructure.utils.resource.ResponseHandler
import com.example.tempstructure.utils.resource.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class TimeRepository
@Inject constructor(
    private val api: AppApi,
    private val responseHandler: ResponseHandler
) {
    suspend fun getTime(): Flow<Resource<TimeResponse>> = flow {
        emit(Resource.loading())
        try {
            val response = responseHandler.handleResponse(api.time())
            if (response.status == Status.SUCCESS) {
                val responseBody = response.data
                emit(
                    Resource.success(responseBody)
                )
            } else if (response.status == Status.ERROR) {
                emit(
                    Resource.error(response.error.toString())
                )
            } else if (response.status == Status.EXCEPTION) {
                emit(
                    Resource.exception(response.error as Throwable)
                )
            }
        } catch (e: Exception) {
            emit(Resource.exception(e))
        }
    }.catch {
        it.printStackTrace()
        emit(Resource.exception(it))
    }


}