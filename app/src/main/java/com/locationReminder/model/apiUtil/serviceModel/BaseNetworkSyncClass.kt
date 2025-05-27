package com.locationReminder.model.apiUtil.serviceModel

import com.locationReminder.model.apiUtil.utils.BaseApiResponse
import com.locationReminder.model.apiUtil.utils.NetworkResult
import com.locationReminder.reponseModel.LocationDetail
import com.locationReminder.viewModel.UserDetailResponseModel
import kotlinx.coroutines.CoroutineDispatcher
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

    suspend fun addMarkerList(map: Map<String, Any>): NetworkResult<List<LocationDetail>> {
        return withContext(Dispatchers.Default) {
            safeApiCall {
                apiService.addMarkerList(map)
            }
        }
    }


    suspend fun getMarkerList(): NetworkResult<List<LocationDetail>> {
        return withContext(Dispatchers.Default) {
            safeApiCall {
                apiService.getMarkerList()
            }
        }
    }

}