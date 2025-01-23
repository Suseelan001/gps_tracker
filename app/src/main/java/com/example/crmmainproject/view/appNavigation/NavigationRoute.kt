package com.example.crmmainproject.view.appNavigation

sealed class NavigationRoute(val path:String) {
    data object SPLASHSCREEN: NavigationRoute("splashScreen")
    data object LOGINHOME: NavigationRoute("login")
    data object SIGNUPSCREEN: NavigationRoute("signupScreen")

    data object HOMESCREEN: NavigationRoute("homeScreen")
    data object LEADSSCREEN: NavigationRoute("bookingScreen")
    data object FOLLOWSUPSCREEN: NavigationRoute("transactionScreen")
    data object CONNECTIONSSCREEN: NavigationRoute("notificationScreen")
    data object SETTINGSSCREEN: NavigationRoute("settingsScreen")

    fun withArgs(vararg args: String?): String {
        return buildString {
            append(path)
            args.forEach{ arg ->
                append("/$arg")
            }
        }
    }

}

