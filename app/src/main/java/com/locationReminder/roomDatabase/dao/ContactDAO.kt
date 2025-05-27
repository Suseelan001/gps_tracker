package com.locationReminder.roomDatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.locationReminder.reponseModel.ContactDetail


@Dao
interface ContactDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(task: ContactDetail)



    @Query("SELECT * FROM contact_detail_dao")
    fun getAllRecord(): LiveData<List<ContactDetail>>

    @Query("SELECT * FROM contact_detail_dao")
    suspend fun getAllContacts(): List<ContactDetail>


    @Query("DELETE FROM contact_detail_dao")
    suspend fun clearUserDB()

    @Delete
    suspend fun deleteRecord(location: ContactDetail)


}