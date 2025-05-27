package com.locationReminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.locationReminder.alarmModel.AlarmActivity
import com.locationReminder.alarmModel.AlarmHelper
import com.locationReminder.model.apiUtil.serviceModel.LocationDaoEntryPoint
import dagger.hilt.android.EntryPointAccessors
import androidx.core.content.edit

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent?.hasError() == true) {
            return
        }

        val triggeringGeofence = geofencingEvent?.triggeringGeofences?.firstOrNull()

        val requestId = triggeringGeofence?.requestId
        val parts = requestId?.split("|")

        val locationId = parts?.getOrNull(0)?.toIntOrNull()
        val locationType = parts?.getOrNull(1)

        var locationEntered=""
        if (locationId == null) {
            return
        }
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            LocationDaoEntryPoint::class.java
        )
        val locationDao = entryPoint.locationDao()
        val contactDAO = entryPoint.contactDAO()
        val sharedPreference = entryPoint.mySharedPreference()

        when (geofencingEvent.geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                if (locationType == "Entry") {
                    AlarmHelper.getInstance(context, locationDao,contactDAO,sharedPreference).playAlarm(locationId)
                    val fullScreenIntent = Intent(context, AlarmActivity::class.java).apply {
                        putExtra("location_id", locationId)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    context.startActivity(fullScreenIntent)
                }

                if (locationType == "Exit") {
                    locationEntered="Entered"
                    GeofenceStateManager.markEntered(context, locationId)
                }
            }

            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                if (locationEntered=="Entered"){
                    if (locationType == "Exit" && GeofenceStateManager.hasEntered(context, locationId)) {
                        GeofenceStateManager.clearEntered(context, locationId)
                        AlarmHelper.getInstance(context, locationDao,contactDAO,sharedPreference).playAlarm(locationId)

                        val fullScreenIntent = Intent(context, AlarmActivity::class.java).apply {
                            putExtra("location_id", locationId)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        context.startActivity(fullScreenIntent)
                    }
                }else{
                    AlarmHelper.getInstance(context, locationDao,contactDAO,sharedPreference).playAlarm(locationId)
                    val fullScreenIntent = Intent(context, AlarmActivity::class.java).apply {
                        putExtra("location_id", locationId)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    context.startActivity(fullScreenIntent)
                }

            }
        }
    }
}


object GeofenceStateManager {
    fun markEntered(context: Context, locationId: Int) {
        context.getSharedPreferences("geofence_states", Context.MODE_PRIVATE)
            .edit {
                putBoolean("entered_$locationId", true)
            }
    }

    fun hasEntered(context: Context, locationId: Int): Boolean {
        return context.getSharedPreferences("geofence_states", Context.MODE_PRIVATE)
            .getBoolean("entered_$locationId", false)
    }

    fun clearEntered(context: Context, locationId: Int) {
        context.getSharedPreferences("geofence_states", Context.MODE_PRIVATE)
            .edit {
                remove("entered_$locationId")
            }
    }

}

