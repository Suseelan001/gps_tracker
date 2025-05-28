package com.locationReminder.model.apiUtil.serviceModel


import com.locationReminder.reponseModel.LocationDetail
import com.locationReminder.viewModel.UserDetailResponseModel
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {



    @POST("rpc/register_user")
    @JvmSuppressWildcards
    suspend fun callSignUp(@Body params: Map<String, Any>): Response<List<UserDetailResponseModel>>



    @POST("rpc/login_user")
    @JvmSuppressWildcards
    suspend fun callLogin(@Body params: Map<String, Any>): Response<List<UserDetailResponseModel>>


    @POST("marker_list")
    @JvmSuppressWildcards
    suspend fun addMarkerList(@Body params: Map<String, Any>): Response<List<LocationDetail>>


    @GET("marker_list")
    @JvmSuppressWildcards
    suspend fun getMarkerList(): Response<List<LocationDetail>>


    @DELETE("marker_list")
    suspend fun deleteMarker(@Query("id") filter: String): Response<ResponseBody>

}