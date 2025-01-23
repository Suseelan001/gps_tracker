package com.example.crmmainproject

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import com.example.crmmainproject.view.appNavigation.NavigationRoute
import com.example.crmmainproject.viewModel.SplashScreenVM
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    splashScreenVM: SplashScreenVM?,
    navHostController: NavHostController?) {
    SetStatusBarColor()

    LaunchedEffect(Unit) {

   delay(100L)
        redirectFromLoginScreen(navHostController,NavigationRoute.SPLASHSCREEN.path)



    }

    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Red,
                    Color.Yellow
                )
            )
        )) {

    }
}


fun redirectFromLoginScreen(navHostController: NavHostController?, currentStack:String) {
    navHostController?.navigate(NavigationRoute.LOGINHOME.path){
        popUpTo(currentStack){
            inclusive=true
        }
    }
}

@Composable
fun SetStatusBarColor() {
    val context = LocalContext.current
    val activity = context as Activity
    val window = activity.window
    @Suppress("DEPRECATION")
    window.statusBarColor = Color.Red.toArgb()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val insetsController = window.insetsController
        insetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    } else {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}