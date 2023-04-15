package com.dunghn2201.eternalcalendar.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.navigation.compose.rememberNavController
import com.dunghn2201.eternalcalendar.ui.theme.EternalCalendarTheme
import com.dunghn2201.eternalcalendar.util.extension.HideStatusBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val bottomNavigationItems = listOf(
                BottomNavigationScreens.News,
                BottomNavigationScreens.DayCalendar,
                BottomNavigationScreens.MonthCalendar,
            )
            EternalCalendarTheme {
                Scaffold(
                    bottomBar = {
                        CalendarBottomNavigation(navController = navController, items = bottomNavigationItems)
                    },
                ) { paddingValues ->
                    MainScreenNavConfiguration(navController, paddingValues)
                }
            }
            HideStatusBar()
        }
    }
}
