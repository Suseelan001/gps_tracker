package com.locationReminder.reponseModel

import com.google.gson.annotations.SerializedName


data class AreaList (

    @SerializedName("area_Id"   ) var areaId   : String? = null,
    @SerializedName("area_Name" ) var areaName : String? = null

)