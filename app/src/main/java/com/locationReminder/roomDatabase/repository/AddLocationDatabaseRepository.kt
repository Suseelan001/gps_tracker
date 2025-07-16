package com.locationReminder.roomDatabase.repository

import androidx.lifecycle.LiveData
import com.locationReminder.reponseModel.LocationDetail
import com.locationReminder.roomDatabase.dao.LocationDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AddLocationDatabaseRepository @Inject constructor(
    private val locationDAO: LocationDAO
) {

    suspend fun insertAccount(task: LocationDetail) {
        withContext(Dispatchers.IO) {
            locationDAO.insertAccount(task)
        }
    }
   suspend fun updateRecord(task: LocationDetail) {
        withContext(Dispatchers.IO) {
            locationDAO.updateRecord(task)
        }
    }


    suspend fun enableNotificationsForExitType() {
        withContext(Dispatchers.IO) {
            locationDAO.enableNotificationsForExitType()
        }
    }
    suspend fun updateCurrentStatus(locationId: Int,status: Boolean) {
        withContext(Dispatchers.IO) {
            locationDAO.updateCurrentStatus(locationId, status)
        }
    }

    suspend fun clearUserDB() {
        withContext(Dispatchers.IO) {
            locationDAO.clearUserDB()
        }
    }


    fun getAllRecord(): LiveData<List<LocationDetail>> {
        return locationDAO.getAllRecord( )
    }


    fun getMarkerListByFolder(categoryId: String,type: String): LiveData<List<LocationDetail>>? {
        return locationDAO.getMarkerListByFolder(categoryId,type)
    }

    fun getSingleRecord(id: Int): LocationDetail{
        return locationDAO.getSingleRecord(id)
    }


    suspend fun deleteLocation(location: LocationDetail) {
        locationDAO.deleteLocation(location)
    }

    fun deleteMarkerListByFolder(categoryId: String,type: String) {
        locationDAO.deleteMarkerListByFolder(categoryId,type )
    }

    fun deleteMarkerList(ids: List<Int>) {
        ids.forEach {
            locationDAO.deleteSingleRecord(it)
        }
    }


}
