package com.locationReminder.alarmModel

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.locationReminder.R


class GeofenceForegroundService : Service() {
    private lateinit var geofencingClient: GeofencingClient

    override fun onCreate() {
        super.onCreate()
        geofencingClient = LocationServices.getGeofencingClient(this)
        startForeground()
    }

    private fun startForeground() {
        val channelId = createNotificationChannel()

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Geofence Monitoring")
            .setContentText("Monitoring your locations in background")
            .setSmallIcon(R.drawable.notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        startForeground(1, notification)
    }

    private fun createNotificationChannel(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "geofence_channel"
            val channel = NotificationChannel(
                channelId,
                "Geofence Monitoring",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Background geofence monitoring"
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
            return channelId
        }
        return ""
    }

    override fun onBind(intent: Intent?) = null
}