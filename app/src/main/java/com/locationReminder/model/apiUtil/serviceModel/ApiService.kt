package com.locationReminder.model.apiUtil.serviceModel


import com.locationReminder.reponseModel.AreaList
import com.locationReminder.reponseModel.CategoryFolderResponseModel
import com.locationReminder.reponseModel.LocationDetail
import com.locationReminder.reponseModel.MarkerUpdateRequest
import com.locationReminder.reponseModel.NotifyEmergencyRequest
import com.locationReminder.reponseModel.SuggestionsCategoryListResponseModel
import com.locationReminder.reponseModel.SuggestionsList
import com.locationReminder.viewModel.UserDetailResponseModel
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {


    @POST("rest/v1/rpc/register_user")
    @JvmSuppressWildcards
    suspend fun callSignUp(@Body params: Map<String, Any>): Response<List<UserDetailResponseModel>>


    @POST("rest/v1/rpc/login_user")
    @JvmSuppressWildcards
    suspend fun callLogin(@Body params: Map<String, Any>): Response<List<UserDetailResponseModel>>


    @PATCH("rest/v1/user")
    @JvmSuppressWildcards
    suspend fun callLogout(@Query("id")  id: String, @Body params: Map<String, Any>): Response<List<UserDetailResponseModel>>


    @PATCH("rest/v1/user")
    @JvmSuppressWildcards
    suspend fun updateUserLogin(@Query("user_mail") mail: String, @Body params: Map<String, Any>): Response<ResponseBody>


    @PATCH("rest/v1/user")
    @Headers("Prefer: return=representation")
    @JvmSuppressWildcards
    suspend fun updatePassword(@Query("user_mail") mail: String, @Query("mobilenumber") mobile: String, @Body params: Map<String, Any>): Response<List<UserDetailResponseModel>>


    //Marker list ADD  GET DELETE

    @POST("rest/v1/marker_list")
    @JvmSuppressWildcards
    suspend fun addMarkerList(@Body params: Map<String, Any>): Response<List<LocationDetail>>

    @GET("rest/v1/marker_list?select=*")
    @JvmSuppressWildcards
    suspend fun getMarkerList(@Query("category_id") categoryId: String, @Query("user_id") userId: String): Response<List<LocationDetail>>


    @GET("rest/v1/marker_list?select=*")
    @JvmSuppressWildcards
    suspend fun getImportedMarkerList(@Query("category_id") categoryTitle: String, @Query("user_id") userId: String): Response<List<LocationDetail>>


/*
    @POST("marker_list?")
    @Headers("Prefer: return=representation")
    @JvmSuppressWildcards
    suspend fun updateMarkers(@Body markers: List<MarkerUpdateRequest>): Response<List<LocationDetail>>

*/

    @POST("rest/v1/marker_list?on_conflict=id")
    @Headers("Prefer: resolution=merge-duplicates, return=representation")
    @JvmSuppressWildcards
    suspend fun updateMarkers(@Body markers: List<MarkerUpdateRequest>): Response<List<LocationDetail>>



    @DELETE("rest/v1/marker_list")
    suspend fun deleteMarkerList(@Query("id") id: String): Response<ResponseBody>

    @DELETE("rest/v1/marker_list")
    suspend fun deleteAllMarker(@Query("category_id") categoryId: String, @Query("user_id") userId: String): Response<ResponseBody>


    @PATCH("rest/v1/marker_list")
    suspend fun editMarker(@Query("id") filter: String, @Body params: Map<String, @JvmSuppressWildcards Any>): Response<ResponseBody>


    @PATCH("rest/v1/marker_list")
    suspend fun updateMarkerStatus(@Query("id") filter: String, @Body params: Map<String, @JvmSuppressWildcards Any>): Response<ResponseBody>


    @PATCH("rest/v1/marker_list?select=*")
    suspend fun updateAllMarkerStatus(@Query("category_id") categoryId: String, @Query("user_id") userId: String, @Body params: Map<String, @JvmSuppressWildcards Any>): Response<ResponseBody>


    //CategoryList ADD GET EDIT DELETE

/*    @POST("category_list")
    @Headers("Prefer: return=representation")
    @JvmSuppressWildcards
    suspend fun addCategoryList(@Body params: Map<String, Any>): Response<List<CategoryFolderResponseModel>>*/



    @POST("rest/v1/category_list?on_conflict=category_name,user_id&select=*")
    @Headers("Prefer: resolution=merge-duplicates,return=representation")
    @JvmSuppressWildcards
    suspend fun addCategoryList(@Body params: Map<String, Any>): Response<List<CategoryFolderResponseModel>>

    @PATCH("rest/v1/category_list")
    suspend fun updateCategoryListStatus(@Query("id") id: String, @Body params: Map<String, @JvmSuppressWildcards Any>): Response<List<CategoryFolderResponseModel>>


    @GET("rest/v1/category_list")
    @JvmSuppressWildcards
    suspend fun getCategoryList(@Query("user_id") filter: String): Response<List<CategoryFolderResponseModel>>

    @DELETE("rest/v1/category_list")
    suspend fun deleteCategory(@Query("id") filter: String): Response<ResponseBody>

    @PATCH("rest/v1/category_list")
    suspend fun editCategory(@Query("id") filter: String, @Body params: Map<String, @JvmSuppressWildcards Any>): Response<ResponseBody>


    //Suggestions category list

    @GET("rest/v1/suggestions_category_list?select=*")
    @JvmSuppressWildcards
    suspend fun getSuggestionsCategoryList(@Query("area_Id") areaName: String,@Query("category_name") searchKey: String): Response<List<SuggestionsCategoryListResponseModel>>

    // suggestions list

    @GET("rest/v1/suggestions_list?select=*")
    @JvmSuppressWildcards
    suspend fun getSuggestionsList(@Query("category_id") categoryId: String): Response<List<SuggestionsList>>


    @GET("rest/v1/suggestions_list?select=*")
    @JvmSuppressWildcards
    suspend fun getSuggestionsRecord(@Query("id") recordId: String): Response<List<SuggestionsList>>

    // area list

    @GET("rest/v1/area_list")
    @JvmSuppressWildcards
    suspend fun getAreaId(@Query("area_Name") areaName: String): Response<List<AreaList>>


    @POST("functions/v1/push")
    suspend fun notifyEmergencyContacts(@Body request: NotifyEmergencyRequest): Response<ResponseBody>


}