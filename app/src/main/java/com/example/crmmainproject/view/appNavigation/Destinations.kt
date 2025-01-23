package com.example.crmmainproject.view.appNavigation

import com.example.crmmainproject.R


sealed class Destinations(
    val route: String,
    val iconSelected: Int ,
    val iconUnSelected: Int
) {
    data object HOME : Destinations(
        route = NavigationRoute.HOMESCREEN.path,
        iconSelected = R.drawable.selected_home,
        iconUnSelected = R.drawable.home,
    )

    data object BOOKINGS : Destinations(
        route = NavigationRoute.LEADSSCREEN.path,
        iconSelected = R.drawable.selected_leads,
        iconUnSelected = R.drawable.leads,
    )

    data object TRANSACTIONS : Destinations(
        route = NavigationRoute.FOLLOWSUPSCREEN.path,
        iconSelected = R.drawable.selected_follows_up,
        iconUnSelected = R.drawable.follows_up,
    )

    data object NOTIFICATIONS : Destinations(
        route = NavigationRoute.CONNECTIONSSCREEN.path,
        iconSelected = R.drawable.selected_content,
        iconUnSelected = R.drawable.content,
    )

    data object SETTINGS : Destinations(
        route = NavigationRoute.SETTINGSSCREEN.path,
        iconSelected = R.drawable.selected_settings,
        iconUnSelected = R.drawable.settings,
    )
}