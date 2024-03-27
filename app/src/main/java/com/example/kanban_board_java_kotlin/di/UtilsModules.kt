package com.example.kanban_board_java_kotlin.di

import android.content.Context
import android.content.SharedPreferences
import com.example.kanban_board_java_kotlin.app.AppConstant
import com.example.kanban_board_java_kotlin.utils.PreferenceUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

}