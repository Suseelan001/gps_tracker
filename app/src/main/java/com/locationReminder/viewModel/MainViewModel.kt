package com.locationReminder.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel:ViewModel() {


    private val _loadingRequestCircle = MutableLiveData<Boolean>()
    val loadingRequestCircle: LiveData<Boolean> get() = _loadingRequestCircle



    var broadcastMessage by mutableIntStateOf(0)



    private val _autoLogout = MutableLiveData<Boolean>()
    val autoLogout: LiveData<Boolean> get() = _autoLogout


    private val _onResumeCall = MutableLiveData<Boolean>()
    val onResumeCall: LiveData<Boolean> get() = _onResumeCall



    fun updateResumeStatus() {
        _onResumeCall.value=true
    }
    fun  resetResumeStatus(){
        _onResumeCall.value=false
    }

    fun updateAutoLogoutStatus(status: Boolean) {
        _autoLogout.value=status
    }
}