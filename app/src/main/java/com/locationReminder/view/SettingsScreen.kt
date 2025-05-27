package com.locationReminder.view

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.core.view.WindowCompat
import com.locationReminder.reponseModel.LocationDetail
import com.locationReminder.reponseModel.SettingsData
import com.locationReminder.ui.theme.Hex222227
import com.locationReminder.ui.theme.Hexa0a0a0
import com.locationReminder.view.appNavigation.NavigationRoute
import com.locationReminder.viewModel.AddSettingsViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    addSettingsViewModel: AddSettingsViewModel
) {
    val selectedItems = remember { mutableStateListOf<LocationDetail>() }
    val isSelectionMode = selectedItems.isNotEmpty()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val showMenu = remember { mutableStateOf(false) }
    val context = LocalContext.current
    var showDistanceDialog by remember { mutableStateOf(false) }
    var showLocationUpdateIntervalDialog by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedUnit by remember { mutableStateOf("") }
    var selectedLocationUpdateInterval by remember { mutableStateOf("") }

    var entry by remember { mutableStateOf("") }
    var exit by remember { mutableStateOf("") }
    var max by remember { mutableStateOf("") }

    var entryDialog by remember { mutableStateOf("") }
    var exitDialog by remember { mutableStateOf("") }
    var maxDialog by remember { mutableStateOf("") }

    val listState = rememberLazyListState()
    val isScrolled by remember {
        derivedStateOf {
            val firstVisibleItem = listState.firstVisibleItemIndex
            val scrollOffset = listState.firstVisibleItemScrollOffset
            firstVisibleItem > 0 || scrollOffset > 0
        }
    }

    SideEffect {
        val window = (context as Activity).window
        window.statusBarColor = "#222227".toColorInt()
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false
    }

    val settingsRecord by addSettingsViewModel.getAllRecord().observeAsState()

    LaunchedEffect(settingsRecord) {
        if (settingsRecord == null) {
            addSettingsViewModel.insertRecord(
                SettingsData(
                    unit = "Meters/Kilometers",
                    locationUpdateInterval = "adaptable",
                    entryRadius = "100",
                    exitRadius = "100",
                    maximumRadius = "2000"
                )
            )
        } else {
            selectedUnit = settingsRecord!!.unit
            selectedLocationUpdateInterval = settingsRecord!!.locationUpdateInterval
            entry = settingsRecord!!.entryRadius
            exit = settingsRecord!!.exitRadius
            max = settingsRecord!!.maximumRadius
        }
    }


    LaunchedEffect(showDialog) {
        if (showDialog && settingsRecord != null) {
            entryDialog = entry
            exitDialog = exit
            maxDialog = max
        }
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        if (isSelectionMode) "${selectedItems.size} selected"
                        else "Settings",
                        color = Color.White
                    )
                },
                actions = {
                    if (isSelectionMode) {
                        IconButton(onClick = { /* Handle delete */ }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                        }
                    } else if (isScrolled) {
                        IconButton(onClick = { showMenu.value = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White)
                        }
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Hex222227,
                    scrolledContainerColor = Hex222227,
                    titleContentColor = Color.White
                ),
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Hex222227)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Hex222227)
                    .padding(horizontal = 20.dp)
            ) {
                SettingItem(title = "Units", subtitle = selectedUnit) {
                    showDistanceDialog = true
                }

                SettingItem(
                    title = "Location update interval",
                    subtitle = selectedLocationUpdateInterval
                ) {
                    showLocationUpdateIntervalDialog = true
                }

                SettingItem(
                    title = "Default alarm area",
                    subtitle = "$entry m, $exit m, $max m"
                ) {
                    showDialog = true
                }

                SettingItem(
                    title = "Add emergency contact",
                    subtitle = "Send a notification to a parent or relative"
                ) {
                    navController.navigate(NavigationRoute.CONTACTNUMBERSLISTCREEN.path)
                }
            }

            if (showDistanceDialog) {
                ShowDistanceDialog(
                    selectedSound = selectedUnit,
                    onDismiss = { showDistanceDialog = false },
                    onSoundSelect = {
                        selectedUnit = it
                        addSettingsViewModel.updateUnit(it)
                        showDistanceDialog = false
                    }
                )
            }

            if (showLocationUpdateIntervalDialog) {
                ShowLocationUpdateIntervalDialog(
                    selectedSound = selectedLocationUpdateInterval,
                    onDismiss = { showLocationUpdateIntervalDialog = false },
                    onSoundSelect = {
                        selectedLocationUpdateInterval = it
                        addSettingsViewModel.updateLocationUpdateInterval(it)
                        showLocationUpdateIntervalDialog = false
                    }
                )
            }

            if (showDialog) {
                GeoAlarmDialog(
                    entryValue = entryDialog,
                    exitValue = exitDialog,
                    maxValue = maxDialog,
                    onEntryChange = { entryDialog = it },
                    onExitChange = { exitDialog = it },
                    onMaxChange = { maxDialog = it },
                    onDismiss = { showDialog = false },
                    onConfirm = { entryVal, exitVal, maxVal ->
                        entry = entryVal
                        exit = exitVal
                        max = maxVal


                        addSettingsViewModel.updateEntryRadius(entryVal)
                        addSettingsViewModel.updateExitRadius(exitVal)
                        addSettingsViewModel.updateMaximumRadius(maxVal)
                        showDialog = false
                    }
                )
            }
        }
    }
}



@Composable
fun SettingItem(title: String, subtitle: String? = null, enabled: Boolean = true, onClick: (() -> Unit)? = null) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .alpha(if (enabled) 1f else 0.5f)
            .clickable { onClick?.invoke() }
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge.copy(color = Color.White))
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(color = Hexa0a0a0)
            )
        }
    }
}


@Composable
fun ShowDistanceDialog(
    selectedSound: String,
    onDismiss: () -> Unit,
    onSoundSelect: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Show Distance") },
        text = {
            Column {
                Spacer(modifier = Modifier.height(8.dp))

                val sounds = listOf("Meters/Kilometers", "Kilometers/Mile","Yard/Mile","Foot/Meters")

                sounds.forEach { sound ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { onSoundSelect(sound) }
                    ) {
                        RadioButton(
                            selected = selectedSound == sound,
                            onClick = { onSoundSelect(sound) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = sound)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
fun ShowLocationUpdateIntervalDialog(
    selectedSound: String,
    onDismiss: () -> Unit,
    onSoundSelect: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Location Update Interval") },
        text = {
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                val sounds = listOf("adaptable", "5 Seconds", "10 Seconds", "15 Seconds", "30 Seconds", "1 minute")

                sounds.forEach { sound ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { onSoundSelect(sound) }
                    ) {
                        RadioButton(
                            selected = selectedSound == sound,
                            onClick = { onSoundSelect(sound) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = sound)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeoAlarmDialog(
    entryValue: String,
    exitValue: String,
    maxValue: String,
    onEntryChange: (String) -> Unit,
    onExitChange: (String) -> Unit,
    onMaxChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    val isEntryValid = entryValue.toFloatOrNull() != null
    val isExitValid = exitValue.toFloatOrNull() != null
    val isMaxValid = maxValue.toFloatOrNull() != null

    val isFormValid = isEntryValid && isExitValid && isMaxValid

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Default Geo Alarm Perimeter",
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                // ENTRY RADIUS
                Text("Alarm on area entry")
                LabeledTextField(value = entryValue, onValueChange = onEntryChange)

                // EXIT RADIUS
                Text("Alarm on area exit")
                LabeledTextField(value = exitValue, onValueChange = onExitChange)

                // MAX RADIUS
                Text("Maximal perimeter value")
                LabeledTextField(value = maxValue, onValueChange = onMaxChange)
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (isFormValid) {
                        onConfirm(
                            entryValue,
                            exitValue,
                            maxValue
                        )
                    }
                },
                enabled = isFormValid
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabeledTextField(
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.wrapContentWidth()
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = Color.Gray,
                disabledIndicatorColor = Color.Transparent,
                containerColor = Color.Transparent
            ),
            modifier = Modifier.defaultMinSize(minWidth = 10.dp)
        )
        Text(
            text = "M",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

