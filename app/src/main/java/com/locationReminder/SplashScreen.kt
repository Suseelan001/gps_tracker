package com.locationReminder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.locationReminder.view.appNavigation.NavigationRoute
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navHostController: NavHostController?) {

    LaunchedEffect(Unit) {

   delay(100L)
        redirectFromLoginScreen(navHostController,NavigationRoute.SPLASHSCREEN.path)



    }


}


fun redirectFromLoginScreen(navHostController: NavHostController?, currentStack:String) {
    navHostController?.navigate(NavigationRoute.HOMESCREEN.path){
        popUpTo(currentStack){
            inclusive=true
        }
    }
}

