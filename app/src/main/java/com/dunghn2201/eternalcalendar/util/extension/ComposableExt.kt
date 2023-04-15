package com.dunghn2201.eternalcalendar.util.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun HideStatusBar() {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.isStatusBarVisible = false
        systemUiController.isNavigationBarVisible = false
        systemUiController.isSystemBarsVisible = false
        systemUiController.navigationBarDarkContentEnabled = false
    }
}
