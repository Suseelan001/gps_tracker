package com.locationReminder


import android.Manifest
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.locationReminder.model.apiUtil.serviceModel.LocationDaoEntryPoint
import com.locationReminder.reponseModel.LocationDetail
import com.locationReminder.roomDatabase.dao.ContactDAO
import com.locationReminder.roomDatabase.dao.LocationDAO
import dagger.hilt.android.EntryPointAccessors
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.locationReminder.alarmModel.AlarmActivity
import com.locationReminder.alarmModel.AlarmHelper


@RequiresApi(Build.VERSION_CODES.O)
class LocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationDao: LocationDAO
    private lateinit var contactDao: ContactDAO

    private val locationStates = mutableMapOf<String, Boolean>()
    private val delayedExitStates = mutableMapOf<String, Boolean>()
    private var monitoredLocations: List<LocationDetail> = emptyList()
    private var isLocationUpdatesStarted = false


    private val stopUpdatesReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "ACTION_STOP_LOCATION_UPDATES") {
                stopLocationUpdates()
            }
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        LocalBroadcastManager.getInstance(this).registerReceiver(stopUpdatesReceiver, IntentFilter("ACTION_STOP_LOCATION_UPDATES"))

        val entryPoint = EntryPointAccessors.fromApplication(
            applicationContext,
            LocationDaoEntryPoint::class.java
        )
        locationDao = entryPoint.locationDao()
        contactDao = entryPoint.contactDAO()

        locationRequest  = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000L)
            .setMinUpdateDistanceMeters(10.0f)
            .setMinUpdateIntervalMillis(10000L)
            .build()



        startForegroundServiceNotification()
        startLocationUpdates()
        loadMonitoredLocations()
    }

    private fun startForegroundServiceNotification() {
        val channelId = "location_channel"
        val channel = NotificationChannel(channelId, "Location Tracking", NotificationManager.IMPORTANCE_LOW)
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Location Tracking")
            .setContentText("Running in background")
            .setSmallIcon(R.drawable.notification)
            .build()

        startForeground(1, notification)
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.locations.forEach { currentLocation ->
                monitoredLocations.forEach { monitoredLoc ->
                    val distanceResult = FloatArray(1)

                    Location.distanceBetween(
                        currentLocation.latitude,
                        currentLocation.longitude,
                        monitoredLoc.lat,
                        monitoredLoc.lng,
                        distanceResult
                    )
                    val distance = distanceResult[0]

                    Toast.makeText(this@LocationService, currentLocation.latitude.toString() +"  " + currentLocation.longitude.toString() + " distance " + distance, Toast.LENGTH_SHORT).show()
                    val isInsideGeofence = distance <= monitoredLoc.radius
                    handleLocationTransition(monitoredLoc, isInsideGeofence)
                }
            }
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun loadMonitoredLocations() {
        locationDao.getAllRecord().observe(ProcessLifecycleOwner.get()) { allLocations ->
            monitoredLocations = allLocations.filter {
                it.currentStatus == true &&
                        (it.entryType == "Entry" || it.entryType == "Exit")
            }

            if (monitoredLocations.isEmpty()) {
                stopLocationUpdates()
            } else if (!isLocationUpdatesStarted) {
                startLocationUpdates()
            }
        }

    }



    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        isLocationUpdatesStarted = false
    }


    private fun handleLocationTransition(locationDetail: LocationDetail, isInside: Boolean) {
        val id = locationDetail.id.toString()
        val wasInside = locationStates[id] == true
        val locationType = locationDetail.entryType




        when {
            locationType == "Entry" && !wasInside && isInside -> {
                delayedExitStates[id] = false
              triggerAlarm(locationDetail.id)
                locationStates[id] = true
            }

            locationType == "Exit" && wasInside && !isInside -> {
                delayedExitStates[id] = false
                triggerAlarm(locationDetail.id)
                locationStates[id] = false
            }

            locationType == "Exit" && !wasInside && isInside -> {
                locationStates[id] = true
                delayedExitStates[id] = false
            }

            locationType == "Exit" && !isInside && !wasInside -> {
                delayedExitStates[id] = true
            }
        }
    }

    private fun triggerAlarm(locationId: Int) {
        val intent = Intent(this, LocationTransitionReceiver::class.java).apply {
            putExtra("location_id", locationId)
        }
        sendBroadcast(intent)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(stopUpdatesReceiver)
    }

}
