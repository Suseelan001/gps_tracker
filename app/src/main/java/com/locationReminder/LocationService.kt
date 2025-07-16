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
import com.locationReminder.roomDatabase.dao.LocationDAO
import dagger.hilt.android.EntryPointAccessors
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.locationReminder.alarmModel.AlarmHelper
import com.locationReminder.roomDatabase.dao.SettingsDAO
import com.locationReminder.view.getAddressFromLatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



@RequiresApi(Build.VERSION_CODES.O)
class LocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationDao: LocationDAO
    private lateinit var settingsDAO: SettingsDAO
    lateinit var alarmHelper: AlarmHelper


    private val locationStates = mutableMapOf<String, Boolean>()
    private var monitoredLocations: List<LocationDetail> = emptyList()
    private val delayedEntryStates = mutableMapOf<String, Boolean>()
    private val delayedExitStates = mutableMapOf<String, Boolean>()

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

        LocalBroadcastManager.getInstance(this).registerReceiver(
            stopUpdatesReceiver,
            IntentFilter("ACTION_STOP_LOCATION_UPDATES")
        )

        val entryPoint = EntryPointAccessors.fromApplication(
            applicationContext,
            LocationDaoEntryPoint::class.java
        )
        locationDao = entryPoint.locationDao()
        settingsDAO = entryPoint.settingsDAO()
        alarmHelper = entryPoint.alarmHelper()

        startForegroundServiceNotification()

        settingsDAO.getSettings().observe(ProcessLifecycleOwner.get()) { settings ->
            val intervalTime = if (settings != null) {
                val intervalInSec = convertIntervalToSeconds(settings.locationUpdateInterval)
                intervalInSec * 1000L
            } else {
                3000L
            }
            locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                intervalTime
            )
                .setMinUpdateIntervalMillis(intervalTime)
                .setMinUpdateDistanceMeters(3.0f)
                .build()

            startLocationUpdates()

            loadMonitoredLocations()
        }


    }

    fun convertIntervalToSeconds(interval: String): Long {
        return when {
            interval.equals("adaptable", ignoreCase = true) -> 3L
            interval.contains("second", ignoreCase = true) -> {
                interval.filter { it.isDigit() }.toLongOrNull() ?: 5L
            }
            interval.contains("minute", ignoreCase = true) -> {
                (interval.filter { it.isDigit() }.toLongOrNull() ?: 1L) * 60
            }
            else -> 3L
        }
    }


    private fun startForegroundServiceNotification() {
        val channelId = "location_channel"
        val channelName = "Location Tracking"

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Used for location tracking in background"
            enableLights(false)
            enableVibration(false)
            setSound(null, null)
        }
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Location Service Running")
            .setContentText("Your location is being tracked in the background.")
            .setSmallIcon(R.drawable.notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setOngoing(true)
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

                    val isInsideGeofence = distance <= monitoredLoc.radius
                    handleLocationTransition(monitoredLoc, isInsideGeofence , currentLocation.latitude, currentLocation.longitude )
                }
            }
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun loadMonitoredLocations() {
        locationDao.getAllRecord().observe(ProcessLifecycleOwner.get()) { allLocations ->
            println("CHECK_TAG_ALL_RECORD_allLocations " + Gson().toJson(allLocations))


            monitoredLocations = allLocations.filter {
                it.currentStatus == true &&
                        (it.entryType == "Entry" || it.entryType == "Exit"|| it.entryType == "Marker"|| it.entryType == "ImportedMarker")
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

    fun handleLocationTransition(
        locationDetail: LocationDetail,
        isInside: Boolean,
        currentLatitude: Double,
        currentLongitude: Double
    ) {
        val id = locationDetail.id.toString()
        val wasInside = locationStates[id] == true
        val locationType = locationDetail.entryType
        val isEntryType = locationType in listOf("Entry", "Marker", "ImportedMarker")

        if (isEntryType) {
            if (locationStates[id] == null) {
                locationStates[id] = isInside
                delayedEntryStates[id] = isInside
                return
            }

            if (!wasInside && isInside && delayedEntryStates[id] != true) {
                locationStates[id] = true
                triggerAlarm(locationDetail, currentLatitude, currentLongitude)
            }

            else if (delayedEntryStates[id] == true && !wasInside && isInside) {
                delayedEntryStates[id] = false
                locationStates[id] = true
                triggerAlarm(locationDetail, currentLatitude, currentLongitude)
            }

            else if (wasInside && !isInside) {
                locationStates[id] = false
            }

            return
        }


        if (locationType == "Exit") {
            when {
                wasInside && !isInside && delayedExitStates[id] != true -> {
                    locationStates[id] = false
                    triggerAlarm(locationDetail, currentLatitude, currentLongitude)
                }

                !wasInside && isInside -> {
                    locationStates[id] = true
                    delayedExitStates[id] = true
                }

                // ✅ Flow 3: Delayed Exit Trigger — now exiting after setup
                delayedExitStates[id] == true && wasInside && !isInside -> {
                    locationStates[id] = false
                    delayedExitStates[id] = false
                    triggerAlarm(locationDetail, currentLatitude, currentLongitude)
                }

                // Update current state passively
                isInside -> locationStates[id] = true
                else -> locationStates[id] = false
            }
        }
    }

    private fun triggerAlarm(locationDetail: LocationDetail, lat: Double, lng: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            val addr = getAddressFromLatLng(this@LocationService, LatLng(lat, lng))
            if (locationDetail.id != -1) {
                alarmHelper.playAlarm(locationDetail.id, addr)
            }
        }
    }


    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(stopUpdatesReceiver)
    }

}
