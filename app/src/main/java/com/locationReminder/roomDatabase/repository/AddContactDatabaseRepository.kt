package com.locationReminder.roomDatabase.repository




import androidx.lifecycle.LiveData
import com.locationReminder.reponseModel.ContactDetail
import com.locationReminder.roomDatabase.dao.ContactDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class AddContactDatabaseRepository @Inject constructor(
    private val contactDAO: ContactDAO
) {

    suspend fun insertRecord(task: ContactDetail) {
        withContext(Dispatchers.IO) {
            contactDAO.insertRecord(task)
        }
    }




    suspend fun clearUserDB() {
        withContext(Dispatchers.IO) {
            contactDAO.clearUserDB()
        }
    }



    fun getAllRecord(): LiveData<List<ContactDetail>> {
        return contactDAO.getAllRecord( )
    }

    suspend fun deleteLocation(location: ContactDetail) {
        contactDAO.deleteRecord(location)
    }


}
