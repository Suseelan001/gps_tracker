package com.locationReminder.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.locationReminder.R
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.locationReminder.ui.theme.Hex36374a
import com.locationReminder.ui.theme.Hexeef267
import com.locationReminder.view.appNavigation.NavigationRoute
import com.locationReminder.viewModel.AddFolderNameViewModel
import com.locationReminder.viewModel.SharedPreferenceVM
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.sp
import com.locationReminder.reponseModel.MarkerUpdateRequest
import com.locationReminder.ui.theme.Hex222227
import com.locationReminder.viewModel.AddImportedCategoryNameViewModel
import com.locationReminder.viewModel.AddLocationViewModel
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkerListBaseScreen(
    navController: NavHostController,
    addFolderNameViewModel: AddFolderNameViewModel,
    sharedPreferenceVM: SharedPreferenceVM,
    addImportedCategoryNameViewModel: AddImportedCategoryNameViewModel,
    addLocationViewModel: AddLocationViewModel
) {
    val selectedTabIndex = rememberSaveable {
        mutableIntStateOf(
            if (sharedPreferenceVM.getImportList() == true) 1 else 0
        )
    }
    LaunchedEffect(Unit) {
        sharedPreferenceVM.setImportList(false)
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
                            text = "Marker Folders",
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.weight(1f))




                        if (sharedPreferenceVM.isUserLoggedIn()) {

                            FloatingActionButton(
                                onClick = {
                                    navController.navigate(NavigationRoute.SUGGESTFOLDERLISTSCREEN.path) {
                                        popUpTo(NavigationRoute.SUGGESTFOLDERLISTSCREEN.path) {
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                    }
                                },
                                containerColor = Hexeef267,
                                contentColor = Hex222227,
                                modifier = Modifier.padding(end = 24.dp, bottom = 12.dp)
                            ) {
                                Icon(Icons.Default.Search, contentDescription = "Search")
                            }

                            if (selectedTabIndex.intValue == 0) {
                                FloatingActionButton(
                                    onClick = {
                                        navController.navigate("${NavigationRoute.ADDFOLDERNAMESCREEN.path}/${""}/${""}/${""}") {
                                            popUpTo(NavigationRoute.FOLLOWSUPSCREEN.path) {
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

                        }


                    }
                },


                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Color(0xFF222227),
                    titleContentColor = Color.White
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


        ){
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
            } else {
                TabBarScreen(
                    navController,
                    addFolderNameViewModel,
                    sharedPreferenceVM,
                    addImportedCategoryNameViewModel,
                    addLocationViewModel,
                    selectedTabIndex
                )
            }
        }
    }
}


@Composable
fun TabBarScreen(
    navController: NavHostController,
    addFolderNameViewModel: AddFolderNameViewModel,
    sharedPreferenceVM: SharedPreferenceVM,
    addImportedCategoryNameViewModel: AddImportedCategoryNameViewModel,
    addLocationViewModel: AddLocationViewModel,
    selectedTabIndex: MutableState<Int>
) {
    val tabs = listOf("Own", "Imported")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTabIndex.value,
            containerColor = Color(0xFF36374a),
            contentColor = Color(0xFF36374a),
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex.value]),
                    color = Hexeef267
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex.value == index,
                    onClick = { selectedTabIndex.value = index },
                    text = {
                        Text(
                            title,
                            color = Color.White
                        )
                    }
                )
            }
        }

        when (selectedTabIndex.value) {
            0 -> OwnTabContent(navController, addFolderNameViewModel, sharedPreferenceVM)
            1 -> ImportedTabContent(
                navController,
                addImportedCategoryNameViewModel,
                addFolderNameViewModel,
                sharedPreferenceVM,
                addLocationViewModel,
                selectedTabIndex
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OwnTabContent(
    navController: NavHostController,
    addFolderNameViewModel: AddFolderNameViewModel,
    sharedPreferenceVM: SharedPreferenceVM
) {
    val context = LocalContext.current
    val userId = sharedPreferenceVM.getUserId().toString()
    val isLoading by addFolderNameViewModel.loading.observeAsState(true)
    val successMessage by addFolderNameViewModel.successMessage.observeAsState("")
    val allCategoryList by addFolderNameViewModel.getAllRecord().observeAsState(emptyList())

    LaunchedEffect(Unit) {
        addFolderNameViewModel.getCategoryFolderList("eq.$userId")
    }

    LaunchedEffect(successMessage) {
        if (successMessage == "Record deleted") {
            addFolderNameViewModel.getCategoryFolderList("eq.$userId")
            addFolderNameViewModel.clearSuccessMessage()
        }
    }

    val categoryList = if (successMessage == "Get Category Folder") {
        allCategoryList.distinctBy { it.categoryName }
    } else {
        emptyList()
    }

    if (isLoading) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 1.dp, top = 15.dp)
        ) {
            items(4) {
                ShimmerCardPlaceholder()
            }
        }
    } else {
        Box(
            Modifier
                .fillMaxSize()
                .padding(top = 15.dp),
            contentAlignment = Alignment.Center
        ) {
            if (categoryList.isEmpty()) {
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
                        text = "Tap the '+' button to add a new folder",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

            } else

            {val adFrequency = 4

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(categoryList) { actualIndex, item ->
                        val toggleState = remember { mutableStateOf(item.currentStatus ?: false) }

                        // Main item card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .padding(bottom = 16.dp)
                                .combinedClickable(
                                    onClick = {
                                        val categoryTitle = item.categoryName ?: ""
                                        val id = item.id ?: ""
                                        navController.navigate("${NavigationRoute.MARKERLISTSCREEN.path}/$categoryTitle/$id")
                                    },
                                    onLongClick = {}
                                ),
                            colors = CardDefaults.cardColors(
                                containerColor = Hex36374a
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.marker),
                                    contentDescription = "Item Image",
                                    modifier = Modifier.size(70.dp),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = item.categoryName.toString(),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.White,
                                    )

                                    val textValue = if (!item.userName.isNullOrEmpty()) {
                                        "By ${item.userName}"
                                    } else {
                                        item.areaName ?: ""
                                    }

                                    Text(
                                        text = textValue,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontStyle = FontStyle.Italic,
                                            fontSize = 12.sp
                                        ),
                                        color = Color.Gray,
                                    )
                                }

                                Row(
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    IconButton(onClick = {
                                        addFolderNameViewModel.deleteCategoryList("eq.${item.id}", "eq.${item.userId}")
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = Color.White
                                        )
                                    }

                                    IconButton(onClick = {
                                        navController.navigate("${NavigationRoute.ADDFOLDERNAMESCREEN.path}/${item.id}/${item.categoryName}/${""}")
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit",
                                            tint = Color.White
                                        )
                                    }

                                    IconButton(onClick = {
                                        shareDeepLink(
                                            context,
                                            item.categoryName.toString(),
                                            userId,
                                            item.id.toString(),
                                            sharedPreferenceVM.getUserName().toString()
                                        )
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Share,
                                            contentDescription = "Share",
                                            tint = Color.White
                                        )
                                    }
                                }

                                Switch(
                                    checked = toggleState.value,
                                    onCheckedChange = null,
                                    modifier = Modifier
                                        .scale(0.75f)
                                        .clickable {
                                            toggleState.value = !toggleState.value
                                            addFolderNameViewModel.updateCategoryListStatus(
                                                "eq.${item.id}",
                                                "eq.${item.userId}",
                                                toggleState.value
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
                        }

                        // Inject BannerAd after every 4 items or at end
                        if ((actualIndex + 1) % adFrequency == 0 || actualIndex == categoryList.size - 1)
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

                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }


            }
        }

    }
}

@Composable
fun ShimmerCardPlaceholder() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Hex36374a)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .customShimmer()
                    .background(Color.Gray, shape = RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.7f),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Box(
                    modifier = Modifier
                        .height(20.dp)
                        .fillMaxWidth()
                        .customShimmer()
                        .background(Color.Gray, shape = RoundedCornerShape(4.dp))
                )
                Box(
                    modifier = Modifier
                        .height(20.dp)
                        .fillMaxWidth(0.5f)
                        .customShimmer()
                        .background(Color.Gray, shape = RoundedCornerShape(4.dp))
                )
            }
        }
    }
}

@Composable
fun Modifier.customShimmer(
    baseColor: Color = Color.Gray.copy(alpha = 0.3f),
    highlightColor: Color = Color.White.copy(alpha = 0.6f),
): Modifier {
    val transition = rememberInfiniteTransition()
    val shimmerTranslateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    return this.drawWithCache {
        val gradientWidth = 200f
        val brush = Brush.linearGradient(
            colors = listOf(baseColor, highlightColor, baseColor),
            start = Offset(shimmerTranslateAnim.value - gradientWidth, 0f),
            end = Offset(shimmerTranslateAnim.value, size.height)
        )
        onDrawWithContent {
            drawContent()
            drawRect(brush = brush, blendMode = BlendMode.SrcIn)
        }
    }
}


fun shareDeepLink(
    context: Context,
    categoryFolderName: String,
    userId: String,
    categoryId: String,
    userName: String
) {
    val shareText =
        "Hi! View my shared marker:\nhttps://gpsalarm.eronpos.com/?" +
                "FN=${Uri.encode(categoryFolderName)}" +
                "&UID=${Uri.encode(userId)}" +
                "&CID=${Uri.encode(categoryId)}" +
                "&UN=${Uri.encode(userName)}"


    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, shareText)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, "Share via")
    context.startActivity(shareIntent)
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImportedTabContent(
    navController: NavHostController,
    addImportedCategoryNameViewModel: AddImportedCategoryNameViewModel,
    addFolderNameViewModel: AddFolderNameViewModel,
    sharedPreferenceVM: SharedPreferenceVM,
    addLocationViewModel: AddLocationViewModel,
    selectedTabIndex: MutableState<Int>


) {
    val context = LocalContext.current
    var clickedId by remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(false) }
    val errorMessage by addFolderNameViewModel.errorMessage.observeAsState("")

    if (errorMessage == "Record already exist") {
        Toast.makeText(context, "Folder name already exists", Toast.LENGTH_LONG).show()
        addFolderNameViewModel.clearErrorMessage()
        isLoading.value = false

    }

    val record by addFolderNameViewModel.categoryFolderResponse.observeAsState()
    record?.let {
        val getAccountList by addLocationViewModel
            .getMarkerListByFolder(clickedId, "ImportedMarker")
            .observeAsState(emptyList())

        val markerList = getAccountList.filter { marker ->
            marker.entryType.equals("ImportedMarker", ignoreCase = true)
        }


        val updatedList = remember(markerList) {
            markerList.map { marker ->
                MarkerUpdateRequest(
                    id = addLocationViewModel.generate6DigitUniqueId(),
                    title = marker.title,
                    address = marker.address,
                    category_id = record!!.id.toString(),
                    category_name = marker.category_name,
                    currentStatus = marker.currentStatus,
                    entryType = "Marker",
                    lat = marker.lat,
                    lng = marker.lng,
                    radius = marker.radius,
                    sendNotification = true,
                    vibration = false,
                    user_id = sharedPreferenceVM.getUserId()
                )
            }
        }
        println("CHECK_TAG_UPDATEDLIST_ " + Gson().toJson(updatedList))
        LaunchedEffect(updatedList) {
            if (updatedList.isNotEmpty()) {
                addLocationViewModel.updateMarkers(updatedList)
            }
        }
    }


    val successMessage by addLocationViewModel.successMessage.observeAsState("")

    if (successMessage == "Record Imported") {
        isLoading.value = false

        addImportedCategoryNameViewModel.deleteSingleRecord(clickedId.toInt())
        addImportedCategoryNameViewModel.deleteMarkerListByFolder(clickedId.toString(), "ImportedMarker")

        clickedId = ""
        selectedTabIndex.value = 0

        addLocationViewModel.clearSuccessMessage()
    }


    val allRecords by addImportedCategoryNameViewModel.getAllRecord().observeAsState(emptyList())

    if (isLoading.value) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 1.dp, top = 15.dp)
        ) {
            items(4) {
                ShimmerCardPlaceholder()
            }
        }
    } else {
        Box(
            Modifier
                .fillMaxSize()
                .padding(top = 15.dp), contentAlignment = Alignment.Center
        ) {
            if (allRecords.isEmpty()) {
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

            } else {
                val adFrequency = 4

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(allRecords) { actualIndex, record ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .padding(bottom = 16.dp)
                                .clickable {
                                    navController.navigate("${NavigationRoute.IMPORTEDMARKERLISTSCREEN.path}/${record.id}")
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = Hex36374a
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.marker),
                                    contentDescription = "Item Image",
                                    modifier = Modifier.size(70.dp),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = record.categoryName.toString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    modifier = Modifier.weight(1f)
                                )

                                Row(
                                    modifier = Modifier.wrapContentWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    if (record.showImport == true) {
                                        IconButton(onClick = {
                                            isLoading.value = true
                                            clickedId = record.id.toString()
                                            addFolderNameViewModel.addCategoryList(
                                                record.categoryName.toString(),
                                                sharedPreferenceVM.getUserId().toString(),
                                                record.userName.toString()
                                            )
                                        }) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_download),
                                                contentDescription = "Download",
                                                tint = Color.White
                                            )
                                        }
                                    }

                                    IconButton(onClick = {
                                        navController.navigate("${NavigationRoute.ADDFOLDERNAMESCREEN.path}/${record.id}/${record.categoryName}/ImportedMarker")
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit",
                                            tint = Color.White
                                        )
                                    }

                                    IconButton(onClick = {
                                        addImportedCategoryNameViewModel.deleteSingleRecord(record.id)
                                        addImportedCategoryNameViewModel.deleteMarkerListByFolder(
                                            record.id.toString(),
                                            "ImportedMarker"
                                        )
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                        }

                        // Inject ad every 4 items or at the end
                        if ((actualIndex + 1) % adFrequency == 0 || actualIndex == allRecords.size - 1) {
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

                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }


            }
        }

    }
}
