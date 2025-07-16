package com.locationReminder.reponseModel

import com.google.gson.annotations.SerializedName


data class SuggestionsList (

    @SerializedName("id"            ) var id           : Int?    = null,
    @SerializedName("title"         ) var title        : String? = null,
    @SerializedName("address"       ) var address      : String? = null,
    @SerializedName("category_id"   ) var categoryId   : String? = null,
    @SerializedName("lat"           ) var lat          : Double? = null,
    @SerializedName("lng"           ) var lng          : Double? = null,
    @SerializedName("category_name" ) var categoryName : String? = null,
    @SerializedName("area_name" ) var areaName : String? = null

)