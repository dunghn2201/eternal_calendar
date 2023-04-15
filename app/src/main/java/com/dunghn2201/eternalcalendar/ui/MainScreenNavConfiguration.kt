package com.dunghn2201.eternalcalendar.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dunghn2201.eternalcalendar.R
import com.dunghn2201.eternalcalendar.ui.day_calendar.DayCalendarScreen
import com.dunghn2201.eternalcalendar.ui.month_calendar.MonthCalendarScreen
import com.dunghn2201.eternalcalendar.ui.news.NewsScreen
import timber.log.Timber

@Composable
fun MainScreenNavConfiguration(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController,
        startDestination = BottomNavigationScreens.DayCalendar.route,
        modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
    ) {
        composable(BottomNavigationScreens.News.route) {
            NewsScreen()
        }
        composable(BottomNavigationScreens.DayCalendar.route) {
            DayCalendarScreen()
        }
        composable(BottomNavigationScreens.MonthCalendar.route) {
            MonthCalendarScreen()
        }
    }
}

@Composable
fun CalendarBottomNavigation(
    navController: NavController,
    items: List<BottomNavigationScreens>
) {
    var currentTab by remember {
        mutableStateOf(BottomNavigationScreens.DayCalendar.route)
    }

    BottomNavigation(backgroundColor = Color.White) {
        val currentRoute = currentRoute(navController)
        items.forEach { screen ->
            val selected = currentRoute == screen.route // This hides the title for the unselected items
            val colorTab = if (currentTab == screen.route) Color.Red else Color.Gray
            BottomNavigationItem(
                icon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = screen.icon),
                        tint = colorTab,
                        contentDescription = screen.route,
                    )
                },
                label = {
                    Text(text = stringResource(id = screen.nameRes), color = colorTab)
                },
                selected = selected,
                selectedContentColor = Color.Red,
                unselectedContentColor = Color.Gray,
                onClick = {
                    // This if check gives us a "singleTop" behavior where we do not create a
                    // second instance of the composable if we are already on that destination
                    if (currentRoute != screen.route) {
                        Timber.e("/// navController.navigate(screen.route)")
                        navController.navigate(screen.route)
                        currentTab = screen.route
                    }
                },
            )
        }
    }
}

@Composable
fun currentRoute(navController: NavController): String {
    val keyRoute = "key_route"
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.arguments?.getString(keyRoute).orEmpty()
}

sealed class BottomNavigationScreens(
    val route: String,
    @StringRes val nameRes: Int,
    @DrawableRes val icon: Int,
) {
    object News : BottomNavigationScreens("NEWS", R.string.news, R.drawable.ic_news)
    object DayCalendar : BottomNavigationScreens("DAY_CALENDAR", R.string.daily_calendar, R.drawable.ic_day_calendar)
    object MonthCalendar :
        BottomNavigationScreens("MONTH_CALENDAR", R.string.calendar_month, R.drawable.ic_month_calendar)
}
