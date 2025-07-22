package com.locationReminder.reponseModel

data class NotifyEmergencyRequest(
    val emergency_numbers: List<String>,
    val address: String,
    val from_user_id: String
)
