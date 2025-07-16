package com.locationReminder.reponseModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "add_location_dao")
data class LocationDetail(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val address: String,
    val entryType: String,
    val currentStatus: Boolean,
    val radius: Float,
    val lat: Double,
    val lng: Double,
    val sendNotification: Boolean=false,
    val vibration: Boolean=true,
    var category_name : String? = null,
    var category_id : String? = null



)
