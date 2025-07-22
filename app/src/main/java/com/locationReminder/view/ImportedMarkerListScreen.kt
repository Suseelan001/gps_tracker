package com.locationReminder.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
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

            addLocationViewModel.getImportedMarkerList("eq.${record.id}","eq.${record.userId}")
           addImportedCategoryNameViewModel.updateRecordStatus(record.id,false)
        }
    }
    val getAccountList by addLocationViewModel.getMarkerListByFolder("${record.id}","ImportedMarker").observeAsState(emptyList())
    val markerList = getAccountList.filter { it.entryType.equals("ImportedMarker", ignoreCase = true) }


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



    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()

                    ) {
                        Text(
                            text = if (isSelectionMode) "${selectedItems.size} selected" else "On Marker\n Entry List",
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.weight(1f))

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
                                modifier = Modifier.padding(end = 24.dp)
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
                            modifier = Modifier.padding(end = 24.dp)
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
                .background(Hex222227)
                .padding(top = paddingValues.calculateTopPadding())
                .padding(bottom = 50.dp)


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
                    val itemId = recordId.removePrefix("eq.").toInt()
                    addImportedCategoryNameViewModel.updateShowImportStatus(itemId, true)

                    val adFrequency = 4

                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 1.dp)
                    ) {
                        itemsIndexed(markerList) { actualIndex, item ->
                            // Main item card
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

                            // Inject ad after every 4 items or at the end
                            if ((actualIndex + 1) % adFrequency == 0 || actualIndex == markerList.size - 1)
                            {
                                var nativeAd by remember { mutableStateOf<NativeAd?>(null) }

                                AndroidView(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight(),
                                    factory = { context ->
                                        NativeAdView(context).apply {
                                            val adView = LayoutInflater.from(context).inflate(R.layout.native_ad_layout, this) as NativeAdView

                                            val adLoader = AdLoader.Builder(context, "ca-app-pub-6069496300926751/5719311055")
                                                .forNativeAd { ad ->
                                                    nativeAd = ad

                                                    val headlineView = adView.findViewById<TextView>(R.id.ad_headline)
                                                    val bodyView = adView.findViewById<TextView>(R.id.ad_body)
                                                    val mediaView = adView.findViewById<MediaView>(R.id.ad_media)

                                                    headlineView.text = ad.headline
                                                    bodyView.text = ad.body ?: ""
                                                    bodyView.visibility = if (ad.body != null) View.VISIBLE else View.GONE

                                                    adView.headlineView = headlineView
                                                    adView.bodyView = bodyView
                                                    adView.mediaView = mediaView

                                                    adView.setNativeAd(ad)
                                                }
                                                .build()

                                            adLoader.loadAd(AdRequest.Builder().build())
                                        }
                                    },
                                    update = { view ->
                                        nativeAd?.let { view.setNativeAd(it) }
                                    }
                                )

                            }

                        }
                    }

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




