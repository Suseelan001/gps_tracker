package com.example.crmmainproject.model.apiUtil.serviceModel

import com.example.crmmainproject.model.apiUtil.utils.BaseApiResponse
import com.example.crmmainproject.model.apiUtil.utils.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class BaseNetworkSyncClass @Inject constructor(
    @Named("CRM_API_SERVICE") private val apiService: ApiService,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : BaseApiResponse(){

    suspend fun callSettingsInfo(map: Map<String, Any>): NetworkResult<ResponseBody> {
        return withContext(defaultDispatcher) {
            safeApiCall {
                apiService.settingsInfo(map)
            }
        }
    }



}