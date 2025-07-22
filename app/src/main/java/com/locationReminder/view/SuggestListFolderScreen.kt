package com.locationReminder.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.locationReminder.ui.theme.Hex222227
import com.locationReminder.ui.theme.Hex36374a
import com.locationReminder.viewModel.SuggestionsViewModel
import com.locationReminder.R
import com.locationReminder.view.appNavigation.NavigationRoute
import com.locationReminder.viewModel.SharedPreferenceVM
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SuggestListFolderScreen(
    navController: NavHostController,
    suggestionsViewModel: SuggestionsViewModel,
    sharedPreferenceVM: SharedPreferenceVM
) {
    LaunchedEffect(Unit) {
        val rawAreaName = sharedPreferenceVM.getArea()
        val areaNameFilter = "ilike.${rawAreaName.lowercase()}"
        val categoryNameFilter = "ilike.%%"
        suggestionsViewModel.getAreaId(areaNameFilter, categoryNameFilter)
    }

    val getAccountList by suggestionsViewModel.suggestionsCategoryList.observeAsState(emptyList())
    val isLoading by suggestionsViewModel.loading.observeAsState(true)
    val searchQuery = rememberSaveable { mutableStateOf("") }
    val isFocused = rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val collapsedTopAppBarState = rememberTopAppBarState(
        initialHeightOffsetLimit = -Float.MAX_VALUE,
        initialHeightOffset = 0f,
        initialContentOffset = 0f
    )

    val defaultTopAppBarState = rememberTopAppBarState()

    val scrollBehavior = if (isFocused.value) {
        TopAppBarDefaults.enterAlwaysScrollBehavior(collapsedTopAppBarState)
    } else {
        TopAppBarDefaults.enterAlwaysScrollBehavior(defaultTopAppBarState)
    }




    LaunchedEffect(isFocused.value) {
        if (isFocused.value) {
            snapshotFlow { collapsedTopAppBarState.heightOffsetLimit }
                .first { it != 0f && it != Float.NEGATIVE_INFINITY }
            collapsedTopAppBarState.heightOffset = collapsedTopAppBarState.heightOffsetLimit
        }
    }




    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                focusManager.clearFocus()
            },


        topBar = {
            LibraryTopBar(scrollBehavior)
        }


    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Hex222227)
                .padding(top = paddingValues.calculateTopPadding())
                .padding(bottom = 50.dp)


        ) {


            var clickedId by remember { mutableStateOf("") }

            val successMessage by suggestionsViewModel.successMessage.observeAsState("")

            if (successMessage == "Record Imported") {
                clickedId = ""
                suggestionsViewModel.clearSuccessMessage()
            }



            LaunchedEffect(searchQuery.value) {
                if (searchQuery.value.isNotEmpty()) {
                    delay(500)
                    val rawAreaName = sharedPreferenceVM.getArea()
                    val areaNameFilter = "ilike.${rawAreaName.lowercase()}"
                    val searchKey = searchQuery.value.trim().lowercase()
                    val categoryNameFilter = "ilike.%$searchKey%"
                    suggestionsViewModel.getAreaId(areaNameFilter, categoryNameFilter)
                }
            }

            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, start = 16.dp, end = 16.dp)
                        .shadow(8.dp, shape = RoundedCornerShape(32.dp))
                        .background(Color.Black, shape = RoundedCornerShape(32.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(modifier = Modifier.weight(1f)) {

                                TextField(
                                    value = searchQuery.value,
                                    onValueChange = {
                                        searchQuery.value = it
                                    },
                                    placeholder = {
                                        Text("Search here", color = Color.LightGray)
                                    },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                        .onFocusChanged { focusState ->
                                            isFocused.value = focusState.isFocused
                                        },
                                    colors = TextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        cursorColor = Color.White,
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    ),
                                    leadingIcon = {
                                        IconButton(onClick = {}) {
                                            Icon(
                                                imageVector = Icons.Default.Search,
                                                contentDescription = "Search",
                                                tint = Color.White
                                            )
                                        }
                                    },
                                    trailingIcon = {
                                        if (searchQuery.value.isNotEmpty()) {
                                            IconButton(onClick = {
                                                searchQuery.value = ""
                                            }) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.cancel),
                                                    contentDescription = "cancel",
                                                    tint = Color.White
                                                )
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                when {
                    isLoading -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                        ) {
                            items(4) {
                                ShimmerCardPlaceholder()
                            }
                        }
                    }

                    getAccountList.isEmpty() -> {
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
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                        ) {
                            items(getAccountList.size) { index ->
                                val record = getAccountList[index]

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp)
                                        .padding(bottom = 16.dp)
                                        .clickable {
                                            navController.navigate("${NavigationRoute.SUGGESTMARKERLISTSCREEN.path}/${record.id}")
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

                                        Column(
                                            Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = record.categoryName.toString(),
                                                style = MaterialTheme.typography.titleMedium,
                                                color = Color.White,
                                            )


                                            Text(
                                                text = record.areaName.toString(),
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontStyle = FontStyle.Italic,
                                                    fontSize = 12.sp
                                                ),
                                                color = Color.Gray,
                                            )

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

    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LibraryTopBar(scrollBehavior: TopAppBarScrollBehavior) =
    LargeTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()

            ) {
                Text(
                    text = "List of Suggestions",
                    color = Color.White
                )

            }
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = Hex222227,
            scrolledContainerColor = Hex222227,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        scrollBehavior = scrollBehavior
    )



