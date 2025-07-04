package com.locationReminder.view

import android.content.pm.PackageManager
import android.os.Looper
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.locationReminder.viewModel.AddLocationViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import com.locationReminder.R
import com.locationReminder.reponseModel.LocationDetail
import com.locationReminder.ui.theme.Hex222227
import com.locationReminder.ui.theme.Hex36374a
import com.locationReminder.ui.theme.Hexeef267
import com.locationReminder.ui.theme.RobotoMediumWithHexFFFFFF18sp
import com.locationReminder.ui.theme.RobotoRegularWithHexFFFFFF14sp
import com.locationReminder.ui.theme.RobotoRegularWithHexHex80808016sp
import com.locationReminder.ui.theme.RobotoRegularWithHexHexeef26714sp
import com.locationReminder.view.appNavigation.NavigationRoute
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun OnExitListScreen(
    navController: NavHostController,
    addLocationViewModel: AddLocationViewModel
) {
    val getAccountList by addLocationViewModel.getAllRecord().observeAsState(emptyList())
    val exitList = getAccountList.filter { it.entryType.equals("Exit", ignoreCase = true) }

    val selectedItems = remember { mutableStateListOf<LocationDetail>() }
    val isSelectionMode = selectedItems.isNotEmpty()



    val listState = rememberLazyListState()

    val context = LocalContext.current
    val currentLatitude = remember { mutableDoubleStateOf(0.0) }
    val currentLongitude = remember { mutableDoubleStateOf(0.0) }

    if (exitList.isNotEmpty()) {
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
    val topBarBackgroundColor = Hex222227
    val topBarTextColor = Color.White


    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isSelectionMode) "${selectedItems.size} selected" else "On Exit",
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        if (exitList.isNotEmpty()){
                            FloatingActionButton(
                                onClick = {
                                    navController.navigate("${NavigationRoute.VIEWALLMAPSCREEN.path}/Exit/${""}") {
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
                                navController.navigate("${NavigationRoute.MAPSCREEN.path}/Exit/${""}/${""}/${""}") {
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
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = topBarTextColor
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = topBarBackgroundColor,
                    scrolledContainerColor = topBarBackgroundColor,
                    titleContentColor = topBarTextColor,
                    navigationIconContentColor = topBarTextColor,
                    actionIconContentColor = topBarTextColor
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Hex222227)

        ) {
            if (exitList.isEmpty()) {
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
                        text = "Tap the '+' button to add a new location exit",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

            } else
            {val adFrequency = 4

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val numberOfAds = exitList.size / adFrequency
                    val shouldShowEndAd = exitList.size % adFrequency != 0
                    val totalAds = numberOfAds + if (shouldShowEndAd) 1 else 0
                    val totalCount = exitList.size + totalAds

                    items(totalCount) { index ->
                        val adInsertedBefore = index / (adFrequency + 1)

                        // Ad positions: every 4 items + one final ad
                        val isAdPosition =
                            (index + 1) % (adFrequency + 1) == 0 ||
                                    (shouldShowEndAd && index == totalCount - 1 && exitList.size % adFrequency != 0)

                        if (isAdPosition) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                            ) {
                                BannerAd()
                            }
                        } else {
                            val actualIndex = index - adInsertedBefore
                            if (actualIndex in exitList.indices) {
                                val item = exitList[actualIndex]
                                val isSelected = selectedItems.contains(item)

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                        .padding(bottom = 16.dp)
                                        .combinedClickable(
                                            onClick = {
                                                if (isSelectionMode) {
                                                    if (isSelected) selectedItems.remove(item)
                                                    else selectedItems.add(item)
                                                } else {
                                                    navController.navigate("${NavigationRoute.MAPSCREEN.path}/Exit/${item.id}/${""}/${""}")
                                                }
                                            },
                                            onLongClick = {
                                                if (!isSelected) selectedItems.add(item)
                                            }
                                        ),
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
                                            painter = painterResource(id = R.drawable.onexit),
                                            contentDescription = "Item Image",
                                            modifier = Modifier
                                                .width(70.dp)
                                                .height(70.dp),
                                            contentScale = ContentScale.Crop
                                        )

                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(end = 16.dp, bottom = 16.dp)
                                                .wrapContentWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = item.title,
                                                    style = RobotoMediumWithHexFFFFFF18sp,
                                                    modifier = Modifier.weight(1f),
                                                    maxLines = 2,
                                                    overflow = TextOverflow.Ellipsis
                                                )

                                                Spacer(Modifier.width(4.dp))

                                                Switch(
                                                    checked = item.currentStatus,
                                                    onCheckedChange = null,
                                                    modifier = Modifier
                                                        .scale(0.75f)
                                                        .clickable {
                                                            addLocationViewModel.updateCurrentStatus(
                                                                item.id,
                                                                !item.currentStatus
                                                            )
                                                        },
                                                    colors = SwitchDefaults.colors(
                                                        uncheckedThumbColor = Color(0xFFEEF267),
                                                        checkedThumbColor = Color.White,
                                                        uncheckedTrackColor = Color.White,
                                                        checkedTrackColor = Color(0xFFEEF267),
                                                        uncheckedBorderColor = Color.Transparent,
                                                        checkedBorderColor = Color.Transparent
                                                    )
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Text(
                                                text = item.address,
                                                style = RobotoRegularWithHexHex80808016sp,
                                                maxLines = 3,
                                                overflow = TextOverflow.Ellipsis
                                            )

                                            Spacer(modifier = Modifier.height(4.dp))

                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.Start
                                            ) {
                                                if (currentLatitude.doubleValue != 0.0 && currentLongitude.doubleValue != 0.0) {
                                                    Text(
                                                        text = calculateDistanceInKm(
                                                            currentLatitude.doubleValue,
                                                            currentLongitude.doubleValue,
                                                            item.lat,
                                                            item.lng
                                                        ),
                                                        style = RobotoRegularWithHexHexeef26714sp,
                                                    )
                                                }
                                                Spacer(modifier = Modifier.weight(1f))
                                                Text(
                                                    text = String.format(
                                                        Locale.US,
                                                        "%.5f, %.5f",
                                                        item.lat,
                                                        item.lng
                                                    ),
                                                    style = RobotoRegularWithHexFFFFFF14sp,
                                                    modifier = Modifier.padding(end = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                }
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


