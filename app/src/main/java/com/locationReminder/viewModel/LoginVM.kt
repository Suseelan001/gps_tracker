package com.locationReminder.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.locationReminder.model.apiUtil.serviceModel.BaseNetworkSyncClass
import com.locationReminder.model.apiUtil.utils.NetworkResult
import com.locationReminder.model.localStorage.MySharedPreference
import com.locationReminder.roomDatabase.repository.UserDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginVM @Inject constructor(
    private val baseNetworkCall: BaseNetworkSyncClass,
    private val mySharedPreference: MySharedPreference,
    private val userDatabaseRepository: UserDatabaseRepository
) : ViewModel() {

    private val _userDetail = MutableLiveData<UserDetailResponseModel?>()
    val userDetail: LiveData<UserDetailResponseModel?> get() = _userDetail


    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading


    fun callSignUp(
        username: String,
        mobileNumber: String,
        email: String,
        password: String,
    ) = viewModelScope.launch {
        val params = mutableMapOf<String, Any>()
        params["username"] = username
        params["email"] = email
        params["pass"] = password
        params["mobilenumber"] = mobileNumber
        _loading.postValue(true)
        when (val result = baseNetworkCall.callSignUp(params)) {
            is NetworkResult.Success -> {
                val users = result.data
                if (!users.isNullOrEmpty()) {
                    val user = users.first()
                    mySharedPreference.saveUserId(user.id!!.toString())
                    mySharedPreference.saveUserName(user.username!!.toString())
                    userDatabaseRepository.insertUser(user)
                    _userDetail.postValue(user)
                } else {
                    _errorMessage.postValue("No user returned from API")
                }
                _loading.postValue(false)
            }

            is NetworkResult.Error -> {
                _errorMessage.postValue(result.message)
                _loading.postValue(false)
            }

            is NetworkResult.Loading -> {
            }
        }
    }

    fun callLogin(
        email: String,
        password: String,
    ) = viewModelScope.launch {
        val params = mutableMapOf<String, Any>()
        params["email"] = email
        params["pass"] = password

        _loading.postValue(true)

        when (val result = baseNetworkCall.callLogin(params)) {
            is NetworkResult.Success -> {
                val userList = result.data

                if (userList.isNullOrEmpty()) {
                    _errorMessage.postValue("Email or Password is incorrect")
                } else {
                    val user = userList.firstOrNull()
                    user?.let {
                        mySharedPreference.saveUserId(it.id!!.toString())
                        mySharedPreference.saveUserName(it.username!!.toString())
                        userDatabaseRepository.insertUser(it)
                        _userDetail.postValue(it)
                    }
                }

                _loading.postValue(false)
            }

            is NetworkResult.Error -> {
                println("CHECK_TAG_ERROR " + result.message)
                _errorMessage.postValue(result.message ?: "Something went wrong")
                _loading.postValue(false)
            }

            is NetworkResult.Loading -> {
                _loading.postValue(true)
            }
        }
    }



    fun clearErrorMessage(){
        _errorMessage.postValue(null)

    }
}


