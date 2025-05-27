package com.locationReminder.view.appNavigation

import com.locationReminder.R


sealed class Destinations(
    val route: String,
    val iconSelected: Int ,
    val iconUnSelected: Int
) {
    data object HOME : Destinations(
        route = NavigationRoute.HOMESCREEN.path,
        iconSelected = R.drawable.onentry,
        iconUnSelected = R.drawable.unselected_onentry,
    )

    data object BOOKINGS : Destinations(
        route = NavigationRoute.LEADSSCREEN.path,
        iconSelected = R.drawable.onexit,
        iconUnSelected = R.drawable.unselected_onexit,
    )

    data object TRANSACTIONS : Destinations(
        route = NavigationRoute.FOLLOWSUPSCREEN.path,
        iconSelected = R.drawable.marker,
        iconUnSelected = R.drawable.unselected_marker,
    )



    data object SETTINGS : Destinations(
        route = NavigationRoute.SETTINGSSCREEN.path,
        iconSelected = R.drawable.settings_yellow,
        iconUnSelected = R.drawable.unselected_settings_yellow,
    )
}