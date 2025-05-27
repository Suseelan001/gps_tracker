package com.locationReminder.view.appNavigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.locationReminder.view.AddContactNumberScreen
import com.locationReminder.SplashScreen
import com.locationReminder.view.OnEntryListScreen
import com.locationReminder.view.OnExitListScreen
import com.locationReminder.view.MapScreen
import com.locationReminder.view.MarkerListScreen
import com.locationReminder.view.ViewAllMapScreen
import com.locationReminder.view.ContactNumberListScreen
import com.locationReminder.view.SettingsScreen
import com.locationReminder.viewModel.AddLocationViewModel
import com.locationReminder.view.LoginScreen
import com.locationReminder.view.SignUpScreen
import com.locationReminder.viewModel.AddContactViewModel
import com.locationReminder.viewModel.AddSettingsViewModel
import com.locationReminder.viewModel.LoginVM
import com.locationReminder.viewModel.SharedPreferenceVM

@Composable
fun NavigationGraph(navHostController: NavHostController, paddingValues: PaddingValues) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)) {
        NavHost(
            navController = navHostController,
            startDestination = NavigationRoute.HOMESCREEN.path,
            modifier = Modifier.fillMaxSize(),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(200)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(200)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(200)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(200)
                )
            }
        ) {


        composable(route = NavigationRoute.SPLASHSCREEN.path) {

            SplashScreen(navHostController)
        }

        composable(route = NavigationRoute.LOGINHOME.path) {
            val loginVM = hiltViewModel<LoginVM>()

            LoginScreen(navHostController,loginVM)
        }
        composable(route = NavigationRoute.SIGNUPSCREEN.path) {

            val loginVM = hiltViewModel<LoginVM>()

            SignUpScreen(navHostController,loginVM)
        }

        composable(route = NavigationRoute.LEADSSCREEN.path) {
            val viewModeRef = hiltViewModel<AddLocationViewModel>()

            OnExitListScreen(navHostController,viewModeRef)
        }


        composable(route = NavigationRoute.HOMESCREEN.path) {
            val viewModeRef = hiltViewModel<AddLocationViewModel>()
            val addSettingsViewModel = hiltViewModel<AddSettingsViewModel>()
            OnEntryListScreen(navHostController,viewModeRef,addSettingsViewModel)

        }

        composable(route = NavigationRoute.FOLLOWSUPSCREEN.path) {
            val viewModeRef = hiltViewModel<AddLocationViewModel>()
            val sharedPreferenceVM = hiltViewModel<SharedPreferenceVM>()
            MarkerListScreen(navHostController,viewModeRef,sharedPreferenceVM)

        }


        composable(route = NavigationRoute.SETTINGSSCREEN.path) {

            val viewModeRef = hiltViewModel<AddSettingsViewModel>()

            SettingsScreen(navHostController,viewModeRef)

        }
        composable(route = NavigationRoute.ADDCONTACTNUMBERSCREEN.path) {
            val viewModeRef = hiltViewModel<AddContactViewModel>()

            AddContactNumberScreen(navHostController,viewModeRef)

        }

        composable(route = NavigationRoute.CONTACTNUMBERSLISTCREEN.path) {
            val viewModeRef = hiltViewModel<AddContactViewModel>()

            ContactNumberListScreen(navHostController,viewModeRef)

        }

        composable(route = NavigationRoute.MAPSCREEN.path+"/{type}"+"/{itemId}",
            arguments = listOf(
                navArgument("type"){type = NavType.StringType},
            )) {

            val type = it.arguments?.getString("type")
            val itemId = it.arguments?.getString("itemId")
            val viewModeRef = hiltViewModel<AddLocationViewModel>()
            val addContactViewModel = hiltViewModel<AddContactViewModel>()
            val addSettingsViewModel = hiltViewModel<AddSettingsViewModel>()
            val sharedPreferenceVM = hiltViewModel<SharedPreferenceVM>()
            MapScreen(navHostController,type.toString(),itemId.toString(),viewModeRef,addContactViewModel,addSettingsViewModel,sharedPreferenceVM)}


        composable(route = NavigationRoute.VIEWALLMAPSCREEN.path+"/{type}",
            arguments = listOf(
                navArgument("type"){type = NavType.StringType},
            )) {

            val type = it.arguments?.getString("type")
            val viewModeRef = hiltViewModel<AddLocationViewModel>()
            ViewAllMapScreen(navHostController,viewModeRef, type.toString())        }


    }
}
}