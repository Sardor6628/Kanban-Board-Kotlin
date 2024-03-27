package com.example.kanban_board_java_kotlin.repository

import android.util.Log
import com.example.kanban_board_java_kotlin.data.response.UserResponse
import com.example.kanban_board_java_kotlin.utils.resource.Resource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    suspend fun signInWithAuthCredential(authCredential: AuthCredential): Flow<Resource<UserResponse>> =
        flow {
            emit(Resource.loading())
            try {
                val result = firebaseAuth.signInWithCredential(authCredential).await()
                if (result != null) {
                    val firebaseUser = firebaseAuth.currentUser
                    if (firebaseUser != null) {
                        val uid = firebaseUser.uid
                        val name = firebaseUser.displayName
                        firebaseUser.getIdToken(false).addOnSuccessListener {
                            it.token
                        }
                        val user = UserResponse(uid, name)
                        emit(Resource.success(user))
                    } else {
                        emit(Resource.error("something went wrong"))
                    }

                } else {
                    emit(Resource.error("something went wrong"))
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("Tag", "" + e.message)
                emit(Resource.exception(e))
            }
        }.catch {
            Log.e("Tag", "" + it.message)
            emit(Resource.exception(it))
        }

}