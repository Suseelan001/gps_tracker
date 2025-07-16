package com.locationReminder.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.locationReminder.model.apiUtil.serviceModel.BaseNetworkSyncClass
import com.locationReminder.model.apiUtil.utils.NetworkResult
import com.locationReminder.reponseModel.CategoryFolderResponseModel
import com.locationReminder.reponseModel.SuggestionsCategoryListResponseModel
import com.locationReminder.reponseModel.SuggestionsList
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class SuggestionsViewModel @Inject constructor(
    private val baseNetworkCall: BaseNetworkSyncClass
) : ViewModel() {



    private val _suggestionsCategoryList = MutableLiveData<List<SuggestionsCategoryListResponseModel>>()
    val suggestionsCategoryList: LiveData<List<SuggestionsCategoryListResponseModel>> get() = _suggestionsCategoryList


    private val _suggestionsList = MutableLiveData<List<SuggestionsList>>()
    val suggestionsList: LiveData<List<SuggestionsList>> get() = _suggestionsList

    private val _categoryFolderResponse = MutableLiveData<CategoryFolderResponseModel?>()
    val categoryFolderResponse: LiveData<CategoryFolderResponseModel?> get() = _categoryFolderResponse

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _successMessage = MutableLiveData<String?>()
    val successMessage: LiveData<String?> get() = _successMessage

     val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading



    fun getSuggestionsCategoryList(areaId: String,searchKey: String) = viewModelScope.launch {
        _loading.postValue(true)
        when (val result = baseNetworkCall.getSuggestionsCategoryList("eq.$areaId",searchKey)) {
            is NetworkResult.Success -> {
                val suggestionsList = result.data
                if (suggestionsList?.isNotEmpty() == true) {
                    _suggestionsCategoryList.value = suggestionsList
                    _loading.postValue(false)
                }else {
                    _suggestionsCategoryList.value = emptyList()
                    _loading.postValue(false)
                }
            }

            is NetworkResult.Error -> {
                _loading.postValue(false)
            }

            is NetworkResult.Loading -> {
                _loading.postValue(true)
            }
        }
    }


    fun getAreaId(areaName: String,searchKey: String) = viewModelScope.launch {
        _loading.postValue(true)
        when (val result = baseNetworkCall.getAreaId(areaName)) {
            is NetworkResult.Success -> {
                val areaList = result.data
                if (areaList?.isNotEmpty() == true) {
                   getSuggestionsCategoryList(areaList[0].areaId.toString(),searchKey)
                }else {
                    _suggestionsCategoryList.value = emptyList()
                    _loading.postValue(false)
                }
            }

            is NetworkResult.Error -> {
                _loading.postValue(false)
            }

            is NetworkResult.Loading -> {
                _loading.postValue(true)
            }
        }
    }

    fun getSuggestionsList(categoryId: String) = viewModelScope.launch {
        _loading.postValue(true)
        when (val result = baseNetworkCall.getSuggestionsList(categoryId)) {
            is NetworkResult.Success -> {
                val suggestionsList = result.data
                if (suggestionsList?.isNotEmpty() == true) {
                    _suggestionsList.value = suggestionsList
                    _loading.postValue(false)
                }else {
                    _suggestionsList.value = emptyList()
                    _loading.postValue(false)
                }
            }

            is NetworkResult.Error -> {
                _loading.postValue(false)
            }

            is NetworkResult.Loading -> {
                _loading.postValue(true)
            }
        }
    }



    fun getSuggestionsRecord(recordId: String) = viewModelScope.launch {
        _loading.postValue(true)
        when (val result = baseNetworkCall.getSuggestionsRecord(recordId)) {
            is NetworkResult.Success -> {
                val suggestionsList = result.data
                if (suggestionsList?.isNotEmpty() == true) {
                    _suggestionsList.value = suggestionsList
                    _loading.postValue(false)
                }else {
                    _suggestionsList.value = emptyList()
                    _loading.postValue(false)
                }
            }

            is NetworkResult.Error -> {
                _loading.postValue(false)
            }

            is NetworkResult.Loading -> {
                _loading.postValue(true)
            }
        }
    }





    fun addCategoryList(categoryName: String,userId: String,userName: String,areaName: String="") = viewModelScope.launch {
        val params = mutableMapOf<String, Any>()
        params["category_name"] = categoryName
        params["user_id"] = userId
        params["userName"] = userName
        params["area_name"] = areaName
        _loading.postValue(true)
        when (val result = baseNetworkCall.addCategoryList(params)) {

            is NetworkResult.Success -> {
                val data = result.data
                if (data != null && data.isNotEmpty()) {
                    _categoryFolderResponse.value = data.first()
                }

            }

            is NetworkResult.Error -> {
                val backendMessage = result.message ?: "Unknown error"
                val errorMessage = if (backendMessage.contains("duplicate key value", ignoreCase = true)) {
                    "Record already exists"
                } else {
                    backendMessage
                }

                _errorMessage.postValue(errorMessage)
                _loading.postValue(false)

            }

            is NetworkResult.Loading -> {
                _loading.postValue(true)
            }
        }
    }



    fun clearSuccessMessage() { _successMessage.value = null }


}
