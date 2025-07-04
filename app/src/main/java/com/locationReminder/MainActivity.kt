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
import android.provider.Telephony
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.locationReminder.view.appNavigation.NavigationRoute
import com.locationReminder.view.appNavigation.BottomBar
import com.locationReminder.view.appNavigation.NavigationGraph
import dagger.hilt.android.AndroidEntryPoint
import com.locationReminder.roomDatabase.dao.ContactDAO
import com.locationReminder.roomDatabase.dao.LocationDAO
import javax.inject.Inject
import com.locationReminder.model.apiUtil.serviceModel.LocationDaoEntryPoint
import com.locationReminder.reponseModel.ImportedCategoryNameResponseModel
import com.locationReminder.viewModel.AddImportedCategoryNameViewModel
import com.locationReminder.viewModel.SharedPreferenceVM
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import com.locationReminder.view.BannerAd

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val sharedPreferenceVM: SharedPreferenceVM by viewModels()
    @Inject
    lateinit var contactDAO: ContactDAO
    @Inject
    lateinit var locationDao: LocationDAO
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var backgroundLocationLauncher: ActivityResultLauncher<String>
    private lateinit var locationSettingsLauncher: ActivityResultLauncher<Intent>
    private val addImportedCategoryNameViewModel: AddImportedCategoryNameViewModel by viewModels()
    private var haveImportedList = false


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
       handleDeepLink(intent)

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
                Toast.makeText(
                    this,
                    "Location is still disabled. Please enable it to continue.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false






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
            }
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            backgroundLocationLauncher = registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) {
                    Toast.makeText(this, "Background location granted", Toast.LENGTH_SHORT).show()
                    checkOverlayPermission()
                } else {
                    Toast.makeText(this, "Background location denied", Toast.LENGTH_SHORT).show()
                    checkOverlayPermission()
                }
            }
        }

        requestAllPermissions()
        ensureDefaultSmsApp(this)
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
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

    private fun handleDeepLink(intent: Intent?) {
        if (sharedPreferenceVM.isUserLoggedIn()) {
            val data = intent?.data
            if (data != null && data.scheme == "myapp" && data.host == "imported_marker") {
                val categoryFolderName = data.getQueryParameter("categoryFolderName")
                val userId = data.getQueryParameter("user_id")
                val categoryId = data.getQueryParameter("categoryId")
                if (userId == sharedPreferenceVM.getUserId()) {
                    Toast.makeText(this, "Import other user detail", Toast.LENGTH_SHORT).show()
                    intent.data = null
                    return
                }

                if (categoryFolderName != null && userId != null&& categoryId != null) {
                    lifecycleScope.launch {
                        val existingRecord = addImportedCategoryNameViewModel
                            .isCategoryAlreadyExists(categoryFolderName, userId)
                        if (existingRecord == null) {
                            val newRecord = ImportedCategoryNameResponseModel(
                                id = categoryId.toInt(),
                                categoryName = categoryFolderName,
                                userId = userId,
                                firstTimeImport = true,
                                showImport=false
                            )
                            addImportedCategoryNameViewModel.insertRecord(newRecord)

                        } else {
                            val updatedRecord = existingRecord.copy(firstTimeImport = true)
                            addImportedCategoryNameViewModel.updateRecord(updatedRecord)
                        }

                        haveImportedList = true

                    }
                    intent.data = null

                }

            }
        }
    }

    @Composable
    private fun ComposeBaseFunction() {
        val navController = rememberNavController()
        val buttonsVisible = remember { mutableStateOf(false) }
        var isBottomBarVisible by remember { mutableStateOf(false) }
        var navGraphInitialized by remember { mutableStateOf(false) }

        val startDestination = if (haveImportedList) {
            sharedPreferenceVM.setImportList(true)
            NavigationRoute.FOLLOWSUPSCREEN.path
        } else {
            sharedPreferenceVM.setImportList(false)
            NavigationRoute.HOMESCREEN.path
        }


        LaunchedEffect(navGraphInitialized, haveImportedList) {
            if (navGraphInitialized) {
                navController.navigate(startDestination) {
                    popUpTo(0) { inclusive = true }
                }
                isBottomBarVisible = true
            }
        }

        LaunchedEffect(Unit) {
            navController.addOnDestinationChangedListener { _, destination, _ ->
                val route = destination.route
                buttonsVisible.value = listOf(
                    NavigationRoute.HOMESCREEN.path,
                    NavigationRoute.LEADSSCREEN.path,
                    NavigationRoute.FOLLOWSUPSCREEN.path,
                    NavigationRoute.SETTINGSSCREEN.path
                ).contains(route)

            }
        }
        Scaffold(
            bottomBar = {
                if (buttonsVisible.value) {

                        BottomBar(
                            navController = navController,
                            state = buttonsVisible,
                            modifier = Modifier.fillMaxWidth(),
                            sharedPreferenceVM = sharedPreferenceVM,
                            context = this@MainActivity
                        )
                   // }
                }
            }
        ) { padding ->
            NavigationGraph(navController, padding)
            LaunchedEffect(Unit) {
                navGraphInitialized = true
            }
        }


    }


    private fun requestAllPermissions() {
        val mandatoryPermissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            mandatoryPermissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            mandatoryPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                mandatoryPermissions.add(Manifest.permission.SEND_SMS)
            }
        }


        if (mandatoryPermissions.isNotEmpty()) {
            permissionLauncher.launch(mandatoryPermissions.toTypedArray())
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
        }else {
            checkOverlayPermission()
        }
    }

    private fun isBackgroundLocationGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) ==
                    PackageManager.PERMISSION_GRANTED
        } else true
    }

    private fun promptForBackgroundLocationPermission() {
        AlertDialog.Builder(this)
            .setTitle("Allow Background Location")
            .setMessage("To trigger geofence alarms even when the app is closed, please allow 'All the time' location in settings.")
            .setPositiveButton("Go to Settings") { _, _ -> openAppSettings() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun openAppSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            backgroundLocationLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        } else {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = "package:$packageName".toUri()
            }
            startActivity(intent)
        }
    }


    private fun checkOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            showOverlayPermissionDialog()
        }
    }


    private fun showOverlayPermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle("Allow Overlay Permission")
            .setMessage("This permission is required to display alarms and alerts over other apps.")
            .setPositiveButton("Grant") { _, _ ->
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    "package:$packageName".toUri()
                )
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }



}



fun ensureDefaultSmsApp(context: Context) {
    val myPackageName = context.packageName
    val defaultSmsPackage = Telephony.Sms.getDefaultSmsPackage(context)

    if (defaultSmsPackage != myPackageName) {
        val intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, myPackageName)
        context.startActivity(intent)
    }
}





