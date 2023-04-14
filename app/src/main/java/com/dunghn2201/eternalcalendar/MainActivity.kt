package com.dunghn2201.eternalcalendar

import android.os.Build
import android.os.Bundle
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.dunghn2201.eternalcalendar.ui.theme.EternalCalendarTheme
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.DateFormatSymbols
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val calendar by lazy {
        Calendar.getInstance()
    }
    private val daysInMonth by lazy {
        calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EternalCalendarTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    HorizontalPagerScreen()
                }
            }
            val systemUiController = rememberSystemUiController()
            systemUiController.hideStatusBar()
        }

    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun HorizontalPagerScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
        ) {
            val items = createItems()
            val pagerState = rememberPagerState()

            HorizontalPager(
                pageCount = items.size,
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { currentPage ->
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = items[currentPage].title,
                        style = MaterialTheme.typography.h2
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = items[currentPage].subtitle,
                        style = MaterialTheme.typography.h4
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = items[currentPage].description,
                        style = MaterialTheme.typography.body1
                    )
                }
            }

            val coroutineScope = rememberCoroutineScope()
            Button(
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(page = 2)
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Scroll to the third page")
            }
        }
    }

    private fun SystemUiController.hideStatusBar() {
        this.apply {
            isStatusBarVisible = false
            isNavigationBarVisible = false
            isSystemBarsVisible = false
            navigationBarDarkContentEnabled = false
        }
    }

    fun createItems(): List<HorizontalPagerContent> {
        val year = 2023
        val month = 4

        val dateMonth = LocalDate.of(year, month, 1)
        val totalDaysInMonth = dateMonth.lengthOfMonth()
        return List(totalDaysInMonth) { dayOfMonth ->
            Timber.e("/// totalDaysInMonth $totalDaysInMonth")
            val date = LocalDate.of(year, month, dayOfMonth + 1)
            val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
            HorizontalPagerContent(
                title = "${dayOfMonth + 1}",
                subtitle = dayOfWeek,
                description = "Chúc 1 ngày tốt lành"
            )
        }
    }
}

data class HorizontalPagerContent(
    val title: String,
    val subtitle: String,
    val description: String,
)

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EternalCalendarTheme {
        Greeting("Android")
    }
}