package com.locationReminder.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.locationReminder.reponseModel.SettingsData
import com.locationReminder.roomDatabase.repository.SettingsDatabaseRepository
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class AddSettingsViewModel @Inject constructor(
    private val settingsDatabaseRepository: SettingsDatabaseRepository
) : ViewModel() {

    fun insertRecord(settingsData: SettingsData) {
        viewModelScope.launch {
            settingsDatabaseRepository.insertRecord(settingsData)
        }
    }
    fun clearUserDB() {
        viewModelScope.launch {
            settingsDatabaseRepository.clearUserDB()
        }
    }

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

}
