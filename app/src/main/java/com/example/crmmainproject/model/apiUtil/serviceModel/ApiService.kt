package com.example.crmmainproject.model.apiUtil.serviceModel


import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("settings/info")
    @JvmSuppressWildcards
    suspend fun settingsInfo(@Body params: Map<String, Any>): Response<ResponseBody>


}