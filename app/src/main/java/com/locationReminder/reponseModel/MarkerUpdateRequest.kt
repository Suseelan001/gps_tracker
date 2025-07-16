package com.locationReminder.reponseModel


data class MarkerUpdateRequest(

    val id: String,
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
    var category_id : String? = null,
    var user_id : String? = null,

)