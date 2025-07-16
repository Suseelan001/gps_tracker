package com.locationReminder.roomDatabase.repository




import androidx.lifecycle.LiveData
import com.locationReminder.reponseModel.CategoryFolderResponseModel
import com.locationReminder.roomDatabase.dao.FolderNameDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class AddFolderDatabaseRepository @Inject constructor(
    private val folderNameDAO: FolderNameDAO
) {

    suspend fun insertRecord(task: CategoryFolderResponseModel) {
        withContext(Dispatchers.IO) {
            folderNameDAO.insertRecord(task)
        }
    }




    suspend fun clearUserDB() {
        withContext(Dispatchers.IO) {
            folderNameDAO.clearUserDB()
        }
    }



    fun getAllRecord(): LiveData<List<CategoryFolderResponseModel>> {
        return folderNameDAO.getAllRecord( )
    }


}
