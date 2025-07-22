package com.locationReminder.viewModel


import androidx.lifecycle.ViewModel
import com.locationReminder.model.localStorage.MySharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedPreferenceVM @Inject constructor(
    private val mySharedPreference: MySharedPreference
) : ViewModel() {

    fun isUserLoggedIn(): Boolean {
        return !mySharedPreference.getUserId().isNullOrEmpty()
    }

    fun getUserId(): String {
        return mySharedPreference.getUserId() ?: ""
    }

    fun getUserName(): String {
        return mySharedPreference.getUserName() ?: ""
    }
    fun getArea(): String {
        return mySharedPreference.getArea() ?: ""
    }

    fun setArea(value: String) {
        mySharedPreference.setArea(value)
    }

    fun getImportList(): Boolean {
        return mySharedPreference.getImportList() == true
    }

    fun setImportList(value: Boolean) {
        mySharedPreference.setImportList(value)
    }
    fun setNewUser(value: Boolean) {
        mySharedPreference.setNewUser(value)
    }


    fun getNewUser(): Boolean {
        return   mySharedPreference.getNewUser() == true
    }


}