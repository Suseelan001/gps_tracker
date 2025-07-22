package com.locationReminder.view

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
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
import com.locationReminder.R
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.locationReminder.reponseModel.MarkerUpdateRequest
import com.locationReminder.ui.theme.Hex222227
import com.locationReminder.ui.theme.Hex36374a
import com.locationReminder.ui.theme.Hexeef267
import com.locationReminder.ui.theme.RobotoMediumWithHexFFFFFF18sp
import com.locationReminder.ui.theme.RobotoRegularWithHexFFFFFF14sp
import com.locationReminder.ui.theme.RobotoRegularWithHexHex80808016sp
import com.locationReminder.ui.theme.RobotoRegularWithHexHexeef26714sp
import com.locationReminder.view.appNavigation.NavigationRoute
import com.locationReminder.viewModel.AddLocationViewModel
import com.locationReminder.viewModel.SharedPreferenceVM
import com.locationReminder.viewModel.SuggestionsViewModel
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SuggestMarkerListScreen(
    navController: NavHostController,
    suggestionsViewModel: SuggestionsViewModel,
    sharedPreferenceVM: SharedPreferenceVM,
    categoryId: String,
    addLocationViewModel: AddLocationViewModel
) {
    LaunchedEffect(Unit) {
        suggestionsViewModel.getSuggestionsList("eq.$categoryId")
    }

    val successMessage by addLocationViewModel.successMessage.observeAsState("")



    val markerList by suggestionsViewModel.suggestionsList.observeAsState(emptyList())
    val isLoading by suggestionsViewModel.loading.observeAsState(true)

    if (successMessage == "Record Imported") {
        suggestionsViewModel._loading.postValue(false)
        addLocationViewModel.clearSuccessMessage()

        navController.navigate(NavigationRoute.FOLLOWSUPSCREEN.path) {
            popUpTo(NavigationRoute.FOLLOWSUPSCREEN.path) {
                inclusive = true
            }
        }

    }

    val record by suggestionsViewModel.categoryFolderResponse.observeAsState()
    record?.let {
        val updatedList = remember(markerList) {
            markerList.map { marker ->
                MarkerUpdateRequest(
                    id = marker.id.toString(),
                    title = marker.title.toString(),
                    address = marker.address.toString(),
                    category_id = record!!.id.toString(),
                    category_name = marker.categoryName,
                    currentStatus = true,
                    entryType = "Marker",
                    lat = marker.lat!!,
                    lng = marker.lng!!,
                    radius = 100F,
                    sendNotification = true,
                    vibration = false,
                    user_id = sharedPreferenceVM.getUserId()
                )
            }
        }

        LaunchedEffect(updatedList) {
            if (updatedList.isNotEmpty()) {
                addLocationViewModel.updateMarkers(updatedList)
            }
        }
    }


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
                            text = "Suggestions",
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        if (markerList.isNotEmpty()) {
                            FloatingActionButton(
                                onClick = {
                                    navController.navigate("${NavigationRoute.VIEWALLSUGGESTIONSMAPSCREEN.path}/$categoryId") {
                                        popUpTo(NavigationRoute.VIEWALLSUGGESTIONSMAPSCREEN.path) {
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                    }
                                },
                                containerColor = Hexeef267,
                                contentColor = Hex222227,
                                modifier = Modifier.padding(end = 12.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.viewall),
                                    contentDescription = "View All"
                                )
                            }


                            FloatingActionButton(
                                onClick = {
                                    suggestionsViewModel.addCategoryList(markerList[0].categoryName.toString(), sharedPreferenceVM.getUserId().toString(), "", markerList[0].areaName.toString())
                                },
                                containerColor = Hexeef267,
                                contentColor = Hex222227,
                                modifier = Modifier.padding(end = 12.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_download),
                                    contentDescription = "Download",
                                    tint = Color.Black
                                )
                            }
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
            if (!sharedPreferenceVM.isUserLoggedIn()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Please log in to view suggestions.",
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
            } else {
                when {
                    isLoading -> {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                        ) {
                            items(5) { MarkerShimmerCardPlaceholder() }
                        }
                    }

                    markerList.isEmpty() -> {
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
                        }
                    }

                    else -> {
                        val sortedList = if (currentLatitude.doubleValue != 0.0 && currentLongitude.doubleValue != 0.0) {
                            markerList.sortedBy {
                                val lat = it.lat ?: 0.0
                                val lng = it.lng ?: 0.0
                                val results = FloatArray(1)
                                Location.distanceBetween(currentLatitude.doubleValue, currentLongitude.doubleValue, lat, lng, results)
                                results[0]
                            }
                        } else {
                            markerList
                        }
                        val adFrequency = 4

                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 1.dp)
                        ) {
                            itemsIndexed(sortedList) { actualIndex, item ->
                                val lat = item.lat ?: 0.0
                                val lng = item.lng ?: 0.0

                                // Main content card
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                        .padding(bottom = 16.dp)
                                        .combinedClickable(
                                            onClick = {
                                                navController.navigate("${NavigationRoute.SUGGESTIONSMAPSCREEN.path}/${item.id}")
                                            }
                                        ),
                                    colors = CardDefaults.cardColors(containerColor = Hex36374a)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.marker),
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
                                                    text = item.title.toString(),
                                                    style = RobotoMediumWithHexFFFFFF18sp,
                                                    modifier = Modifier.weight(1f),
                                                    maxLines = 2,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Text(
                                                text = item.address.toString(),
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
                                                            lat, lng
                                                        ),
                                                        style = RobotoRegularWithHexHexeef26714sp,
                                                    )
                                                }

                                                Spacer(modifier = Modifier.weight(1f))

                                                Text(
                                                    text = String.format(Locale.US, "%.5f, %.5f", lat, lng),
                                                    style = RobotoRegularWithHexFFFFFF14sp,
                                                    modifier = Modifier.padding(end = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                }

                                // Show banner ad every 4 items or at the end
                                if ((actualIndex + 1) % adFrequency == 0 || actualIndex == sortedList.lastIndex)
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



}





