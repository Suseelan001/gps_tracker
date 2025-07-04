package com.locationReminder.view

import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.locationReminder.reponseModel.LocationDetail
import com.locationReminder.ui.theme.Hex222227
import com.locationReminder.viewModel.AddLocationViewModel
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.locationReminder.ui.theme.Hex36374a
import com.locationReminder.R
import com.locationReminder.ui.theme.Hexeef267
import com.locationReminder.ui.theme.RobotoMediumWithHexFFFFFF18sp
import com.locationReminder.ui.theme.RobotoRegularWithHexFFFFFF14sp
import com.locationReminder.ui.theme.RobotoRegularWithHexHex80808016sp
import com.locationReminder.ui.theme.RobotoRegularWithHexHexeef26714sp
import com.locationReminder.view.appNavigation.NavigationRoute
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun OnEntryListScreen(
    navController: NavHostController,
    addLocationViewModel: AddLocationViewModel
)
 {
    val context = LocalContext.current
    val allRecords by addLocationViewModel.getAllRecord().observeAsState(emptyList())

    val entryList = remember(allRecords) { allRecords.filter { it.entryType.equals("Entry", ignoreCase = true) } }
     val selectedItems = remember { mutableStateListOf<LocationDetail>() }
    val isSelectionMode = selectedItems.isNotEmpty()

    val currentLatitude = remember { mutableDoubleStateOf(0.0) }
    val currentLongitude = remember { mutableDoubleStateOf(0.0) }

    val listState = rememberLazyListState()



    if (entryList.isNotEmpty()) {
        LaunchedEffect(Unit) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

            if (ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {

                val locationRequest = LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    5000L
                ).apply {
                    setMinUpdateDistanceMeters(5f)
                }.build()

                val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        val location = result.lastLocation
                        location?.let {
                            currentLatitude.doubleValue = it.latitude
                            currentLongitude.doubleValue = it.longitude
                        }
                    }
                }

                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        }
    }






    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isSelectionMode) "${selectedItems.size} selected" else "On Entry",
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        if (entryList.isNotEmpty()){
                            FloatingActionButton(
                                onClick = {
                                    navController.navigate("${NavigationRoute.VIEWALLMAPSCREEN.path}/Entry/${""}") {
                                        popUpTo(NavigationRoute.VIEWALLMAPSCREEN.path) {
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                    }
                                },
                                containerColor = Hexeef267,
                                contentColor = Hex222227,
                                modifier = Modifier.padding(end = 24.dp, bottom = 12.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.viewall), contentDescription = "View All")
                            }
                        }

                        FloatingActionButton(
                            onClick = {
                                navController.navigate("${NavigationRoute.MAPSCREEN.path}/Entry/${""}/${""}/${""}") {
                                    popUpTo(NavigationRoute.MAPSCREEN.path) {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                }
                            },
                            containerColor = Hexeef267,
                            contentColor = Hex222227,
                            modifier = Modifier.padding(end = 24.dp, bottom = 12.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add Marker")
                        }

                    }
                },
                actions = {
                    if (isSelectionMode) {
                        IconButton(onClick = {
                            selectedItems.forEach { addLocationViewModel.deleteItem(it) }
                            selectedItems.clear()
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                        }
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Hex222227,
                    scrolledContainerColor = Hex222227
                )
            )

        }) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Hex222227)
        ){
        if (entryList.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No entries found",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tap the '+' button to add a new location entry.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

            } else
            {


                val adFrequency = 4

                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val numberOfAds = entryList.size / adFrequency
                    val shouldShowEndAd = entryList.size % adFrequency != 0
                    val totalAds = numberOfAds + if (shouldShowEndAd) 1 else 0
                    val totalCount = entryList.size + totalAds

                    items(totalCount) { index ->
                        val adInsertedBefore = index / (adFrequency + 1)
                        if ((index + 1) % (adFrequency + 1) == 0 || // Regular interval ads
                            (shouldShowEndAd && index == totalCount - 1 && entryList.size % adFrequency != 0) // End ad condition
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                BannerAd()
                            }
                        } else {
                            val actualIndex = index - adInsertedBefore
                            if (actualIndex < entryList.size) {
                                val item = entryList[actualIndex]

                                EntryCard(
                                    item = item,
                                    isSelected = selectedItems.contains(item),
                                    onClick = {
                                        if (isSelectionMode) {
                                            if (selectedItems.contains(item)) selectedItems.remove(item)
                                            else selectedItems.add(item)
                                        } else {
                                            navController.navigate("${NavigationRoute.MAPSCREEN.path}/Entry/${item.id}/${""}/${""}")
                                        }
                                    },
                                    onLongClick = {
                                        if (!selectedItems.contains(item)) selectedItems.add(item)
                                    },
                                    currentLatitude = currentLatitude.doubleValue,
                                    currentLongitude = currentLongitude.doubleValue,
                                    onToggleChange = { newStatus ->
                                        addLocationViewModel.updateCurrentStatus(item.id, newStatus)
                                    }
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }


            }


        }
    }
  }

 @OptIn(ExperimentalFoundationApi::class)
@Composable
fun EntryCard(
    item: LocationDetail,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    currentLatitude: Double,
    currentLongitude: Double,
    onToggleChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(bottom = 16.dp)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFE3F2FD) else Hex36374a
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.onentry),
                contentDescription = "Entry Image",
                modifier = Modifier.size(70.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 16.dp, bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.title,
                        style = RobotoMediumWithHexFFFFFF18sp,
                        modifier = Modifier.weight(1f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Switch(
                        checked = item.currentStatus,
                        onCheckedChange = { onToggleChange(it) },
                        modifier = Modifier.scale(0.75f),
                        colors = SwitchDefaults.colors(
                            uncheckedThumbColor = Color(0xFFEEF267),
                            checkedThumbColor = Color.White,
                            uncheckedTrackColor = Color.White,
                            checkedTrackColor = Color(0xFFEEF267)
                        )
                    )
                }

                Text(
                    text = item.address,
                    style = RobotoRegularWithHexHex80808016sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currentLatitude != 0.0 && currentLongitude != 0.0) {
                        Text(
                            text = calculateDistanceInKm(currentLatitude, currentLongitude, item.lat, item.lng),
                            style = RobotoRegularWithHexHexeef26714sp
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = String.format(Locale.US, "%.5f, %.5f", item.lat, item.lng),
                        style = RobotoRegularWithHexFFFFFF14sp,
                        modifier = Modifier.padding(end = 2.dp)
                    )
                }
            }
        }
    }
}



fun calculateDistanceInKm(
    startLat: Double,
    startLng: Double,
    endLat: Double,
    endLng: Double
): String {
    val startLocation = Location("").apply {
        latitude = startLat
        longitude = startLng
    }

    val endLocation = Location("").apply {
        latitude = endLat
        longitude = endLng
    }

    val distanceInMeters = startLocation.distanceTo(endLocation)
    return if (distanceInMeters < 1000) {
        "${distanceInMeters.toInt()} meters away"
    } else {
        String.format(Locale.US, "%.1f Km Away", distanceInMeters / 1000)
    }
}
