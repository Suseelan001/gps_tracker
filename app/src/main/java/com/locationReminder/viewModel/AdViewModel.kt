package com.locationReminder.viewModel


import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.AdLoader



class AdViewModel(application: Application) : AndroidViewModel(application) {

    private val adCache = mutableMapOf<Int, NativeAd>()

    fun getCachedAd(position: Int): NativeAd? = adCache[position]

    fun getNativeAd(
        position: Int,
        adUnitId: String,
        onAdLoaded: () -> Unit
    ) {
        if (adCache.containsKey(position)) {
            onAdLoaded()
            return
        }

        val context = getApplication<Application>().applicationContext

        val adLoader = AdLoader.Builder(context, adUnitId)
            .forNativeAd { nativeAd ->
                adCache[position] = nativeAd
                onAdLoaded()
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("AdViewModel", "Failed to load native ad: ${error.message}")
                }
            })
            .build()

        val request = AdRequest.Builder().build()
        adLoader.loadAd(request)
    }

    override fun onCleared() {
        adCache.values.forEach { it.destroy() }
        super.onCleared()
    }
}
sealed class ListItem {
    data class Content(val text: String) : ListItem()
    data class Ad(val adKey: Int) : ListItem()
}
