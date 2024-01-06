package com.example.tempstructure.di

import com.example.tempstructure.api.AppApi
import com.example.tempstructure.repository.TimeRepository
import com.example.tempstructure.utils.PreferenceUtils
import com.example.tempstructure.utils.resource.ResponseHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideTimeRepository(
        api: AppApi,
        responseHandler: ResponseHandler
    ): TimeRepository {
        return TimeRepository(api, responseHandler)
    }

}