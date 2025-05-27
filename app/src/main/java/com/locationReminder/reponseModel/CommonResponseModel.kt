package com.locationReminder.reponseModel


import com.google.gson.annotations.SerializedName

data class CommonResponseModel(
    @SerializedName("error"   ) var error   : String? = null,
    @SerializedName("message" ) var message : String? = null
)