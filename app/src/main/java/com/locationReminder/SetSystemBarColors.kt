package com.locationReminder

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

@Composable
fun ComponentActivity.SetSystemBarColors() {

    val isSystemInDarkTheme = isSystemInDarkTheme()

    DisposableEffect(isSystemInDarkTheme) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT,
                Color.TRANSPARENT,
            ) { isSystemInDarkTheme },
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim,
                darkScrim,
            ) { isSystemInDarkTheme },
        )
        onDispose {}
    }
}


val lightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)


val darkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b)