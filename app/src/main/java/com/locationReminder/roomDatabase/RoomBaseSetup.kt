package com.locationReminder.roomDatabase


import androidx.room.Database
import androidx.room.RoomDatabase
import com.locationReminder.reponseModel.ContactDetail
import com.locationReminder.reponseModel.LocationDetail
import com.locationReminder.reponseModel.SettingsData
import com.locationReminder.roomDatabase.dao.ContactDAO
import com.locationReminder.roomDatabase.dao.LocationDAO
import com.locationReminder.roomDatabase.dao.SettingsDAO
import com.locationReminder.roomDatabase.dao.UserDao
import com.locationReminder.viewModel.UserDetailResponseModel

@Database(
    version = 13,
    exportSchema = false,
    entities = [
        LocationDetail::class,
        ContactDetail::class,
        SettingsData::class,
        UserDetailResponseModel::class

    ]
)

abstract class RoomBaseSetup : RoomDatabase() {
      abstract fun locationDAO(): LocationDAO
      abstract fun contactDAO(): ContactDAO
      abstract fun settingsDAO(): SettingsDAO
      abstract fun userDao(): UserDao
}