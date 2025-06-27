package com.locationReminder.roomDatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.locationReminder.reponseModel.LocationDetail


@Dao
interface LocationDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(task: LocationDetail)

    @Update
    suspend fun updateRecord(task: LocationDetail)


    @Query("SELECT * FROM add_location_dao")
    fun getAllRecord(): LiveData<List<LocationDetail>>


    @Query("SELECT * FROM add_location_dao")
    fun getAllRecordsNow(): List<LocationDetail>

    @Query("SELECT * FROM add_location_dao WHERE entryType = :type AND category_id = :categoryId")
    fun getMarkerListByFolder(categoryId: String, type: String): LiveData<List<LocationDetail>>?

    @Query("DELETE FROM add_location_dao WHERE entryType = :type AND category_id = :categoryId")
    fun deleteMarkerListByFolder(categoryId: String, type: String)


    @Query("SELECT EXISTS(SELECT 1 FROM add_location_dao WHERE id = :id AND entryType = :type)")
     fun isMarkerOfTypeExists(id: Int, type: String): Boolean

    @Query("SELECT * FROM add_location_dao")
    fun getAllLocations(): List<LocationDetail>



    @Query("DELETE FROM add_location_dao")
    suspend fun clearUserDB()




    @Query("SELECT * FROM add_location_dao WHERE id = :id")
    fun getSingleRecord(id: Int):LocationDetail


    @Query("DELETE FROM add_location_dao WHERE id = :id")
    fun deleteSingleRecord(id: Int)


    @Query("UPDATE add_location_dao SET currentStatus = :isCurrent WHERE id = :locationId")
    fun updateCurrentStatus(locationId: Int, isCurrent: Boolean)


    @Query("UPDATE add_location_dao SET sendNotification = 1 WHERE entryType = 'Exit'")
    suspend fun enableNotificationsForExitType()

    @Delete
    suspend fun deleteLocation(location: LocationDetail)


}