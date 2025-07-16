package com.locationReminder.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.locationReminder.model.apiUtil.serviceModel.BaseNetworkSyncClass
import com.locationReminder.model.apiUtil.utils.NetworkResult
import com.locationReminder.model.localStorage.MySharedPreference
import com.locationReminder.reponseModel.LocationDetail
import com.locationReminder.reponseModel.MarkerUpdateRequest
import com.locationReminder.roomDatabase.repository.AddImportedCategoryDatabaseRepository
import com.locationReminder.roomDatabase.repository.AddLocationDatabaseRepository
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.atomic.AtomicInteger

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
        params["id"] = generate6DigitUniqueId()
        params["address"] = address
        params["entryType"] = entryType
        params["radius"] = radius
        params["currentStatus"] = currentStatus
        params["lat"] = lat
        params["lng"] = lng
        params["sendNotification"] = sendNotification
        params["vibration"] = vibration
        params["category_id"] = categoryId
        params["category_name"] = categoryTitle
        mySharedPreference.getUserId()?.toIntOrNull()?.let {
            params["user_id"] = it
        }
        _loading.postValue(true)

        when (baseNetworkCall.addMarkerList(params)) {
            is NetworkResult.Success -> {
                _successMessage.value="Marker added successfully"
                _loading.postValue(false)
            }

            is NetworkResult.Error -> {
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

        when (baseNetworkCall.updateMarkerStatus(itemId,params)) {
            is NetworkResult.Success -> {
                val itemId = itemId.removePrefix("eq.").toInt()
                updateCurrentStatus(itemId.toInt(),currentStatus )
                _successMessage.value="Record updated"

            }

            is NetworkResult.Error -> {
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
        params["category_name"] = categoryTitle


        mySharedPreference.getUserId()?.toIntOrNull()?.let {
            params["user_id"] = it
        } ?: run {
            Log.e("Supabase", "User ID is null or invalid")
        }
        _loading.postValue(true)

        when (baseNetworkCall.editMarker(itemId,params)) {
            is NetworkResult.Success -> {
                _loading.postValue(false)
            }

            is NetworkResult.Error -> {
                _loading.postValue(false)
            }

            is NetworkResult.Loading -> {
                _loading.postValue(true)
            }
        }
    }


    private val counter = AtomicInteger(0)

    fun generate6DigitUniqueId(): String {
        val timePart = (System.currentTimeMillis() / 1000) % 100000
        val countPart = counter.getAndIncrement() % 100
        val id = "$timePart${String.format("%02d", countPart)}"
        return id.padStart(6, '0').takeLast(6)
    }

    fun updateMarkers(updatedList: List<MarkerUpdateRequest>)= viewModelScope.launch {
        _loading.postValue(true)
        when (baseNetworkCall.updateMarkers(updatedList)) {
            is NetworkResult.Success -> {
               _successMessage.value="Record Imported"
            }

            is NetworkResult.Error -> {
                _loading.postValue(false)
            }

            is NetworkResult.Loading -> {
                _loading.postValue(true)
            }
        }
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
                _loading.postValue(false)
            }

            is NetworkResult.Loading -> {
                _loading.postValue(true)
            }
        }
    }


    fun getImportedMarkerList(categoryId: String, userId: String) = viewModelScope.launch {
        _loading.postValue(true)
        when (val result = baseNetworkCall.getImportedMarkerList(categoryId, userId)) {
            is NetworkResult.Success -> {
                val locationList = result.data

                if (!locationList.isNullOrEmpty()) {
                    viewModelScope.launch {
                        locationList.forEach { location ->
                            location.let {

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
                _loading.postValue(false)
            }

            is NetworkResult.Loading -> {
                _loading.postValue(true)
            }
        }
    }




    fun deleteMarkerList(ids: List<String>) = viewModelScope.launch {
        val filter = "in.(${ids.joinToString(",")})"

        _loading.postValue(true)
        when (baseNetworkCall.deleteMarkerList(filter)) {
            is NetworkResult.Success -> {
                val idInts = ids.mapNotNull { it.toIntOrNull() }
                addLocationDatabaseRepository.deleteMarkerList(idInts)
                _successMessage.value = "Record deleted"
            }

            is NetworkResult.Error -> {
                _loading.postValue(false)
            }

            is NetworkResult.Loading -> {
                _loading.postValue(true)
            }
        }
    }


    fun clearSuccessMessage() { _successMessage.value = null }


}
