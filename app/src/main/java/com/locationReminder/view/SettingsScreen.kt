package com.locationReminder.view

import android.widget.Toast
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.locationReminder.reponseModel.CommonResponseModel
import com.locationReminder.reponseModel.LocationDetail
import com.locationReminder.ui.theme.Hex222227
import com.locationReminder.ui.theme.Hexa0a0a0
import com.locationReminder.view.appNavigation.NavigationRoute
import com.locationReminder.viewModel.AddSettingsViewModel
import com.locationReminder.viewModel.SharedPreferenceVM




import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.locationReminder.R
import com.locationReminder.ui.theme.HexFFFFFF


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    addSettingsViewModel: AddSettingsViewModel,
    sharedPreferenceVM: SharedPreferenceVM
) {
    val selectedItems = remember { mutableStateListOf<LocationDetail>() }
    val isSelectionMode = selectedItems.isNotEmpty()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val context = LocalContext.current

    var showDistanceDialog by remember { mutableStateOf(false) }
    var showLocationUpdateIntervalDialog by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var logoutDialog by remember { mutableStateOf(false) }

    var selectedUnit by remember { mutableStateOf("") }
    var selectedLocationUpdateInterval by remember { mutableStateOf("") }

    var entry by remember { mutableStateOf("") }
    var exit by remember { mutableStateOf("") }
    var max by remember { mutableStateOf("") }

    var entryDialog by remember { mutableStateOf("") }
    var exitDialog by remember { mutableStateOf("") }
    var maxDialog by remember { mutableStateOf("") }

    val settingsRecord by addSettingsViewModel.getAllRecord().observeAsState()

    val updateResponse by addSettingsViewModel.updateResponse.observeAsState(initial = null)
    val successMessage by addSettingsViewModel.successMessage.observeAsState("")

    val listState = rememberLazyListState()
    val isScrolled by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
        }
    }

    LaunchedEffect(Unit) {
        addSettingsViewModel.ensureDefaultSettingsInserted()
    }

    LaunchedEffect(settingsRecord) {
        settingsRecord?.let {
            selectedUnit = it.unit
            selectedLocationUpdateInterval = it.locationUpdateInterval
            entry = it.entryRadius
            exit = it.exitRadius
            max = it.maximumRadius
        }
    }

    LaunchedEffect(showDialog) {
        if (showDialog && settingsRecord != null) {
            entryDialog = entry
            exitDialog = exit
            maxDialog = max
        }
    }

    LaunchedEffect(updateResponse) {
        updateResponse?.let {
            if (it.message == "appLogout") {
                Toast.makeText(context, "Successfully logged out", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
        }
    }

    if (successMessage == "Logout updated") {
        addSettingsViewModel.appLogout()
    } else if (successMessage == "Logout error") {
        logoutDialog = false
        addSettingsViewModel.clearSuccessMessage()
        Toast.makeText(context, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        if (isSelectionMode) "${selectedItems.size} selected" else "Settings",
                        color = Color.White
                    )
                },
                actions = {
                    if (isSelectionMode) {
                        IconButton(onClick = { /* Handle delete */ }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Hex222227)
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            if (sharedPreferenceVM.isUserLoggedIn()) {
                SettingItem(
                    painter = painterResource(id = R.drawable.ic_profile),
                    title = "Profile",
                    subtitle = sharedPreferenceVM.getUserName()
                ) {
                    navController.navigate(NavigationRoute.PROFILESCREEN.path)
                }
            }

            SettingItem(
                painter = painterResource(id = R.drawable.ic_unitsicon),
                title = "Units",
                subtitle = selectedUnit
            ) {
                showDistanceDialog = true
            }

            SettingItem(
                painter = painterResource(id = R.drawable.ic_location_update),
                title = "Location update interval",
                subtitle = selectedLocationUpdateInterval
            ) {
                showLocationUpdateIntervalDialog = true
            }

            SettingItem(
                painter = painterResource(id = R.drawable.ic_location_range),
                title = "Default alarm area",
                subtitle = "$entry m, $exit m, $max m"
            ) {
                showDialog = true
            }

            if (sharedPreferenceVM.isUserLoggedIn()) {
                SettingItem(
                    painter = painterResource(id = R.drawable.ic_contacts),
                    title = "Add emergency contact",
                    subtitle = "Send a notification to a parent or relative"
                ) {
                    navController.navigate(NavigationRoute.CONTACTNUMBERSLISTCREEN.path)
                }
            }

            // Logout or Login
            SettingItem(
                painter = if (sharedPreferenceVM.isUserLoggedIn()) painterResource(id = R.drawable.ic_logout) else painterResource(id = R.drawable.ic_login),
                title = if (sharedPreferenceVM.isUserLoggedIn()) "Log out" else "Login",
                subtitle = null
            ) {
                if (sharedPreferenceVM.isUserLoggedIn()) {
                    logoutDialog = true
                } else {
                    navController.navigate(NavigationRoute.LOGINHOME.path)
                }
            }
        }

        // Dialogs
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

        if (logoutDialog) {
            LogoutDialog(
                onDismiss = { logoutDialog = false },
                onConfirmLogout = {
                    addSettingsViewModel.callLogout("eq.${sharedPreferenceVM.getUserId()}")
                    logoutDialog = false
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

@Composable
fun SettingItem(
    painter: Painter,
    title: String,
    subtitle: String? = null,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick?.invoke() },
        colors = CardDefaults.cardColors(containerColor = Hex222227),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painter,
                contentDescription = null,
                tint = HexFFFFFF,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, color = Color.White, fontSize = 16.sp)
                subtitle?.let {
                    Text(text = it, color = Hexa0a0a0, fontSize = 13.sp)
                }
            }
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
        containerColor = Color(0xFF222227), // Background color
        title = {
            Text(
                text = "Show Distance",
                color = Color.White
            )
        },
        text = {
            Column {
                Spacer(modifier = Modifier.height(8.dp))

                val distance = listOf("Meters/Kilometers", "Kilometers/Mile", "Yard/Mile", "Foot/Meters")

                distance.forEach { sound ->
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
                            onClick = { onSoundSelect(sound) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color.White,
                                unselectedColor = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = sound, color = Color.White)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = Color.White)
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
        containerColor = Color(0xFF222227),
        title = {
            Text(
                "Location Update Interval",
                color = Color.White
            )
        },
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
                            onClick = { onSoundSelect(sound) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color.White,
                                unselectedColor = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = sound, color = Color.White)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = Color.White)
            }
        }
    )

}


@Composable
fun LogoutDialog(
    onDismiss: () -> Unit,
    onConfirmLogout: () -> Unit
)   {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF222227),
        title = {
            Text(
                text = "Logout",
                color = Color.White
            )
        },
        text = {
            Text(
                text = "Are you sure you want to logout?",
                color = Color.White
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmLogout) {
                Text("Yes", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.White)
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
        confirmButton = {
            TextButton(
                onClick = {
                    if (isFormValid) {
                        onConfirm(entryValue, exitValue, maxValue)
                    }
                },
                enabled = isFormValid
            ) {
                Text("OK", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = Color.White)
            }
        },
        title = {
            Text(
                text = "Alarm Perimeter",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        },
        text = {
            Column() {

                Text("Alarm on area entry", color = Color.White)
                Spacer(modifier = Modifier.height(6.dp))

                LabeledTextField(value = entryValue, onValueChange = onEntryChange)
                Spacer(modifier = Modifier.height(30.dp))



                Text("Alarm on area exit", color = Color.White)
                LabeledTextField(value = exitValue, onValueChange = onExitChange)

                Spacer(modifier = Modifier.height(30.dp))


                Text("Maximal perimeter value", color = Color.White)
                Spacer(modifier = Modifier.height(6.dp))

                LabeledTextField(value = maxValue, onValueChange = onMaxChange)
            }
        },
        containerColor = Color(0xFF222227)
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
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            cursorBrush = SolidColor(Color.White),
            modifier = Modifier
                .defaultMinSize(minWidth = 10.dp)
                .drawBehind {
                    val strokeWidth = 1.dp.toPx()
                    val y = size.height - strokeWidth / 2
                    drawLine(
                        color = Color.White,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = strokeWidth
                    )
                },
            decorationBox = { innerTextField ->
                Box(Modifier.padding(vertical = 8.dp)) {
                    innerTextField()
                }
            }
        )

        Text(
            text = "M",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )

    }
}

