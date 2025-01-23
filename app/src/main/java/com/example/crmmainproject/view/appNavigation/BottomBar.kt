package com.example.crmmainproject.view.appNavigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.example.crmmainproject.R
import com.example.crmmainproject.model.localStorage.MySharedPreference


@Composable
fun BottomBar(
    mySharedPreference: MySharedPreference,
    navController: NavHostController,
    state: MutableState<Boolean>,
    count:Int,
    modifier: Modifier = Modifier,
) {
    val screens = listOf(Destinations.HOME, Destinations.BOOKINGS, Destinations.TRANSACTIONS, Destinations.NOTIFICATIONS, Destinations.SETTINGS)

    if (state.value) {
        NavigationBar(
            modifier = modifier,
            containerColor = Color.White,
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            screens.forEach { screen ->

                val isSelected = currentRoute == screen.route
                NavigationBarItem(
                    icon = {
                            Image(
                                painter = if (isSelected) {
                                    painterResource(screen.iconSelected)
                                } else {
                                    painterResource(screen.iconUnSelected)
                                },
                                contentDescription = "content description",
                            )


                    },
                    selected = currentRoute == screen.route,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(NavigationRoute.HOMESCREEN.path) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = androidx.compose.material3.NavigationBarItemDefaults
                        .colors(
                            indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(0.dp)
                        )
                    /*colors = NavigationBarItemDefaults.colors(
                    unselectedTextColor = Color.Gray, selectedTextColor = Color.White
                ),*/
                )
            }
        }
    }

}