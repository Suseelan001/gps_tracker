package com.locationReminder.reponseModel

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "settings_data_dao")
data class SettingsData(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
    val unit: String ,
    val locationUpdateInterval: String ,
    val entryRadius: String ,
    val exitRadius: String,
    val maximumRadius: String
)
