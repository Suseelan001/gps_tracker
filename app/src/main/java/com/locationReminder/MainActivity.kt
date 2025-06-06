package com.locationReminder

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Window
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.rememberNavController
import com.locationReminder.view.appNavigation.NavigationRoute
import com.locationReminder.view.appNavigation.BottomBar
import com.locationReminder.view.appNavigation.NavigationGraph
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.graphics.toColorInt
import com.locationReminder.roomDatabase.dao.ContactDAO
import com.locationReminder.roomDatabase.dao.LocationDAO
import javax.inject.Inject
import androidx.core.net.toUri
import com.locationReminder.model.apiUtil.serviceModel.LocationDaoEntryPoint
import dagger.hilt.android.EntryPointAccessors


@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @Inject
    lateinit var contactDAO: ContactDAO
    @Inject lateinit var locationDao: LocationDAO
    private var isHomeScreenLaunchedFirstTime = true
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var locationSettingsLauncher: ActivityResultLauncher<Intent>


    private fun startLocationService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(Intent(this, LocationService::class.java))
        } else {
            startService(Intent(this, LocationService::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setStatusBarColor()

        val entryPoint = EntryPointAccessors.fromApplication(
            this.applicationContext,
            LocationDaoEntryPoint::class.java
        )
        locationDao = entryPoint.locationDao()

        locationSettingsLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (isLocationEnabled()) {
                checkBackgroundLocationManually()
                launchComposeUI()
            } else {
                Toast.makeText(this, "Location is still disabled. Please enable it to continue.", Toast.LENGTH_LONG).show()
            }
        }

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val locationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            val notificationsGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissions[Manifest.permission.POST_NOTIFICATIONS] == true
            } else {
                true
            }

            if (locationGranted && notificationsGranted) {
                if (isLocationEnabled()) {
                    checkBackgroundLocationManually()
                    launchComposeUI()
                } else {
                    promptEnableLocation()
                }
            } else {
                promptForBackgroundLocationPermission()
            }
        }

        requestAllPermissions()
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun promptEnableLocation() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        locationSettingsLauncher.launch(intent)
    }



    private fun launchComposeUI() {
        setContent {
            startLocationService()
            ComposeBaseFunction()
        }
    }

    @Composable
    private fun ComposeBaseFunction() {
        val navController = rememberNavController()
        val buttonsVisible = remember { mutableStateOf(false) }
        var isBottomBarVisible by remember { mutableStateOf(false) }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val route = destination.route
            buttonsVisible.value = listOf(
                NavigationRoute.HOMESCREEN.path,
                NavigationRoute.LEADSSCREEN.path,
                NavigationRoute.FOLLOWSUPSCREEN.path,
                NavigationRoute.SETTINGSSCREEN.path
            ).contains(route)
            if (route == NavigationRoute.HOMESCREEN.path) {
                isHomeScreenLaunchedFirstTime = false
            }
        }

        LaunchedEffect(Unit) {
            isBottomBarVisible = true
        }

        Scaffold(
            bottomBar = {
                if (isBottomBarVisible) {
                    BottomBar(navController, buttonsVisible)
                }
            }
        ) { paddingValues ->
            NavigationGraph(navController,paddingValues)
        }

    }

    private fun requestAllPermissions() {
        val permissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.SEND_SMS)
        }

        if (permissions.isNotEmpty()) {
            permissionLauncher.launch(permissions.toTypedArray())
        } else {
            if (isLocationEnabled()) {
                checkBackgroundLocationManually()
                launchComposeUI()
            } else {
                promptEnableLocation()
            }
        }
    }




    private fun checkBackgroundLocationManually() {
        if (!isBackgroundLocationGranted()) {
            promptForBackgroundLocationPermission()
        }
    }

    private fun isBackgroundLocationGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED
        } else true
    }

    private fun promptForBackgroundLocationPermission() {
        AlertDialog.Builder(this)
            .setTitle("Allow Background Location")
            .setMessage("To trigger geofence alarms even when the app is closed, please allow 'All the time' location in settings.")
            .setPositiveButton("Go to Settings") { _, _ -> openAppSettings(this) }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = "package:${context.packageName}".toUri()
        }
        context.startActivity(intent)
    }

    private fun setStatusBarColor() {
        window.statusBarColor = "#222227".toColorInt()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
    }





}







