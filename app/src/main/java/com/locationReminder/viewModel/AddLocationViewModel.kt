package com.locationReminder.viewModel

import android.util.Log
import androidx.compose.animation.scaleOut
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.locationReminder.model.apiUtil.serviceModel.BaseNetworkSyncClass
import com.locationReminder.model.apiUtil.utils.NetworkResult
import com.locationReminder.model.localStorage.MySharedPreference
import com.locationReminder.reponseModel.LocationDetail
import com.locationReminder.roomDatabase.repository.AddImportedCategoryDatabaseRepository
import com.locationReminder.roomDatabase.repository.AddLocationDatabaseRepository
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class AddLocationViewModel @Inject constructor(
    private val baseNetworkCall: BaseNetworkSyncClass,
    private val addLocationDatabaseRepository: AddLocationDatabaseRepository,
    private val mySharedPreference: MySharedPreference,
    private val addImportedCategoryDatabaseRepository: AddImportedCategoryDatabaseRepository
) : ViewModel() {

    val userId = mySharedPreference.getUserId()


    private val _locationDetail = MutableLiveData<List<LocationDetail>>()
    val locationDetail: LiveData<List<LocationDetail>> get() = _locationDetail


    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _successMessage = MutableLiveData<String?>()
    val successMessage: LiveData<String?> get() = _successMessage

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




    fun updateCurrentStatus(locationId: Int, status: Boolean) {
        viewModelScope.launch {
            addLocationDatabaseRepository.updateCurrentStatus(locationId, status)
        }
    }

    fun getAllRecord(): LiveData<List<LocationDetail>> {
        return addLocationDatabaseRepository.getAllRecord()
    }


    fun getMarkerListByFolder(categoryId: String,type: String): LiveData<List<LocationDetail>> {
        return addLocationDatabaseRepository.getMarkerListByFolder(categoryId,type) ?: MutableLiveData(emptyList())
    }

    fun getSingleRecord(id: Int): LocationDetail {
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
        vibration: Boolean,
        categoryId: String,
        categoryTitle: String
    ) = viewModelScope.launch {
        val params = mutableMapOf<String, Any>()
        params["title"] = title
        params["id"] = generateUniqueId()
        params["address"] = address
        params["entryType"] = entryType
        params["radius"] = radius
        params["currentStatus"] = currentStatus
        params["lat"] = lat
        params["lng"] = lng
        params["sendNotification"] = sendNotification
        params["vibration"] = vibration
        params["category_id"] = categoryId
        params["category_title"] = categoryTitle



        mySharedPreference.getUserId()?.toIntOrNull()?.let {
            params["user_id"] = it
        } ?: run {
            Log.e("Supabase", "User ID is null or invalid")
        }
        _loading.postValue(true)

        when (val result = baseNetworkCall.addMarkerList(params)) {
            is NetworkResult.Success -> {
                _successMessage.value="Marker added successfully"
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

    fun updateMarkerStatus(
        itemId: String,
        currentStatus: Boolean,
    ) = viewModelScope.launch {
        val params = mutableMapOf<String, Any>()
        params["currentStatus"] = currentStatus
        _loading.postValue(true)

        when (val result = baseNetworkCall.updateMarkerStatus(itemId,params)) {
            is NetworkResult.Success -> {
                val itemId = itemId.removePrefix("eq.").toInt()
                updateCurrentStatus(itemId.toInt(),currentStatus )
                _successMessage.value="Record updated"

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

    fun editMarker(
        itemId: String,
        title: String,
        address: String,
        entryType: String,
        radius: Float,
        lat: Double,
        lng: Double,
        currentStatus: Boolean,
        sendNotification: Boolean,
        vibration: Boolean,
        categoryId: String,
        categoryTitle: String
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
        params["category_id"] = categoryId
        params["category_title"] = categoryTitle


        mySharedPreference.getUserId()?.toIntOrNull()?.let {
            params["user_id"] = it
        } ?: run {
            Log.e("Supabase", "User ID is null or invalid")
        }
        _loading.postValue(true)

        when (val result = baseNetworkCall.editMarker(itemId,params)) {
            is NetworkResult.Success -> {
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


    fun generateUniqueId(length: Int = 5): String {
        val chars = "0123456789"
        return (1..length)
            .map { chars.random() }
            .joinToString("")
    }

    fun getMarkerList(categoryId: String, userId: String) = viewModelScope.launch {
        _loading.postValue(true)
        when (val result = baseNetworkCall.getMarkerList(categoryId, userId)) {
            is NetworkResult.Success -> {
                val locationList = result.data
                if (locationList?.isNotEmpty() == true) {
                    viewModelScope.launch {
                        locationList.forEach { location ->

                                addLocationDatabaseRepository.insertAccount(location)

                        }

                    }
                    _locationDetail.value = locationList
                    _loading.postValue(false)
                }else {
                    _locationDetail.value = emptyList()
                    _loading.postValue(false)
                }
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


    fun getImportedMarkerList(categoryTitle: String, userId: String) = viewModelScope.launch {
        _loading.postValue(true)
        when (val result = baseNetworkCall.getImportedMarkerList(categoryTitle, userId)) {
            is NetworkResult.Success -> {
                val locationList = result.data

                if (!locationList.isNullOrEmpty()) {
                    viewModelScope.launch {
                        locationList.forEach { location ->
                            location.let {
                                val categoryId = it.category_id
                                val categoryTitle = it.category_title

                                if (!categoryId.isNullOrEmpty()) {
                                    addImportedCategoryDatabaseRepository.updateRecordId(
                                        categoryId.toInt(),
                                        categoryTitle.toString()
                                    )
                                    println("CHECK_TAG_location categoryId: $categoryId, categoryTitle: $categoryTitle")
                                }

                                val updatedLocation = it.copy(entryType = "ImportedMarker")
                                addLocationDatabaseRepository.insertAccount(updatedLocation)
                            }
                        }
                    }

                }
                _successMessage.value="Import Record"

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


    fun deleteMarker(id: String) = viewModelScope.launch {
        _loading.postValue(true)
        when (val result = baseNetworkCall.deleteMarker(id)) {
            is NetworkResult.Success -> {
                val cleanId = id.removePrefix("eq.").toInt()
                addLocationDatabaseRepository.deleteSingleRecord(cleanId)
                _successMessage.value="Record deleted"
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
    fun clearSuccessMessage() { _successMessage.value = null }


}
