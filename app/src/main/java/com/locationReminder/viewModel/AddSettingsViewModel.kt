package com.locationReminder.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.locationReminder.model.apiUtil.serviceModel.BaseNetworkSyncClass
import com.locationReminder.model.apiUtil.utils.NetworkResult
import com.locationReminder.model.localStorage.MySharedPreference
import com.locationReminder.reponseModel.CommonResponseModel
import com.locationReminder.reponseModel.SettingsData
import com.locationReminder.roomDatabase.repository.AddContactDatabaseRepository
import com.locationReminder.roomDatabase.repository.AddFolderDatabaseRepository
import com.locationReminder.roomDatabase.repository.AddImportedCategoryDatabaseRepository
import com.locationReminder.roomDatabase.repository.AddLocationDatabaseRepository
import com.locationReminder.roomDatabase.repository.SettingsDatabaseRepository
import com.locationReminder.roomDatabase.repository.UserDatabaseRepository
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class AddSettingsViewModel @Inject constructor(
    private val settingsDatabaseRepository: SettingsDatabaseRepository,
    private val mySharedPreference: MySharedPreference,
    private val addContactDatabaseRepository: AddContactDatabaseRepository,
    private val addFolderDatabaseRepository: AddFolderDatabaseRepository,
    private val addImportedCategoryDatabaseRepository: AddImportedCategoryDatabaseRepository,
    private val addLocationDatabaseRepository: AddLocationDatabaseRepository,
    private val userDatabaseRepository: UserDatabaseRepository,
    private val baseNetworkCall: BaseNetworkSyncClass,


    ) : ViewModel() {

    private val _updateResponse = MutableLiveData<CommonResponseModel?>()
    val updateResponse: LiveData<CommonResponseModel?> get() = _updateResponse

    private val _successMessage = MutableLiveData<String?>()
    val successMessage: LiveData<String?> get() = _successMessage


    fun callLogout(id: String ) = viewModelScope.launch {
        val params = mutableMapOf<String, Any>()

        params["fcm_token"] = ""


        when (baseNetworkCall.callLogout(id,params)) {
            is NetworkResult.Success -> {
                _successMessage.value = "Logout updated"
            }

            is NetworkResult.Error -> {
                _successMessage.value = "Logout error"


            }

            is NetworkResult.Loading -> {
            }
        }
    }


    fun clearUserDB() {
        viewModelScope.launch {
            settingsDatabaseRepository.clearUserDB()
        }
    }

    fun clearSuccessMessage() { _successMessage.value = null }


    fun getAllRecord(): LiveData<SettingsData> {
        return settingsDatabaseRepository.getAllRecord()
    }


    fun  updateUnit(unit: String) {
        viewModelScope.launch {
            settingsDatabaseRepository.updateUnit(unit)
        }
    }

    fun  updateLocationUpdateInterval(locationUpdateInterval: String) {
        viewModelScope.launch {
            settingsDatabaseRepository.updateLocationUpdateInterval(locationUpdateInterval)
        }
    }

    fun  updateEntryRadius(entryRadius: String) {
        viewModelScope.launch {
            settingsDatabaseRepository.updateEntryRadius(entryRadius)
        }
    }

    fun  updateExitRadius(exitRadius: String) {
        viewModelScope.launch {
            settingsDatabaseRepository.updateExitRadius(exitRadius)
        }
    }

    fun  updateMaximumRadius(maximumRadius: String) {
        viewModelScope.launch {
            settingsDatabaseRepository.updateMaximumRadius(maximumRadius)
        }
    }



    fun insertRecord(data: SettingsData) {
        viewModelScope.launch {
            settingsDatabaseRepository.insertRecord(data)
        }
    }

    fun ensureDefaultSettingsInserted() {
        viewModelScope.launch {
            val existing = settingsDatabaseRepository.getSettingsOnce()
            if (existing == null) {
                settingsDatabaseRepository.insertRecord(
                    SettingsData(
                        unit = "Meters/Kilometers",
                        locationUpdateInterval = "adaptable",
                        entryRadius = "100",
                        exitRadius = "100",
                        maximumRadius = "2000"
                    )
                )
            }
        }
    }


    fun appLogout() = viewModelScope.launch {
        clearData()
        _updateResponse.value = CommonResponseModel("false", "appLogout")
    }
    fun clearData() = viewModelScope.launch {
        mySharedPreference.clearAll()
        addContactDatabaseRepository.clearUserDB()
        addFolderDatabaseRepository.clearUserDB()
        addImportedCategoryDatabaseRepository.clearUserDB()
        addLocationDatabaseRepository.clearUserDB()
        userDatabaseRepository.clearUserDB()

    }


}
