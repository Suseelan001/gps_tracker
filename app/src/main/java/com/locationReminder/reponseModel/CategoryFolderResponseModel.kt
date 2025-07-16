package com.locationReminder.reponseModel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "folder_name_dao")
data class CategoryFolderResponseModel (
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id"            ) var id           : Int?    = null,
    @SerializedName("category_name" ) var categoryName : String? = null,
    @SerializedName("user_id"       ) var userId       : String? = null,
    @SerializedName("userName"       ) var userName       : String? = null,
    @SerializedName("area_name"       ) var areaName       : String? = null,
    @SerializedName("current_status"       ) var currentStatus       : Boolean? = null

)
