package com.locationReminder.roomDatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.locationReminder.reponseModel.CategoryFolderResponseModel


@Dao
interface FolderNameDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(task: CategoryFolderResponseModel)



    @Query("SELECT * FROM folder_name_dao")
    fun getAllRecord(): LiveData<List<CategoryFolderResponseModel>>

    @Query("SELECT * FROM folder_name_dao")
    suspend fun getAllContacts(): List<CategoryFolderResponseModel>


    @Query("DELETE FROM folder_name_dao")
    suspend fun clearUserDB()

    @Delete
    suspend fun deleteRecord(location: CategoryFolderResponseModel)


}