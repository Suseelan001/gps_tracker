package com.locationReminder.viewModel


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user")
data class UserDetailResponseModel (
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id"           ) var id           : Int?    = null,
    @SerializedName("user_mail"    ) var userMail     : String? = null,
    @SerializedName("username"     ) var username     : String? = null,
    @SerializedName("mobilenumber" ) var mobilenumber : String? = null

)



