package com.locationReminder.reponseModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact_detail_dao")
data class ContactDetail(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val mobileNumber: String
)
