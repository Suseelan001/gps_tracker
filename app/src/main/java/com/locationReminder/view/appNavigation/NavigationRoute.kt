package com.locationReminder.view.appNavigation

sealed class NavigationRoute(val path:String) {
    data object SPLASHSCREEN: NavigationRoute("splashScreen")
    data object LOGINHOME: NavigationRoute("login")
    data object SIGNUPSCREEN: NavigationRoute("signupScreen")

    data object HOMESCREEN: NavigationRoute("homeScreen")
    data object LEADSSCREEN: NavigationRoute("bookingScreen")
    data object FOLLOWSUPSCREEN: NavigationRoute("transactionScreen")
    data object SETTINGSSCREEN: NavigationRoute("settingsScreen")
    data object MAPSCREEN: NavigationRoute("MapScreen")
    data object ADDCONTACTNUMBERSCREEN: NavigationRoute("AddContactNumberScreen")
    data object CONTACTNUMBERSLISTCREEN: NavigationRoute("ContactNumberListScreen")
    data object VIEWALLMAPSCREEN: NavigationRoute("ViewAllMapScreen")



}

