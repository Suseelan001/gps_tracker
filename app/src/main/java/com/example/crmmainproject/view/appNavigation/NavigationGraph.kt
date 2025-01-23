package com.example.crmmainproject.view.appNavigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.crmmainproject.SplashScreen
import com.example.crmmainproject.view.ConnectionsScreen
import com.example.crmmainproject.view.FollowsUpScreen
import com.example.crmmainproject.view.HomeScreen
import com.example.crmmainproject.view.LeadsScreen
import com.example.crmmainproject.view.LoginScreen
import com.example.crmmainproject.view.SettingsScreen
import com.example.crmmainproject.view.SignUpScreen
import com.example.crmmainproject.viewModel.MainViewModel
import com.example.crmmainproject.viewModel.SplashScreenVM

@Composable
fun NavigationGraph(navHostController:NavHostController,mainViewModel: MainViewModel) {
    NavHost(navController = navHostController, startDestination = NavigationRoute.SPLASHSCREEN.path, Modifier.fillMaxSize(),
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(200)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(200)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(200)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(200)
            )
        }) {

        composable(route = NavigationRoute.SPLASHSCREEN.path) {
            val parentEntry = remember(it) {
                navHostController.getBackStackEntry(NavigationRoute.SPLASHSCREEN.path)
            }
            val viewModeRef = hiltViewModel<SplashScreenVM>(parentEntry)
            SplashScreen(viewModeRef,navHostController)
        }

        composable(route = NavigationRoute.LOGINHOME.path) {
            LoginScreen(navHostController)
        }
        composable(route = NavigationRoute.SIGNUPSCREEN.path) {
            SignUpScreen(navHostController)
        }

        composable(route = NavigationRoute.LEADSSCREEN.path) {
            LeadsScreen(navHostController)
        }

        composable(route = NavigationRoute.HOMESCREEN.path) {
            HomeScreen()

        }

        composable(route = NavigationRoute.FOLLOWSUPSCREEN.path) {

            FollowsUpScreen(navHostController)

        }

        composable(route = NavigationRoute.CONNECTIONSSCREEN.path) {

            ConnectionsScreen(navHostController)

        }
        composable(route = NavigationRoute.SETTINGSSCREEN.path) {

            SettingsScreen(navHostController)

        }


    }
}