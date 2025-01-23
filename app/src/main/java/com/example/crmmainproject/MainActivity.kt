package com.example.crmmainproject

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.crmmainproject.model.localStorage.MySharedPreference
import com.example.crmmainproject.viewModel.MainViewModel
import com.example.crmmainproject.view.appNavigation.BottomBar
import com.example.crmmainproject.view.appNavigation.NavigationGraph
import com.example.crmmainproject.view.appNavigation.NavigationRoute
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var isHomeScreenLaunchedFirstTime = true
    private val mainViewModel = MainViewModel()
    @Inject
    lateinit var mySharedPreference: MySharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeBaseFunction()
        }
    }

    @Composable
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RestrictedApi")
    private fun ComposeBaseFunction() {
        val navController: NavHostController = rememberNavController()
        val buttonsVisible = remember { mutableStateOf(false) }
        var isBottomBarVisible by remember { mutableStateOf(false) }


        navController.addOnDestinationChangedListener { _, destination, _ ->
            buttonsVisible.value =
                        destination.hasRoute(NavigationRoute.HOMESCREEN.path, null) ||
                        destination.hasRoute(NavigationRoute.LEADSSCREEN.path, null) ||
                        destination.hasRoute(NavigationRoute.FOLLOWSUPSCREEN.path, null) ||
                        destination.hasRoute(NavigationRoute.CONNECTIONSSCREEN.path, null) ||
                        destination.hasRoute(NavigationRoute.SETTINGSSCREEN.path, null)

            if (destination.hasRoute(NavigationRoute.HOMESCREEN.path, null)) {
                if (isHomeScreenLaunchedFirstTime) {
                    isHomeScreenLaunchedFirstTime = false
                }
            }


        }
        LaunchedEffect(Unit) {
            delay(1200L)
            isBottomBarVisible = true
        }
        Scaffold(
            bottomBar = {
                Column {

                    if (isBottomBarVisible) {
                        HorizontalDivider(
                            modifier = Modifier
                                .background(colorResource(R.color.black))
                                .height(1.dp)
                        )
                        BottomBar(
                            mySharedPreference,
                            navController = navController,
                            buttonsVisible,
                            mainViewModel.broadcastMessage
                        )
                    }
                }
            }
        ) {
            NavigationGraph(navController, mainViewModel)
        }



    }

}





