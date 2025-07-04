package com.locationReminder.viewModel

import androidx.compose.animation.slideOut
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.locationReminder.model.apiUtil.serviceModel.BaseNetworkSyncClass
import com.locationReminder.model.apiUtil.utils.NetworkResult
import com.locationReminder.reponseModel.CategoryFolderResponseModel
import com.locationReminder.roomDatabase.repository.AddFolderDatabaseRepository
import com.locationReminder.roomDatabase.repository.AddLocationDatabaseRepository
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class AddFolderNameViewModel @Inject constructor(
    private val baseNetworkCall: BaseNetworkSyncClass,
    private val addFolderDatabaseRepository: AddFolderDatabaseRepository,
    private val addLocationDatabaseRepository: AddLocationDatabaseRepository

    ) : ViewModel() {


    private val _successMessage = MutableLiveData<String?>()
    val successMessage: LiveData<String?> get() = _successMessage

    private val _categoryFolderResponse = MutableLiveData<CategoryFolderResponseModel?>()
    val categoryFolderResponse: LiveData<CategoryFolderResponseModel?> get() = _categoryFolderResponse


    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage


    fun getCategoryFolderList(userId: String) = viewModelScope.launch {
        _loading.postValue(true)
        when (val result = baseNetworkCall.getCategoryFolderList(userId)) {
            is NetworkResult.Success -> {
                val folderNameList = result.data
                addFolderDatabaseRepository.clearUserDB()
                if (folderNameList?.isNotEmpty() == true) {
                    folderNameList.forEach { folder->
                        addFolderDatabaseRepository.insertRecord(folder)
                        _successMessage.value="Get Category Folder"

                    }
                }

                _loading.postValue(false)
            }

            is NetworkResult.Error -> {
                _loading.postValue(false)
            }

            is NetworkResult.Loading -> {
                _loading.postValue(true)
            }
        }
    }

    fun getAllRecord(): LiveData<List<CategoryFolderResponseModel>> {
        return addFolderDatabaseRepository.getAllRecord()
    }

    fun addCategoryList(categoryName: String,userId: String) = viewModelScope.launch {
        val params = mutableMapOf<String, Any>()
        params["category_name"] = categoryName
        params["user_id"] = userId
        _loading.postValue(true)
        when (val result = baseNetworkCall.addCategoryList(params)) {

            is NetworkResult.Success -> {
              //  _successMessage.value = "Folder created"
                val data = result.data
                if (data != null && data.isNotEmpty()) {
                    _categoryFolderResponse.value = data.first()
                }

                _loading.postValue(false)
            }

            is NetworkResult.Error -> {

                val backendMessage = result.message ?: "Unknown error"
                println("CHECK_TAG_errorMessage: $backendMessage")

                val userFriendlyMessage = if (backendMessage.contains("duplicate key value", ignoreCase = true)) {
                    "Record already exists"
                } else {
                    backendMessage
                }

                _errorMessage.postValue(userFriendlyMessage)
                _loading.postValue(false)

            }

            is NetworkResult.Loading -> {
                _loading.postValue(true)
            }
        }
    }

    fun deleteMarkerListByFolder(categoryId: String,type: String) {
        viewModelScope.launch {
            addLocationDatabaseRepository.deleteMarkerListByFolder(categoryId,type)
        }
    }


    fun deleteAllMarker(categoryId: String,userId: String) = viewModelScope.launch {
        _loading.postValue(true)
        when (baseNetworkCall.deleteAllMarker(categoryId,userId)) {
            is NetworkResult.Success -> {
                val itemId = categoryId.removePrefix("eq.")
                deleteMarkerListByFolder(itemId,"Marker")

                _successMessage.value="Record deleted"

                _loading.postValue(false)
            }

            is NetworkResult.Error -> {
                _loading.postValue(false)
            }

            is NetworkResult.Loading -> {
                _loading.postValue(true)
            }
        }
    }

    fun deleteCategoryList(categoryId: String,userId: String) = viewModelScope.launch {
        _loading.postValue(true)
        when (baseNetworkCall.deleteCategory(categoryId)) {
            is NetworkResult.Success -> {
                deleteAllMarker(categoryId,userId)
            }

            is NetworkResult.Error -> {
                _loading.postValue(false)
            }

            is NetworkResult.Loading -> {
                _loading.postValue(true)
            }
        }
    }

    fun editCategory(categoryId: String,categoryName: String) = viewModelScope.launch {

        val params = mutableMapOf<String, Any>()
        params["category_name"] = categoryName

        _loading.postValue(true)
        when (baseNetworkCall.editCategory(categoryId,params)) {
            is NetworkResult.Success -> {
                _successMessage.value="Record edited"
                _loading.postValue(false)
            }

            is NetworkResult.Error -> {
                _errorMessage.postValue("Record already exist")

                _loading.postValue(false)
            }

            is NetworkResult.Loading -> {
                _loading.postValue(true)
            }
        }
    }

    fun clearSuccessMessage() { _successMessage.value = null }

    fun clearErrorMessage(){
        _errorMessage.postValue(null)

    }

}
