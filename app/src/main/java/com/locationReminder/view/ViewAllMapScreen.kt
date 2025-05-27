package com.locationReminder.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.locationReminder.ui.theme.Hex095d7f
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import com.locationReminder.reponseModel.LocationDetail
import com.locationReminder.viewModel.AddLocationViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun ViewAllMapScreen(navHostController:NavHostController,addLocationViewModel: AddLocationViewModel,type: String
) {
    val context = LocalContext.current

    val getAccountList by addLocationViewModel.getAllRecord().observeAsState(emptyList())
    val entryList = getAccountList.filter { it.entryType == type }

    if (entryList.isNotEmpty()){
        GeofenceMapApp(context, navHostController, entryList,type)

    }

  /*  LaunchedEffect(getAccountList) {
        val geofencingClient = LocationServices.getGeofencingClient(context)

        for (item in getAccountList) {
            val geofence = Geofence.Builder()
                .setRequestId(item.id.toString())
                .setCircularRegion(
                    item.lat,
                    item.lng,
                    item.radius.toFloat()
                )
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build()

            val geofencingRequest = GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build()

            val geofencePendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                Intent(context, GeofenceBroadcastReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent)
                    .addOnSuccessListener {
                        Log.d("Geofence", "Added geofence for item ${item.id}")
                    }
                    .addOnFailureListener {
                        Log.e("Geofence", "Failed to add geofence", it)
                    }
            }
        }
    }*/


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeofenceMapApp(
    context: Context,
    navHostController: NavHostController,
    locationList: List<LocationDetail>,
    type: String
) {
    val geofencingClient = remember { LocationServices.getGeofencingClient(context) }
    val cameraPositionState = rememberCameraPositionState()
    val geofenceRadius = remember { mutableDoubleStateOf(50.0) }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val currentLocation = remember { mutableStateOf<LatLng?>(null) }

    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    // Log current location for debugging
                    Log.d("LocationDebug", "Current Location: ${it.latitude}, ${it.longitude}")

                    val latLng = LatLng(it.latitude, it.longitude)
                    currentLocation.value = latLng
                    cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(latLng, 15f))


                    locationList.forEach { locationDetail ->

                        val currentLoc = Location("current")
                        currentLoc.latitude = it.latitude
                        currentLoc.longitude = it.longitude

                        val latLngDestination = LatLng(locationDetail.lat, locationDetail.lng)
                        Log.d("LocationDebug", "locationDetail Location: ${locationDetail.lat}, ${ locationDetail.lng}")

                        val destination = Location("destination")
                        destination.latitude = latLngDestination.latitude
                        destination.longitude = latLngDestination.longitude

                        Log.d("LocationDebug", "Destination Location:  - Lat: ${destination.latitude}, Lng: ${destination.longitude}")

                        val distance = currentLoc.distanceTo(destination)
                        Log.d("LocationDistance", "Distance to : $distance meters")
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("On $type List") },
                navigationIcon = {
                    IconButton(onClick = {
                        navHostController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // You can keep other actions here if needed
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Hex095d7f
                )
            )

            GoogleMap(
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    myLocationButtonEnabled = false
                ),
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = true)
            ) {
                locationList.forEach { location ->
                    val latLng = LatLng(location.lat, location.lng)

                    Marker(
                        state = MarkerState(position = latLng),
                        title =" location.locationName",
                        snippet = "Radius: ${location.radius}m"
                    )

                    Circle(
                        center = latLng,
                        radius = location.radius.toDouble(),
                        strokeColor = Color.Red,
                        fillColor = Color.Red.copy(alpha = 0.2f)
                    )
                }
            }
        }
    }
}




