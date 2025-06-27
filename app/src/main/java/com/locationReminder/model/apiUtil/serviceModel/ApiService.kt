package com.locationReminder.model.apiUtil.serviceModel


import com.locationReminder.reponseModel.CategoryFolderResponseModel
import com.locationReminder.reponseModel.LocationDetail
import com.locationReminder.viewModel.UserDetailResponseModel
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {



    @POST("rpc/register_user")
    @JvmSuppressWildcards
    suspend fun callSignUp(@Body params: Map<String, Any>): Response<List<UserDetailResponseModel>>



    @POST("rpc/login_user")
    @JvmSuppressWildcards
    suspend fun callLogin(@Body params: Map<String, Any>): Response<List<UserDetailResponseModel>>

    //Marker list ADD  GET DELETE
    @POST("marker_list")
    @JvmSuppressWildcards
    suspend fun addMarkerList(@Body params: Map<String, Any>): Response<List<LocationDetail>>

    @GET("marker_list?select=*")
    @JvmSuppressWildcards
    suspend fun getMarkerList(@Query("category_id") categoryId: String, @Query("user_id") userId: String): Response<List<LocationDetail>>

    @GET("marker_list?select=*")
    @JvmSuppressWildcards
    suspend fun getImportedMarkerList(@Query("category_title") categoryTitle: String, @Query("user_id") userId: String): Response<List<LocationDetail>>


    @DELETE("marker_list")
    suspend fun deleteMarker(@Query("id") filter: String): Response<ResponseBody>

    @DELETE("marker_list")
    suspend fun deleteAllMarker(@Query("category_id") categoryId: String, @Query("user_id") userId: String): Response<ResponseBody>


    @PATCH("marker_list")
    suspend fun editMarker(
        @Query("id") filter: String,
        @Body params: Map<String, @JvmSuppressWildcards Any>
    ): Response<ResponseBody>


    @PATCH("marker_list")
    suspend fun updateMarkerStatus(
        @Query("id") filter: String,
        @Body params: Map<String, @JvmSuppressWildcards Any>
    ): Response<ResponseBody>


    //CategoryList ADD GET EDIT DELETE

    @POST("category_list")
    @JvmSuppressWildcards
    suspend fun addCategoryList(@Body params: Map<String, Any>): Response<List<CategoryFolderResponseModel>>

    @GET("category_list")
    @JvmSuppressWildcards
    suspend fun getCategoryList(@Query("user_id") filter: String): Response<List<CategoryFolderResponseModel>>

    @DELETE("category_list")
    suspend fun deleteCategory(@Query("id") filter: String): Response<ResponseBody>

    @PATCH("category_list")
    suspend fun editCategory(
        @Query("id") filter: String,
        @Body params: Map<String, @JvmSuppressWildcards Any>
    ): Response<ResponseBody>

}