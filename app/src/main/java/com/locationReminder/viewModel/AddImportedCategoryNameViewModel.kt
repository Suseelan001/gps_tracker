package com.locationReminder.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.locationReminder.reponseModel.ImportedCategoryNameResponseModel
import com.locationReminder.roomDatabase.repository.AddImportedCategoryDatabaseRepository
import com.locationReminder.roomDatabase.repository.AddLocationDatabaseRepository
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class AddImportedCategoryNameViewModel @Inject constructor(
    private val addImportedCategoryDatabaseRepository: AddImportedCategoryDatabaseRepository,
    private val addLocationDatabaseRepository: AddLocationDatabaseRepository,

    ) : ViewModel() {



    fun getAllRecord(): LiveData<List<ImportedCategoryNameResponseModel>> {
        return addImportedCategoryDatabaseRepository.getAllRecord()
    }

    fun deleteMarkerListByFolder(categoryId: String,type: String) {
        viewModelScope.launch {
            addLocationDatabaseRepository.deleteMarkerListByFolder(categoryId,type)
        }
    }

    fun getRecordById(id: Int): ImportedCategoryNameResponseModel {
        return addImportedCategoryDatabaseRepository.getRecordById(id)
    }


    suspend fun isCategoryAlreadyExists(categoryName: String, userId: String): ImportedCategoryNameResponseModel? {
        return withContext(Dispatchers.IO) {
            addImportedCategoryDatabaseRepository.doesCategoryExist(categoryName, userId)
        }
    }


     fun updateRecordStatus(id: Int, status: Boolean) {
        viewModelScope.launch {
            addImportedCategoryDatabaseRepository.updateRecordStatus(id, status)
        }
    }

    fun insertRecord(record: ImportedCategoryNameResponseModel) {
        viewModelScope.launch {
            addImportedCategoryDatabaseRepository.insertRecord(record)
        }
    }


    fun deleteSingleRecord(recordId: Int) {
        viewModelScope.launch {
            addImportedCategoryDatabaseRepository.deleteSingleRecord(recordId)
        }
    }

    fun updateRecord(record: ImportedCategoryNameResponseModel) {
        viewModelScope.launch {
            addImportedCategoryDatabaseRepository.updateRecord(record)
        }
    }


}
