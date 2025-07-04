package com.locationReminder.reponseModel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "imported_category_name_dao")
data class ImportedCategoryNameResponseModel (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @SerializedName("category_name" ) var categoryName : String? = null,
    @SerializedName("user_id"       ) var userId       : String? = null,
    @SerializedName("first_Time_Import") var firstTimeImport       : Boolean? = null,
    @SerializedName("showImport") var showImport       : Boolean? = null,
    @SerializedName("userName") var userName       : String? = null


)
