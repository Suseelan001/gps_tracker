package com.locationReminder.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Looper
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.locationReminder.viewModel.AddLocationViewModel
import com.locationReminder.R
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.gson.Gson
import com.locationReminder.reponseModel.LocationDetail
import com.locationReminder.ui.theme.Hex222227
import com.locationReminder.ui.theme.Hex36374a
import com.locationReminder.ui.theme.Hexeef267
import com.locationReminder.ui.theme.RobotoMediumWithHexFFFFFF18sp
import com.locationReminder.ui.theme.RobotoRegularWithHexFFFFFF14sp
import com.locationReminder.ui.theme.RobotoRegularWithHexHex80808016sp
import com.locationReminder.ui.theme.RobotoRegularWithHexHexeef26714sp
import com.locationReminder.view.appNavigation.NavigationRoute
import com.locationReminder.viewModel.AddImportedCategoryNameViewModel
import com.locationReminder.viewModel.SharedPreferenceVM
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ImportedMarkerListScreen(
    navController: NavHostController,
    addLocationViewModel: AddLocationViewModel,
    sharedPreferenceVM: SharedPreferenceVM,
    recordId: String,addImportedCategoryNameViewModel:AddImportedCategoryNameViewModel
) {
    val record = addImportedCategoryNameViewModel.getRecordById(recordId.toInt())
    LaunchedEffect(Unit) {
        if (record.firstTimeImport==true){
            addLocationViewModel.getImportedMarkerList("eq.${record.categoryName}","eq.${record.userId}")
           addImportedCategoryNameViewModel.updateRecordStatus(record.id,false)
        }
    }
    val getAccountList by addLocationViewModel.getMarkerListByFolder("${record.id}","ImportedMarker").observeAsState(emptyList())
    val markerList = getAccountList.filter { it.entryType.equals("ImportedMarker", ignoreCase = true) }


    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val listState = rememberLazyListState()

    val context = LocalContext.current
    val currentLatitude = remember { mutableDoubleStateOf(0.0) }
    val currentLongitude = remember { mutableDoubleStateOf(0.0) }

    if (markerList.isNotEmpty()) {
        LaunchedEffect(Unit) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
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

    val selectedItems = remember { mutableStateListOf<LocationDetail>() }
    val isSelectionMode = selectedItems.isNotEmpty()
    val isScrolled by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
        }
    }
    val showMenu = remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        "On Marker (Entry)",
                        color = topBarTextColor
                    )
                },
                actions = {
                    if (isSelectionMode) {
                        IconButton(onClick = {
                            selectedItems.forEach { addLocationViewModel.deleteItem(it) }
                            selectedItems.clear()
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                        }
                    } else if (isScrolled) {
                        IconButton(onClick = { showMenu.value = true }) {
                        }
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = topBarBackgroundColor,
                    scrolledContainerColor = topBarBackgroundColor,
                    titleContentColor = topBarTextColor,
                    navigationIconContentColor = topBarTextColor,
                    actionIconContentColor = topBarTextColor
                ),
                scrollBehavior = scrollBehavior
            )

        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = {

        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Hex222227)
                .padding(bottom = 10.dp)

        ) {
            if (sharedPreferenceVM.isUserLoggedIn() == false) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "You do not have an account",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = {
                                navController.navigate(NavigationRoute.LOGINHOME.path)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEEF267))
                        ) {
                            Text(text = "Login", color = Color.Black)
                        }
                    }
                }
            }else
            {
                if (markerList.isEmpty()) {
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
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 1.dp)
                    ) {
                        items(markerList) { item ->
                            ImportedMarkerCard(
                                item = item,
                                isSelected = selectedItems.contains(item),
                                onClick = {
                                    if (isSelectionMode) {
                                        if (selectedItems.contains(item)) selectedItems.remove(item)
                                        else selectedItems.add(item)
                                    } else {
                                        navController.navigate("${NavigationRoute.MAPSCREEN.path}/ImportedMarker/${item.id}/${record.id}/${record.categoryName}")
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
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                if (markerList.isNotEmpty()){
                    FloatingActionButton(
                        onClick = {
                            navController.navigate("${NavigationRoute.VIEWALLMAPSCREEN.path}/ImportedMarker/$recordId") {
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
                        if(record.categoryName?.isNotEmpty() == true){
                            navController.navigate("${NavigationRoute.MAPSCREEN.path}/ImportedMarker/${""}/${record.id}/${record.categoryName}") {
                                popUpTo(NavigationRoute.MARKERLISTSCREEN.path) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }


                    },
                    containerColor = Hexeef267,
                    contentColor = Hex222227,
                    modifier = Modifier.padding(end = 24.dp, bottom = 24.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Marker")
                }

            }
        }
        }

    }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImportedMarkerCard(
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




