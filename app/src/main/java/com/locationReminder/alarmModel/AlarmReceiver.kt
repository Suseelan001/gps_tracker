package com.locationReminder.alarmModel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.locationReminder.model.apiUtil.serviceModel.LocationDaoEntryPoint
import dagger.hilt.android.EntryPointAccessors

class AlarmReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {

        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            LocationDaoEntryPoint::class.java
        )
        val alarmHelper = entryPoint.alarmHelper()


        when (intent.action) {
            "ALARM_SNOOZE", "SNOOZE_ALARM" -> {
                alarmHelper.snoozeAlarm(1)
            }

            "STOP_ALARM" -> {
                val locationId = intent.getIntExtra("location_id", -1)
                alarmHelper.stopAlarm(if (locationId != -1) locationId else null)
            }
        }
    }
}






