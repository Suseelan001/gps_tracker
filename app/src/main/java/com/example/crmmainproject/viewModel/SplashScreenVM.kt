package com.example.crmmainproject.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crmmainproject.model.localStorage.MySharedPreference

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenVM @Inject constructor( private val mySharedPreference: MySharedPreference
): ViewModel(){

    private val _failure = MutableLiveData<String?>()
    val failure: LiveData<String?> get() = _failure

    private val _success = MutableLiveData<Boolean?>()
    val success: LiveData<Boolean?> get() = _success




    fun checkDeviceRegisterCompleted(): Boolean {
        return mySharedPreference.getAppRegisterCompletedStatus()
    }

    fun resetSuccess() {
        _success.value = false
    }

    fun resetFailure() {
        _failure.value = null
    }

    fun isUserLoggedIn(): Boolean {
        if (!mySharedPreference.getUserId().isNullOrEmpty()){
            return true
        }
        return false
    }

    fun isNewPasswordFlow(): Boolean {
        return mySharedPreference.getNewPasswordFlag()
    }
}