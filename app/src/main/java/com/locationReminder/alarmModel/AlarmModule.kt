package com.locationReminder.alarmModel

import com.locationReminder.roomDatabase.dao.LocationDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.Lazy


@Module
@InstallIn(SingletonComponent::class)
object AlarmModule {

    @Provides
    @Singleton
    fun provideAlarmAlertWindow(
        locationDAO: LocationDAO,
        alarmHelper: Lazy<AlarmHelper>
    ): AlarmAlertWindow {
        return AlarmAlertWindow(locationDAO, alarmHelper)
    }
}
