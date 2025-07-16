package com.locationReminder.roomDatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.locationReminder.reponseModel.ImportedCategoryNameResponseModel


@Dao
interface ImportedCategoryNameDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(task: ImportedCategoryNameResponseModel)



    @Query("SELECT * FROM imported_category_name_dao")
    fun getAllRecord(): LiveData<List<ImportedCategoryNameResponseModel>>



    @Query("SELECT * FROM imported_category_name_dao WHERE id = :id LIMIT 1")
     fun getRecordById(id: Int): ImportedCategoryNameResponseModel



    @Query("DELETE FROM imported_category_name_dao")
    suspend fun clearUserDB()


    @Delete
    suspend fun deleteRecord(location: ImportedCategoryNameResponseModel)

    @Query("SELECT * FROM imported_category_name_dao WHERE categoryName = :categoryName AND userId = :userId LIMIT 1")
    suspend fun isCategoryExists(categoryName: String, userId: String): ImportedCategoryNameResponseModel?


    @Query("DELETE FROM imported_category_name_dao WHERE id = :id")
    fun deleteSingleRecord(id: Int)

    @Update
    suspend fun updateRecord(record: ImportedCategoryNameResponseModel)

    @Query("UPDATE imported_category_name_dao SET firstTimeImport = :status WHERE id = :id")
    suspend fun updateRecordStatus(id: Int, status: Boolean)

    @Query("UPDATE imported_category_name_dao SET showImport = :status WHERE id = :id")
    suspend fun updateShowImportStatus(id: Int, status: Boolean)


}