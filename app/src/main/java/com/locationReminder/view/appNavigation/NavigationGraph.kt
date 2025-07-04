package com.locationReminder.view.appNavigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import com.locationReminder.view.AddFolderNameScreen
import com.locationReminder.view.OnEntryListScreen
import com.locationReminder.view.OnExitListScreen
import com.locationReminder.view.MapScreen
import com.locationReminder.view.MarkerListScreen
import com.locationReminder.view.ViewAllMapScreen
import com.locationReminder.view.ContactNumberListScreen
import com.locationReminder.view.ImportMapScreen
import com.locationReminder.view.ImportedMarkerListScreen
import com.locationReminder.view.SettingsScreen
import com.locationReminder.viewModel.AddLocationViewModel
import com.locationReminder.view.LoginScreen
import com.locationReminder.view.MarkerListBaseScreen
import com.locationReminder.view.ProfileScreen
import com.locationReminder.view.SignUpScreen
import com.locationReminder.viewModel.AddContactViewModel
import com.locationReminder.viewModel.AddFolderNameViewModel
import com.locationReminder.viewModel.AddImportedCategoryNameViewModel
import com.locationReminder.viewModel.AddSettingsViewModel
import com.locationReminder.viewModel.LoginVM
import com.locationReminder.viewModel.SharedPreferenceVM

@Composable
fun NavigationGraph(navHostController: NavHostController,padding: PaddingValues) {
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
            OnEntryListScreen(navHostController,viewModeRef)

        }

        composable(route = NavigationRoute.FOLLOWSUPSCREEN.path) {
            val viewModeRef = hiltViewModel<AddFolderNameViewModel>()
            val sharedPreferenceVM = hiltViewModel<SharedPreferenceVM>()
            val addImportedCategoryNameViewModel = hiltViewModel<AddImportedCategoryNameViewModel>()
            val addLocationViewModel = hiltViewModel<AddLocationViewModel>()

            MarkerListBaseScreen(navHostController,viewModeRef,sharedPreferenceVM,addImportedCategoryNameViewModel,addLocationViewModel)

        }



            composable(route = NavigationRoute.MARKERLISTSCREEN.path+"/{categoryTitle}"+"/{id}",
                arguments = listOf(
                    navArgument("categoryTitle"){type = NavType.StringType},
                    navArgument("id"){type = NavType.StringType},
                )) {

                val categoryTitle = it.arguments?.getString("categoryTitle")
                val id = it.arguments?.getString("id")
                val viewModeRef = hiltViewModel<AddLocationViewModel>()
                val sharedPreferenceVM = hiltViewModel<SharedPreferenceVM>()
                MarkerListScreen(navHostController,viewModeRef,sharedPreferenceVM, id.toString(),categoryTitle.toString())
            }



            composable(
                route = NavigationRoute.IMPORTEDMARKERLISTSCREEN.path + "/{recordId}",
                arguments = listOf(
                    navArgument("recordId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val recordId = backStackEntry.arguments?.getString("recordId")
                val viewModeRef = hiltViewModel<AddLocationViewModel>()
                val sharedPreferenceVM = hiltViewModel<SharedPreferenceVM>()
                val addImportedCategoryNameViewModel = hiltViewModel<AddImportedCategoryNameViewModel>()

                ImportedMarkerListScreen(
                    navHostController,
                   viewModeRef,
                   sharedPreferenceVM,
                    recordId.orEmpty(),
                    addImportedCategoryNameViewModel
                )
            }




            composable(route = NavigationRoute.SETTINGSSCREEN.path) {
            val viewModeRef = hiltViewModel<AddSettingsViewModel>()
                val sharedPreferenceVM = hiltViewModel<SharedPreferenceVM>()
                SettingsScreen(navHostController,viewModeRef,sharedPreferenceVM)

        }
        composable(route = NavigationRoute.ADDCONTACTNUMBERSCREEN.path) {
            val viewModeRef = hiltViewModel<AddContactViewModel>()
            val sharedPreferenceVM = hiltViewModel<SharedPreferenceVM>()
            val addLocationViewModel = hiltViewModel<AddLocationViewModel>()

            AddContactNumberScreen(navHostController,viewModeRef,sharedPreferenceVM,addLocationViewModel)

        }


            composable(route = NavigationRoute.ADDFOLDERNAMESCREEN.path+"/{id}"+"/{categoryName}" +"/{isFrom}",
                arguments = listOf(
                    navArgument("id"){type = NavType.StringType},
                    navArgument("categoryName"){type = NavType.StringType},
                    navArgument("isFrom"){type = NavType.StringType},
                )) {

                val id = it.arguments?.getString("id")
                val categoryName = it.arguments?.getString("categoryName")
                val isFrom = it.arguments?.getString("isFrom")
                val viewModeRef = hiltViewModel<AddFolderNameViewModel>()
                val sharedPreferenceVM = hiltViewModel<SharedPreferenceVM>()
                val addImportedCategoryNameViewModel = hiltViewModel<AddImportedCategoryNameViewModel>()

                AddFolderNameScreen(navHostController,id.toString(),categoryName.toString(),isFrom.toString(),viewModeRef,sharedPreferenceVM,addImportedCategoryNameViewModel)}




            composable(route = NavigationRoute.CONTACTNUMBERSLISTCREEN.path) {
            val viewModeRef = hiltViewModel<AddContactViewModel>()

            ContactNumberListScreen(navHostController,viewModeRef)

        }

        composable(route = NavigationRoute.MAPSCREEN.path+"/{type}"+"/{itemId}"+"/{categoryId}"+"/{categoryTitle}",
            arguments = listOf(
                navArgument("type"){type = NavType.StringType},
            )) {

            val type = it.arguments?.getString("type")
            val itemId = it.arguments?.getString("itemId")
            val categoryId = it.arguments?.getString("categoryId")
            val categoryTitle = it.arguments?.getString("categoryTitle")
            val viewModeRef = hiltViewModel<AddLocationViewModel>()
            val addContactViewModel = hiltViewModel<AddContactViewModel>()
            val addSettingsViewModel = hiltViewModel<AddSettingsViewModel>()
            val sharedPreferenceVM = hiltViewModel<SharedPreferenceVM>()
            MapScreen(navHostController,type.toString(),itemId.toString(),categoryId.toString(),categoryTitle.toString(),viewModeRef,addContactViewModel,addSettingsViewModel,sharedPreferenceVM)}

        composable(route = NavigationRoute.IMPORTMAPSCREEN.path+"/{itemId}"+"/{type}",
            arguments = listOf(
                navArgument("type"){type = NavType.StringType},
            )) {
            val itemId = it.arguments?.getString("itemId")
            val type = it.arguments?.getString("type")
            val viewModeRef = hiltViewModel<AddLocationViewModel>()

            ImportMapScreen(navHostController, itemId.toString(),type.toString(), viewModeRef)}


        composable(route = NavigationRoute.VIEWALLMAPSCREEN.path+"/{type}"+"/{categoryId}",
            arguments = listOf(
                navArgument("type"){type = NavType.StringType},
            )) {

            val type = it.arguments?.getString("type")
            val categoryId = it.arguments?.getString("categoryId")
            val viewModeRef = hiltViewModel<AddLocationViewModel>()

            ViewAllMapScreen(navHostController,type.toString(), categoryId.toString(), viewModeRef
            )}

        composable(route = NavigationRoute.PROFILESCREEN.path) {
            val loginVM = hiltViewModel<LoginVM>()
            ProfileScreen(navHostController,loginVM)}
        }
}
