package com.locationReminder.reponseModel

data class FCMDataModel(
    val title: String,
    val body: String,
    val address: String? = null,
    val from_user_id: String? = null,
    val emergency_numbers: List<String>? = null
)
