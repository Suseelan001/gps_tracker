package com.locationReminder.hilt

import android.app.Application
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Places API
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyBrZdwwuEPL4MjX0NN51CTCSsve7LGBNDc")
        }
    }
}