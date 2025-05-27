package com.locationReminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.locationReminder.alarmModel.AlarmActivity
import com.locationReminder.alarmModel.AlarmHelper
import com.locationReminder.model.apiUtil.serviceModel.LocationDaoEntryPoint
import dagger.hilt.android.EntryPointAccessors
import androidx.core.content.edit
import com.google.gson.Gson

class LocationTransitionReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val locationId = intent.getIntExtra("location_id", -1)
        if (locationId != -1) {
            val entryPoint = EntryPointAccessors.fromApplication(
                context.applicationContext,
                LocationDaoEntryPoint::class.java
            )
            val locationDao = entryPoint.locationDao()
            val contactDAO = entryPoint.contactDAO()
            val sharedPreference = entryPoint.mySharedPreference()




            AlarmHelper.getInstance(context, locationDao, contactDAO,sharedPreference).playAlarm(locationId)

            val fullScreenIntent = Intent(context, AlarmActivity::class.java).apply {
                putExtra("location_id", locationId)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(fullScreenIntent)
        }
    }
}


