package com.locationReminder.viewModel

import android.content.Context
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient

object PlacesClientManager {
    private var placesClient: PlacesClient? = null

    fun getClient(context: Context): PlacesClient {
        if (placesClient == null) {
            placesClient = Places.createClient(context.applicationContext)
        }
        return placesClient!!
    }

    fun shutdown() {
        (placesClient as? AutoCloseable)?.close()
        placesClient = null
    }
}
