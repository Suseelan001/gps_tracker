package com.locationReminder.roomDatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.locationReminder.reponseModel.SettingsData


@Dao
interface SettingsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(task: SettingsData)



    @Query("DELETE FROM settings_data_dao")
    suspend fun clearUserDB()


    @Query("UPDATE settings_data_dao SET unit = :unit ")
    fun updateUnit(unit: String)



    @Query("UPDATE settings_data_dao SET locationUpdateInterval = :locationUpdateInterval")
    fun updateLocationUpdateInterval(locationUpdateInterval: String)



    @Query("UPDATE settings_data_dao SET entryRadius = :entryRadius ")
    fun updateEntryRadius(entryRadius: String)

    @Query("SELECT * FROM settings_data_dao LIMIT 1")
    fun getAllRecord(): LiveData<SettingsData>



    @Query("UPDATE settings_data_dao SET exitRadius = :exitRadius ")
    fun updateExitRadius(exitRadius: String)



    @Query("UPDATE settings_data_dao SET maximumRadius = :maximumRadius ")
    fun updateMaximumRadius(maximumRadius: String)






}