package com.locationReminder.view


import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.locationReminder.R
import com.locationReminder.viewModel.AddLocationViewModel
import com.locationReminder.viewModel.PlacesClientManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun ViewAllMapScreen(
    navHostController: NavHostController,
    type: String,
    categoryId: String,
    addLocationViewModel: AddLocationViewModel
) {
    SetStatusBarStyle()
    var showMap by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(200)
        showMap = true
    }
    if (showMap) {
        MapHomeScreenViewAllMap(navHostController, type,categoryId,addLocationViewModel)
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapHomeScreenViewAllMap(
    navHostController: NavHostController,
    type: String,categoryId:String,addLocationViewModel: AddLocationViewModel)
{

    val context = LocalContext.current
    var currentLocation by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var markerPosition by remember { mutableStateOf<LatLng?>(null) }
    var circleRadius by remember { mutableFloatStateOf(100f) }
    val coroutineScope = rememberCoroutineScope()


    val getAccountList by addLocationViewModel.getAllRecord().observeAsState(emptyList())

    val entryList = if (categoryId.isNotEmpty()) {
        getAccountList.filter {
            it.entryType.equals(type, ignoreCase = true) &&
                    it.category_id == categoryId
        }
    } else {
        getAccountList.filter {
            it.entryType.equals(type, ignoreCase = true)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            PlacesClientManager.shutdown()
        }
    }
    BackHandler(enabled = true) {
        navHostController.popBackStack()
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getCurrentLocation(context) { location ->
                currentLocation = location
                isLoading = false
            }
        } else {
            isLoading = false
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 10f)
    }

    LaunchedEffect(Unit) {
        when {
            ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                getCurrentLocation(context) { location ->
                    currentLocation = location
                    isLoading = false
                }
            }

            else -> requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    LaunchedEffect(currentLocation) {
            currentLocation?.let {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(
                    LatLng(it.first, it.second), 15f
                )
            }
    }


        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.fillMaxSize()) {
                        Box(Modifier.fillMaxSize()) {
                                LaunchedEffect(markerPosition) {
                                    markerPosition?.let {
                                        cameraPositionState.animate(
                                            update = CameraUpdateFactory.newLatLngZoom(it, 15f)
                                        )
                                    }
                                }
                            GoogleMap(
                                modifier = Modifier.matchParentSize(),
                                cameraPositionState = cameraPositionState,
                                properties = MapProperties(isMyLocationEnabled = true),
                                uiSettings = MapUiSettings(
                                    zoomControlsEnabled = false,
                                    myLocationButtonEnabled = false
                                ),
                                onMapClick = { latLng -> }
                            ) {
                                // Optional: Individual marker and circle (e.g., selected marker)
                                markerPosition?.let {
                                    Marker(
                                        state = MarkerState(position = it),
                                        icon = BitmapFromVector(context, R.drawable.marker_icon)
                                    )
                                    Circle(
                                        center = it,
                                        radius = circleRadius.toDouble(),
                                        strokeColor = Color.Blue,
                                        strokeWidth = 2f,
                                        fillColor = Color(0x330000FF)
                                    )
                                }

                                entryList.forEach { record ->
                                    val lat = record.lat
                                    val lng = record.lng
                                        val latLng = LatLng(lat, lng)

                                        Marker(
                                            state = MarkerState(position = latLng),
                                            title = record.title,
                                            icon = BitmapFromVector(context, R.drawable.marker_icon)

                                        )

                                        Circle(
                                            center = latLng,
                                            radius = record.radius.toDouble(),
                                            strokeColor = if (record.entryType =="Exit") Color.Red else Color.Blue,
                                            strokeWidth = 2f,
                                            fillColor =if (record.entryType =="Exit")  Color(0x22FF0000) else Color(0x330000FF)
                                        )

                                }
                            }



                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(end = 16.dp, bottom = 180.dp),
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                FloatingActionButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            val fusedLocationClient =
                                                LocationServices.getFusedLocationProviderClient(
                                                    context
                                                )
                                            val location = fusedLocationClient.lastLocation.await()
                                            location?.let {
                                                val currentLatLng =
                                                    LatLng(it.latitude, it.longitude)
                                                cameraPositionState.animate(
                                                    CameraUpdateFactory.newLatLngZoom(
                                                        currentLatLng,
                                                        15f
                                                    )
                                                )
                                            }
                                        }
                                    },
                                    containerColor = Color.White,
                                    contentColor = Color.Black,
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.focus_current_location),
                                        contentDescription = "Go to current location"
                                    )
                                }

                                FloatingActionButton(
                                    onClick = {
                                        val currentZoom = cameraPositionState.position.zoom
                                        coroutineScope.launch {
                                            cameraPositionState.animate(
                                                CameraUpdateFactory.zoomTo(currentZoom + 1f)
                                            )
                                        }
                                    },
                                    containerColor = Color.White,
                                    contentColor = Color.Black,
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.plus),
                                        contentDescription = "Zoom In"
                                    )
                                }

                                FloatingActionButton(
                                    onClick = {
                                        val currentZoom = cameraPositionState.position.zoom
                                        coroutineScope.launch {
                                            cameraPositionState.animate(
                                                CameraUpdateFactory.zoomTo(currentZoom - 1f)
                                            )
                                        }
                                    },
                                    containerColor = Color.White,
                                    contentColor = Color.Black,
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.minus),
                                        contentDescription = "Zoom Out",
                                        tint = Color.Black,
                                        modifier = Modifier.padding(3.dp)
                                    )
                                }

                            }
                        }
                }
            }
        }
}


