package com.locationReminder.reponseModel

import com.google.gson.annotations.SerializedName

data class SuggestionsCategoryListResponseModel (

    @SerializedName("category_name" ) var categoryName : String? = null,
    @SerializedName("id"            ) var id           : String? = null,
    @SerializedName("area_Name"            ) var areaName           : String? = null,
    @SerializedName("area_Id"            ) var areaId           : String? = null

)