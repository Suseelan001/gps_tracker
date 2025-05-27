package com.locationReminder.model.apiUtil.serviceModel



import com.locationReminder.model.localStorage.MySharedPreference
import com.locationReminder.roomDatabase.dao.ContactDAO
import com.locationReminder.roomDatabase.dao.LocationDAO
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface LocationDaoEntryPoint {
    fun locationDao(): LocationDAO
    fun contactDAO(): ContactDAO
    fun mySharedPreference(): MySharedPreference

}



