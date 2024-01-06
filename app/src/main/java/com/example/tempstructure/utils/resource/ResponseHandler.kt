package com.example.tempstructure.utils.resource

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.example.tempstructure.app.AppConstant
import com.example.tempstructure.utils.errorbean.ErrorUtils
import com.example.tempstructure.utils.exceptions.UnauthorizedException
import retrofit2.HttpException
import retrofit2.Response

class ResponseHandler(
    val context: Context,
    private val errorUtils: ErrorUtils
) {
    fun <T : Any> handleResponse(response: Response<T>): Resource<T> {
        when {
            response.isSuccessful -> {
                val responseBody = response.body()
                return when {
                    response.code() in (200..299) -> {
                        Resource.success(responseBody)
                    }

                    else -> {
                        when {
                            responseBody != null -> {
                                Resource.success(responseBody)
                            }

                            else -> {
                                Resource.exception(HttpException(response))
                            }
                        }
                    }
                }
            }

            response.code() == 422
                    || response.code() == 500
                    || response.code() == 400
                    || response.code() == 423
                    || response.code() == 403 -> {
                val body = response.errorBody()
                val adapter = Gson().getAdapter(Any::class.java)
                val errorParser = adapter.fromJson(body?.string())
                val json = Gson().toJson(errorParser)
                return Resource.error(json)
            }

            response.code() == 401 -> {
                /*val intent = Intent(AppConstant.COIN_BROADCAST)
                intent.putExtra(AppConstant.SESSION_TYPE_AUTH, "yes")
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent)*/
                return Resource.exception(UnauthorizedException())
            }

            response.code() == 404 -> {
                return Resource.exception(IllegalStateException())
            }

            else -> {
                Log.e("ACE", "exception: ")
                return Resource.exception(IllegalStateException())
            }
        }
    }
}