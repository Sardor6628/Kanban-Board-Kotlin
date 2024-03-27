package com.example.kanban_board_java_kotlin.viewmodel

import android.R.attr
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kanban_board_java_kotlin.data.response.UserResponse
import com.example.kanban_board_java_kotlin.repository.AuthRepository
import com.example.kanban_board_java_kotlin.utils.resource.Resource
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _googleSignIn: MutableLiveData<Resource<UserResponse>> = MutableLiveData()

    fun googleSignInNetworkState(): LiveData<Resource<UserResponse>> {
        return _googleSignIn
    }

    fun signInWithGoogle(authCredential: AuthCredential) {
        viewModelScope.launch {
            repository.signInWithAuthCredential(authCredential)
                .onEach { state ->
                    _googleSignIn.value = state
                }
                .launchIn(viewModelScope)
        }
    }

    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _googleSignIn.value = Resource.loading()
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = firebaseAuth.currentUser
                        if (firebaseUser != null) {
                            val uid = firebaseUser.uid
                            val name = firebaseUser.displayName
                            firebaseUser.getIdToken(false).addOnSuccessListener {
                                it.token
                            }
                            val user = UserResponse(uid, name)
                            _googleSignIn.value = Resource.success(user)
                        } else {
                            _googleSignIn.value = Resource.error("Something Went Wrong")
                        }
                    }else{
                        _googleSignIn.value = Resource.error("Something Went Wrong")
                    }
                }.addOnFailureListener {
                    _googleSignIn.value = Resource.error(it.message.toString())
                }
        }
    }

    fun signUpWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _googleSignIn.value = Resource.loading()
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                    val firebaseUser = firebaseAuth.currentUser
                    if (firebaseUser != null) {
                        val uid = firebaseUser.uid
                        val name = firebaseUser.displayName
                        firebaseUser.getIdToken(false).addOnSuccessListener {
                            it.token
                        }
                        val user = UserResponse(uid, name)
                        _googleSignIn.value = Resource.success(user)
                    } else {
                        _googleSignIn.value = Resource.error("Something Went Wrong")
                    }
                }).addOnFailureListener {
                    _googleSignIn.value = Resource.error(it.message.toString())
                }
        }
    }
}