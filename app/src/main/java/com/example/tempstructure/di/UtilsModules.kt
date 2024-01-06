package com.example.tempstructure.di

import android.content.Context
import android.content.SharedPreferences
import com.example.tempstructure.app.AppConstant
import com.example.tempstructure.utils.PreferenceUtils
import com.example.tempstructure.utils.errorbean.ErrorUtils
import com.example.tempstructure.utils.resource.ResponseHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
class UtilsModules
{

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(AppConstant.PREF_KEY, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun providePrefUtils(preferences: SharedPreferences): PreferenceUtils {
        return PreferenceUtils(preferences)
    }

    @Singleton
    @Provides
    fun provideErrorUtils(@ApplicationContext context: Context): ErrorUtils {
        return ErrorUtils(context)
    }

    @Singleton
    @Provides
    fun provideResponseHandler(
        @ApplicationContext context: Context,
        errorUtils: ErrorUtils,
    ): ResponseHandler {
        return ResponseHandler(context, errorUtils)
    }

}