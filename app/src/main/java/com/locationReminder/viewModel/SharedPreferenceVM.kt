package com.locationReminder.viewModel


import androidx.lifecycle.ViewModel
import com.locationReminder.model.localStorage.MySharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedPreferenceVM @Inject constructor(private val mySharedPreference: MySharedPreference
): ViewModel(){

    fun isUserLoggedIn(): Boolean {
        return !mySharedPreference.getUserId().isNullOrEmpty()
    }

    fun getUserId(): String {
        return mySharedPreference.getUserId() ?: ""
    }

    fun getExitListExists(): Boolean {
        return mySharedPreference.getExitListExists() == true
    }
    fun setExitListExists(value: Boolean) {
         mySharedPreference.setExitListExists(value)
    }










}