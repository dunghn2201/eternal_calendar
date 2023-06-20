package com.dunghn2201.eternalcalendar.ui.month_calendar

import android.app.Activity
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.dunghn2201.eternalcalendar.R
import com.dunghn2201.eternalcalendar.ui.theme.*
import kotlinx.coroutines.NonDisposableHandle.parent

@Composable
fun MonthCalendarScreen() {
    val context = LocalContext.current
    val activity = context as Activity
    val viewModel = hiltViewModel<MonthCalendarViewModel>()

    Column {
        Spacer(modifier = Modifier.height(100.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .border(1.dp, Color.Red, shape = RoundedCornerShape(5.dp))
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("CN")
            }
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .border(1.dp, Color.Red, shape = RoundedCornerShape(5.dp))
                    .padding(10.dp),
                contentAlignment = Alignment.Center

            ) {
                Text("Hai")
            }
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .border(1.dp, Color.Red, shape = RoundedCornerShape(5.dp))
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Ba")
            }
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .border(1.dp, Color.Red, shape = RoundedCornerShape(5.dp))
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Tư")
            }
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .border(1.dp, Color.Red, shape = RoundedCornerShape(5.dp))
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Năm")
            }
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .border(1.dp, Color.Red, shape = RoundedCornerShape(5.dp))
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Sáu")
            }
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .border(1.dp, Color.Red, shape = RoundedCornerShape(5.dp))
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Bảy")
            }
        }
        LazyVerticalGrid(modifier = Modifier.fillMaxWidth(), columns = GridCells.Fixed(7)) {
            items(viewModel.daysDisplay) { item ->
                ItemMonthCalendar(item)
            }
        }
    }

}

@Composable
private fun ItemMonthCalendar(item: DayCalendarItem) {
    ConstraintLayout(
        modifier = Modifier
            .background(item.bgColor)
            .border(1.dp, Color.Red, shape = RoundedCornerShape(5.dp))
            .padding(10.dp),

        ) {
        val (star, dayOfMonth, dayLunar, dot) = createRefs()
        Image(
            modifier = Modifier
                .size(10.dp)
                .constrainAs(star) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            painter = painterResource(id = R.drawable.ic_star),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.Red)
        )
        Text(
            modifier = Modifier.constrainAs(dayOfMonth) {
                top.linkTo(parent.top)
                start.linkTo(star.end, margin = 2.dp)
                end.linkTo(dot.start)
                bottom.linkTo(dayLunar.top)
            },
            text = item.dayOfMonth.toString(),
            fontSize = 15.sp,
            color = item.colorDayText,
            fontFamily = OpenSansMedium,
        )
        Text(
            modifier = Modifier.constrainAs(dayLunar) {
                top.linkTo(dayOfMonth.bottom)
                bottom.linkTo(parent.bottom)
                start.linkTo(dayOfMonth.start)
                end.linkTo(dayOfMonth.end)
            }, text = item.dayLunar.toString(), fontSize = 8.sp,
            color = item.colorDayText,
            fontFamily = OpenSansMedium
        )

        Image(
            modifier = Modifier
                .size(5.dp)
                .constrainAs(dot) {
                    start.linkTo(dayLunar.end, margin = 2.dp)
                    end.linkTo(parent.end)
                    bottom.linkTo(dayLunar.bottom)
                },
            painter = painterResource(id = R.drawable.ic_dot),
            contentDescription = null,
            colorFilter = ColorFilter.tint(item.colorDot)
        )
    }
}
