package com.locationReminder.view


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.*

@Composable
fun BannerAd(
    adUnitId: String = "ca-app-pub-3940256099942544/6300978111"
) {

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        factory = { context ->
            val adView = AdView(context)

            val display = context.resources.displayMetrics
            val adWidth = (display.widthPixels / display.density).toInt()

            adView.adUnitId = adUnitId
            adView.setAdSize(AdSize.getCurrentOrientationInlineAdaptiveBannerAdSize(context, adWidth))

            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)

            adView
        }
    )
}


