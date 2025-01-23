package com.example.crmmainproject.view

import androidx.compose.runtime.Composable
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController

@Composable
fun ConnectionsScreen(navHostController: NavHostController) {
    val url = "https://www.example.com"

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true

                webViewClient = WebViewClient()

                loadUrl(url)
            }
        },
        update = { webView ->
            webView.loadUrl(url)
        }
    )
}

