package com.locationReminder.roomDatabase.repository




import androidx.lifecycle.LiveData
import com.locationReminder.reponseModel.SettingsData
import com.locationReminder.roomDatabase.dao.SettingsDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class SettingsDatabaseRepository @Inject constructor(
    private val settingsDAO: SettingsDAO
) {

    suspend fun insertRecord(task: SettingsData) {
        withContext(Dispatchers.IO) {
            settingsDAO.insertRecord(task)
        }
    }


    suspend fun clearUserDB() {
        withContext(Dispatchers.IO) {
            settingsDAO.clearUserDB()
        }
    }



    fun getAllRecord(): LiveData<SettingsData> {
        return settingsDAO.getAllRecord()
    }




    suspend fun updateUnit(unit: String) {
        withContext(Dispatchers.IO) {
            settingsDAO.updateUnit(unit)
        }
    }


    suspend fun updateLocationUpdateInterval(locationUpdateInterval: String) {
        withContext(Dispatchers.IO) {
            settingsDAO.updateLocationUpdateInterval(locationUpdateInterval)
        }
    }


    suspend fun updateEntryRadius(entryRadius: String) {
        withContext(Dispatchers.IO) {
            settingsDAO.updateEntryRadius(entryRadius)
        }
    }


    suspend fun updateExitRadius(exitRadius: String) {
        withContext(Dispatchers.IO) {
            settingsDAO.updateExitRadius(exitRadius)
        }
    }


    suspend fun updateMaximumRadius(maximumRadius: String) {
        withContext(Dispatchers.IO) {
            settingsDAO.updateMaximumRadius(maximumRadius)
        }
    }

}
