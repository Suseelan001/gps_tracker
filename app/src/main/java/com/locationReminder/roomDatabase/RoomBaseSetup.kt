package com.locationReminder.roomDatabase


import androidx.room.Database
import androidx.room.RoomDatabase
import com.locationReminder.reponseModel.CategoryFolderResponseModel
import com.locationReminder.reponseModel.ContactDetail
import com.locationReminder.reponseModel.ImportedCategoryNameResponseModel
import com.locationReminder.reponseModel.LocationDetail
import com.locationReminder.reponseModel.SettingsData
import com.locationReminder.roomDatabase.dao.ContactDAO
import com.locationReminder.roomDatabase.dao.FolderNameDAO
import com.locationReminder.roomDatabase.dao.ImportedCategoryNameDAO
import com.locationReminder.roomDatabase.dao.LocationDAO
import com.locationReminder.roomDatabase.dao.SettingsDAO
import com.locationReminder.roomDatabase.dao.UserDao
import com.locationReminder.viewModel.UserDetailResponseModel

@Database(
    version = 41,
    exportSchema = false,
    entities = [
        LocationDetail::class,
        ContactDetail::class,
        SettingsData::class,
        UserDetailResponseModel::class,
        CategoryFolderResponseModel::class,
        ImportedCategoryNameResponseModel::class
    ]
)

abstract class RoomBaseSetup : RoomDatabase() {
      abstract fun locationDAO(): LocationDAO
      abstract fun contactDAO(): ContactDAO
      abstract fun settingsDAO(): SettingsDAO
      abstract fun userDao(): UserDao
      abstract fun folderNameDAO(): FolderNameDAO
      abstract fun ImportedCategoryNameDAO(): ImportedCategoryNameDAO
}