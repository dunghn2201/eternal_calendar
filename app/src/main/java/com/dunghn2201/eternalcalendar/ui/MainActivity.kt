package com.dunghn2201.eternalcalendar.ui

import android.os.Bundle
import android.view.Window
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.dunghn2201.eternalcalendar.ui.theme.EternalCalendarTheme
import com.dunghn2201.eternalcalendar.util.extension.HideStatusBar
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HideStatusBar(window)
            val navController = rememberNavController()
            EternalCalendarTheme {
                Scaffold(
                    bottomBar = {
                        CalendarBottomNavigation(navController)
                    },
                ) { paddingValues ->
                    MainScreenNavConfiguration(navController, paddingValues)
                }
            }
        }
    }
}

