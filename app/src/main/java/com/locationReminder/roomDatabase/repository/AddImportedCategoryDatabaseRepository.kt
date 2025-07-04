package com.locationReminder.roomDatabase.repository




import androidx.lifecycle.LiveData
import com.locationReminder.reponseModel.ImportedCategoryNameResponseModel
import com.locationReminder.reponseModel.LocationDetail
import com.locationReminder.roomDatabase.dao.ImportedCategoryNameDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class AddImportedCategoryDatabaseRepository @Inject constructor(
    private val folderNameDAO: ImportedCategoryNameDAO
) {

    suspend fun insertRecord(task: ImportedCategoryNameResponseModel) {
        withContext(Dispatchers.IO) {
            folderNameDAO.insertRecord(task)
        }
    }

    suspend fun updateRecordStatus(id: Int, status: Boolean) {
        folderNameDAO.updateRecordStatus(id, status)
    }
    suspend fun updateShowImportStatus(id: Int, status: Boolean) {
        folderNameDAO.updateShowImportStatus(id, status)
    }


    fun deleteSingleRecord(id: Int){
        folderNameDAO.deleteSingleRecord(id)
    }


    suspend fun clearUserDB() {
        withContext(Dispatchers.IO) {
            folderNameDAO.clearUserDB()
        }
    }



    fun getAllRecord(): LiveData<List<ImportedCategoryNameResponseModel>> {
        return folderNameDAO.getAllRecord( )
    }

     fun getRecordById(id: Int): ImportedCategoryNameResponseModel {
        return folderNameDAO.getRecordById(id)
    }




    suspend fun doesCategoryExist(categoryName: String, userId: String): ImportedCategoryNameResponseModel? {
        return folderNameDAO.isCategoryExists(categoryName, userId)
    }


    suspend fun deleteLocation(location: ImportedCategoryNameResponseModel) {
        folderNameDAO.deleteRecord(location)
    }
    suspend fun updateRecord(record: ImportedCategoryNameResponseModel) {
        folderNameDAO.updateRecord(record)
    }


}
