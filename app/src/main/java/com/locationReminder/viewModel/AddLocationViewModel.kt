package com.locationReminder.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.locationReminder.model.apiUtil.serviceModel.BaseNetworkSyncClass
import com.locationReminder.model.apiUtil.utils.NetworkResult
import com.locationReminder.reponseModel.LocationDetail
import com.locationReminder.roomDatabase.repository.AddLocationDatabaseRepository
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class AddLocationViewModel @Inject constructor(
    private val baseNetworkCall: BaseNetworkSyncClass,
    private val addLocationDatabaseRepository: AddLocationDatabaseRepository
) : ViewModel() {

    private val _locationDetail = MutableLiveData<LocationDetail?>()
    val locationDetail: LiveData<LocationDetail?> get() = _locationDetail

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading



    fun insertAccount(locationDetail: LocationDetail) {
        viewModelScope.launch {
            addLocationDatabaseRepository.insertAccount(locationDetail)
        }
    }
    fun updateRecord(locationDetail: LocationDetail) {
        viewModelScope.launch {
            addLocationDatabaseRepository.updateRecord(locationDetail)
        }
    }

    fun deleteItem(item: LocationDetail) {
        viewModelScope.launch {
            addLocationDatabaseRepository.deleteLocation(item)
        }
    }



    fun  updateCurrentStatus(locationId: Int,status: Boolean) {
        viewModelScope.launch {
            addLocationDatabaseRepository.updateCurrentStatus(locationId,status)
        }
    }




    fun clearUserDB() {
        viewModelScope.launch {
            addLocationDatabaseRepository.clearUserDB()
        }
    }

    fun getAllRecord(): LiveData<List<LocationDetail>> {
        return addLocationDatabaseRepository.getAllRecord()
    }
    fun getSingleRecord(id: Int):LocationDetail {
        return addLocationDatabaseRepository.getSingleRecord(id)
    }

    fun addMarkerList(
        title: String,
        address: String,
        entryType: String,
        radius: Float,
        lat: Double,
        lng: Double,
        currentStatus: Boolean,
        sendNotification: Boolean,
        vibration: Boolean
    ) = viewModelScope.launch {
        val params = mutableMapOf<String, Any>()
        params["title"] = title
        params["address"] = address
        params["entryType"] = entryType
        params["radius"] = radius
        params["currentStatus"] = currentStatus
        params["lat"] = lat
        params["lng"] = lng
        params["sendNotification"] = sendNotification
        params["vibration"] = vibration

        _loading.postValue(true)

        when (val result = baseNetworkCall.addMarkerList(params)) {
            is NetworkResult.Success -> {
                val userList = result.data

                if (userList.isNullOrEmpty()) {
                    _errorMessage.postValue("Email or Password is incorrect")
                } else {
                    val user = userList.firstOrNull()
                    user?.let {
                        _locationDetail.postValue(it)
                    }
                }

                _loading.postValue(false)
            }

            is NetworkResult.Error -> {
                _errorMessage.postValue(result.message ?: "Something went wrong")
                _loading.postValue(false)
            }

            is NetworkResult.Loading -> {
                _loading.postValue(true)
            }
        }
    }



    fun getMarkerList() = viewModelScope.launch {
        _loading.postValue(true)
        when (val result = baseNetworkCall.getMarkerList()) {
            is NetworkResult.Success -> {
                val userList = result.data


                    viewModelScope.launch {
                        userList?.forEach { user ->
                            addLocationDatabaseRepository.insertAccount(user)
                        }
                    }


                _loading.postValue(false)
            }

            is NetworkResult.Error -> {
                _errorMessage.postValue(result.message ?: "Something went wrong")
                _loading.postValue(false)
            }

            is NetworkResult.Loading -> {
                _loading.postValue(true)
            }
        }
    }

}
