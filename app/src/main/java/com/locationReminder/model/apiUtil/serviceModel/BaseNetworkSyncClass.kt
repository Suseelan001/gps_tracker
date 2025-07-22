package com.locationReminder.model.apiUtil.serviceModel

import com.locationReminder.model.apiUtil.utils.BaseApiResponse
import com.locationReminder.model.apiUtil.utils.NetworkResult
import com.locationReminder.reponseModel.AreaList
import com.locationReminder.reponseModel.CategoryFolderResponseModel
import com.locationReminder.reponseModel.LocationDetail
import com.locationReminder.reponseModel.MarkerUpdateRequest
import com.locationReminder.reponseModel.SuggestionsCategoryListResponseModel
import com.locationReminder.reponseModel.SuggestionsList
import com.locationReminder.viewModel.UserDetailResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class BaseNetworkSyncClass @Inject constructor(
    @Named("LOCATION_REMINDER_API_SERVICE") private val apiService: ApiService
) : BaseApiResponse(){



    suspend fun callSignUp(map: Map<String, Any>): NetworkResult<List<UserDetailResponseModel>> {
        return withContext(Dispatchers.Default) {
            safeApiCall {
                apiService.callSignUp(map)
            }
        }
    }

    suspend fun callLogin(map: Map<String, Any>): NetworkResult<List<UserDetailResponseModel>> {
        return withContext(Dispatchers.Default) {
            safeApiCall {
                apiService.callLogin(map)
            }
        }
    }


    suspend fun callLogout(id: String,map: Map<String, Any>): NetworkResult<List<UserDetailResponseModel>> {
        return withContext(Dispatchers.Default) {
            safeApiCall {
                apiService.callLogout(id,map)
            }
        }
    }
    suspend fun updateUserLogin(mail: String,map: Map<String, Any>): NetworkResult<ResponseBody> {
        return withContext(Dispatchers.Default) {
            safeApiCall {
                apiService.updateUserLogin(mail,map)
            }
        }
    }

    suspend fun addMarkerList(map: Map<String, Any>): NetworkResult<List<LocationDetail>> {
        return withContext(Dispatchers.Default) {
            safeApiCall {
                apiService.addMarkerList(map)
            }
        }
    }


    suspend fun getMarkerList(categoryId: String,userId: String): NetworkResult<List<LocationDetail>> {
        return withContext(Dispatchers.Default) {
            safeApiCall {
                apiService.getMarkerList(categoryId,userId)
            }
        }
    }
    suspend fun getSuggestionsCategoryList(areaId: String,searchKey: String): NetworkResult<List<SuggestionsCategoryListResponseModel>> {
        return withContext(Dispatchers.Default) {
            safeApiCall {
                apiService.getSuggestionsCategoryList(areaId,searchKey)
            }
        }
    }
    suspend fun getAreaId(areaName: String): NetworkResult<List<AreaList>> {
        return withContext(Dispatchers.Default) {
            safeApiCall {
                apiService.getAreaId(areaName)
            }
        }
    }
    suspend fun getSuggestionsList(categoryId: String): NetworkResult<List<SuggestionsList>> {
        return withContext(Dispatchers.Default) {
            safeApiCall {
                apiService.getSuggestionsList(categoryId)
            }
        }
    }

    suspend fun getSuggestionsRecord(recordId: String): NetworkResult<List<SuggestionsList>> {
        return withContext(Dispatchers.Default) {
            safeApiCall {
                apiService.getSuggestionsRecord(recordId)
            }
        }
    }

    suspend fun getImportedMarkerList(categoryId: String,userId: String): NetworkResult<List<LocationDetail>> {
        return withContext(Dispatchers.Default) {
            safeApiCall {
                apiService.getImportedMarkerList(categoryId,userId)
            }
        }
    }

    suspend fun updateMarkers(updatedList: List<MarkerUpdateRequest>): NetworkResult<List<LocationDetail>> {
        return withContext(Dispatchers.Default) {
            safeApiCall {
                apiService.updateMarkers(updatedList)
            }
        }
    }



    suspend fun getCategoryFolderList(userId: String): NetworkResult<List<CategoryFolderResponseModel>> {
        return withContext(Dispatchers.Default) {
            safeApiCall {
                apiService.getCategoryList(userId)
            }
        }
    }


    suspend fun addCategoryList(map: Map<String, Any>): NetworkResult<List<CategoryFolderResponseModel>> {
        return withContext(Dispatchers.Default) {
            safeApiCall {
                apiService.addCategoryList(map)
            }
        }
    }

    suspend fun updateCategoryListStatus(categoryId: String, map: Map<String, Any>): NetworkResult<List<CategoryFolderResponseModel>> {
        return withContext(Dispatchers.Default) {
            safeApiCall {
                apiService.updateCategoryListStatus(categoryId,map)
            }
        }
    }

    suspend fun deleteMarkerList(id: String): NetworkResult<ResponseBody> {
        return withContext(Dispatchers.Default) {
            safeApiCall {
                apiService.deleteMarkerList(id)
            }
        }
    }
    suspend fun deleteCategory(id: String): NetworkResult<ResponseBody> {
        return withContext(Dispatchers.Default) {
            safeApiCall {
                apiService.deleteCategory(id)
            }
        }
    }
    suspend fun editCategory(id: String,map: Map<String, Any>): NetworkResult<ResponseBody> {
        return withContext(Dispatchers.Default) {
            safeApiCall {
                apiService.editCategory(id,map)
            }
        }
    }
    suspend fun editMarker(id: String,map: Map<String, Any>): NetworkResult<ResponseBody> {
        return withContext(Dispatchers.Default) {
            safeApiCall {
                apiService.editMarker(id,map)
            }
        }
    }
    suspend fun updateMarkerStatus(id: String,map: Map<String, Any>): NetworkResult<ResponseBody> {
        return withContext(Dispatchers.Default) {
            safeApiCall {
                apiService.updateMarkerStatus(id,map)
            }
        }
    }
    suspend fun updateAllMarkerStatus(categoryId: String, userId: String, map: Map<String, Any>): NetworkResult<ResponseBody> {
        return withContext(Dispatchers.Default) {
            safeApiCall {
                apiService.updateAllMarkerStatus(categoryId,userId,map)
            }
        }
    }
    suspend fun deleteAllMarker(categoryId: String,userId: String): NetworkResult<ResponseBody> {
        return withContext(Dispatchers.Default) {
            safeApiCall {
                apiService.deleteAllMarker(categoryId,userId)
            }
        }
    }

    suspend fun updatePassword(email: String, mobile: String,map: Map<String, Any>):NetworkResult<List<UserDetailResponseModel>> {
        return withContext(Dispatchers.Default) {
            safeApiCall {
                apiService.updatePassword(email,mobile,map)
            }
        }
    }

}