package com.example.tempstructure.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tempstructure.data.response.TimeResponse
import com.example.tempstructure.repository.TimeRepository
import com.example.tempstructure.utils.resource.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class TimeViewModel @Inject constructor(
    private val timeRepository: TimeRepository
) : ViewModel() {

    private val timeResponseState: MutableLiveData<Resource<TimeResponse>> =
        MutableLiveData()

    fun timeResponseState(): LiveData<Resource<TimeResponse>> {
        return timeResponseState
    }

    fun getTime() {
        viewModelScope.launch {
            timeRepository.getTime()
                .onEach { state ->
                    timeResponseState.value = state
                }
                .launchIn(viewModelScope)
        }
    }

}