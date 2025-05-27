package com.locationReminder.view.appNavigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.locationReminder.ui.theme.Hex222227
import com.locationReminder.ui.theme.Hex36374a
import com.locationReminder.ui.theme.HexFFFFFF
import com.locationReminder.ui.theme.Hexeef267


@Composable
fun BottomBar(
    navController: NavHostController,
    state: MutableState<Boolean>,
    modifier: Modifier = Modifier,
) {
    val screens = listOf(Destinations.HOME, Destinations.BOOKINGS, Destinations.TRANSACTIONS, Destinations.SETTINGS)
    val bottomBarHeight = 80.dp

    if (state.value) {
        Box(modifier = modifier.height(bottomBarHeight)) {
            NavigationBar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .height(bottomBarHeight),
                containerColor =Hex36374a,
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                screens.take(2).forEach { screen ->
                    NavigationBarItem(
                        icon = { ScreenIcon(screen, currentRoute) },
                        selected = currentRoute == screen.route,
                        onClick = { navigateToScreen(navController, screen) },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent, // No background selection
                            selectedIconColor = Hexeef267,
                            unselectedIconColor = Hexeef267,
                            selectedTextColor = Hexeef267,
                            unselectedTextColor = Hexeef267
                        )

                    )
                }

                // Empty spacer item for FAB
                NavigationBarItem(
                    icon = {},
                    selected = false,
                    onClick = {},
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent, // No background selection
                        selectedIconColor = Hexeef267,
                        unselectedIconColor = Hexeef267,
                        selectedTextColor = Hexeef267,
                        unselectedTextColor = Hexeef267
                    )

                )

                // Last two items on the right
                screens.takeLast(2).forEach { screen ->
                    NavigationBarItem(
                        icon = { ScreenIcon(screen, currentRoute) },
                        selected = currentRoute == screen.route,
                        onClick = { navigateToScreen(navController, screen) },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent, // No background selection
                            selectedIconColor = Hexeef267,
                            unselectedIconColor = Hexeef267,
                            selectedTextColor = Hexeef267,
                            unselectedTextColor = Hexeef267
                        )

                    )
                }
            }

            FloatingActionButton(
                onClick = {
                    val currentRoute = navController.currentBackStackEntry?.destination?.route

                    var screen = "Entry"
                    when (currentRoute) {
                        Destinations.HOME.route -> {
                            screen = "Entry"
                        }
                        Destinations.BOOKINGS.route -> {
                            screen = "Exit"
                        }
                        Destinations.TRANSACTIONS.route -> {
                            screen = "Marker"
                        }
                        Destinations.SETTINGS.route -> {
                            screen = "Entry"
                        }
                    }
                    navController.navigate("${NavigationRoute.MAPSCREEN.path}/$screen/${""}") {
                        popUpTo(NavigationRoute.MAPSCREEN.path) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }

                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (-56).dp),
                containerColor = Hexeef267,
                contentColor = Hexeef267,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Hex222227,
                    modifier = Modifier.size(38.dp)
                )
            }


        }
    }
}

@Composable
private fun ScreenIcon(screen: Destinations, currentRoute: String?) {
    val label = when (screen) {
        Destinations.HOME -> "On Entry"
        Destinations.BOOKINGS -> "On Exit"
        Destinations.TRANSACTIONS -> "Marker"
        Destinations.SETTINGS -> "Settings"
    }

    androidx.compose.foundation.layout.Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(
                if (currentRoute == screen.route) screen.iconSelected
                else screen.iconUnSelected
            ),
            contentDescription = screen.route,
        )
        androidx.compose.material3.Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (currentRoute == screen.route) Hexeef267 else HexFFFFFF
        )
    }
}


private fun navigateToScreen(navController: NavHostController, screen: Destinations) {
    navController.navigate(screen.route) {
        popUpTo(NavigationRoute.HOMESCREEN.path) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}