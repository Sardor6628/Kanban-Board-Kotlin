package com.example.tempstructure.api

import com.example.tempstructure.data.response.TimeResponse
import retrofit2.Response
import retrofit2.http.GET

interface AppApi {

    @GET("http://worldtimeapi.org/api/timezone/utc")
    suspend fun time(): Response<TimeResponse>

}