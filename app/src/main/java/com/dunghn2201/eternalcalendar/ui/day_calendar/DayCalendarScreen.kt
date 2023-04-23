package com.dunghn2201.eternalcalendar.ui.day_calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.dunghn2201.eternalcalendar.R
import com.dunghn2201.eternalcalendar.model.CalendarPagerItem
import com.dunghn2201.eternalcalendar.ui.theme.*
import com.dunghn2201.eternalcalendar.util.extension.SafeScaleAnimatedClickable
import com.dunghn2201.eternalcalendar.util.extension.UiState
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DayCalendarScreen() {
    val viewModel: DayCalendarViewModel = hiltViewModel()
    val uiState by lazy {
        viewModel.uiState
    }
    val now by lazy {
        viewModel.now
    }
    Timber.e("/// check rebuild")
    var verticalDragDirection by remember {
        mutableStateOf(VerticalDragDirection.BOTTOM_TO_TOP)
    }
    if (uiState.uiState != UiState.State.COMPLETE) return
    val pagerState = rememberPagerState()
    LaunchedEffect(Unit) {
        pagerState.scrollToPage(now, uiState.items)
        viewModel.updateUiState(
            uiState.copy(
                latestPage = now,
            ),
        )
    }
    val coroutine = rememberCoroutineScope()
    val currentPage by lazy {
        uiState.items[pagerState.currentPage]
    }
    val isCurrentPageIsNotToday: Boolean by lazy {
        currentPage.date != now
    }
    val currentDate = currentPage.date
    val isCurrentIndexIsFirst = pagerState.currentPage == 0 && uiState.items.isNotEmpty()
    val isCurrentIndexIsEnd = pagerState.currentPage + 1 == uiState.items.size
    if (isCurrentIndexIsFirst) viewModel.getMoreDaysByMonth(currentDate, GetMonthType.SUBTRACT)
    if (isCurrentIndexIsEnd) viewModel.getMoreDaysByMonth(currentDate, GetMonthType.PLUS)

    SideEffect {
        coroutine.launch {
            val isApproachingLatestPage = currentPage.date != (uiState.latestPage?.minusDays(1)) ||
                currentPage.date != (uiState.latestPage?.plusDays(1))
            val isValidToSideBack = uiState.latestPage != null &&
                isApproachingLatestPage &&
                uiState.allowRefreshLatestPage
            if (isValidToSideBack) {
                Timber.e("/// scrollToPage latestPage ${uiState.latestPage}")
                Timber.e("/// scrollToPage currentPage ${currentPage.date}")

                pagerState.scrollToPage(uiState.latestPage!!, uiState.items)
                viewModel.updateUiState(
                    uiState.copy(
                        allowRefreshLatestPage = false,
                        latestPage = currentPage.date,
                    ),
                )
            }
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        val (today, pickDate, pager) = createRefs()

        HorizontalPager(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(pager) {
                    top.linkTo(pager.top)
                }
                .pointerInput(Unit) {
                    detectVerticalDragGestures(onVerticalDrag = { change, _ ->
                        val deltaY = change.positionChange().y
                        verticalDragDirection = if (deltaY > 0) {
                            VerticalDragDirection.TOP_TO_BOTTOM
                        } else {
                            VerticalDragDirection.BOTTOM_TO_TOP
                        }
                    }, onDragEnd = {
                        when (verticalDragDirection) {
                            VerticalDragDirection.TOP_TO_BOTTOM -> {
                                viewModel.getMoreDaysByMonth(currentDate, GetMonthType.PLUS, false)
                            }
                            VerticalDragDirection.BOTTOM_TO_TOP -> {
                                viewModel.getMoreDaysByMonth(currentDate, GetMonthType.SUBTRACT, false)
                            }
                        }
                        Timber.e("/// vuốt xong $verticalDragDirection")
                    })
                },
            pageCount = uiState.items.size,
            state = pagerState,
        ) { currentPage ->
            val item = uiState.items[currentPage]
            DayCalendarItem(item)
        }
        if (isCurrentPageIsNotToday) {
            SafeScaleAnimatedClickable(
                modifier = Modifier
                    .constrainAs(today) {
                        top.linkTo(pickDate.top)
                        bottom.linkTo(pickDate.bottom)
                    }
                    .padding(start = 10.dp),
                onClick = {
                    pagerState.scrollToPage(now, uiState.items)
                },
            ) {
                Text(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(30.dp))
                        .border(1.5.dp, ToryBlue, RoundedCornerShape(30.dp))
                        .padding(10.dp),
                    text = stringResource(R.string.today),
                    color = ToryBlue,
                    fontSize = 15.sp,
                    fontFamily = OpenSansMedium,
                )
            }
        }

        Row(
            modifier = Modifier
                .constrainAs(pickDate) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(10.dp)
                .background(Color.White, RoundedCornerShape(30.dp))
                .border(1.5.dp, ToryBlue, RoundedCornerShape(30.dp))
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.pick_date_display, currentPage.date.monthValue, currentPage.date.year),
                color = ToryBlue,
                fontSize = 18.sp,
                fontFamily = OpenSansSemiBold,
            )
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = ToryBlue)
        }
    }
}

@Composable
fun DayCalendarItem(item: CalendarPagerItem) {
    val bgCalendar = bgCalendar.random()
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        val (bg, dayInfo, quote, additionalInfo) = createRefs()
        /** background */
        Image(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(bg) {
                    top.linkTo(parent.top)
                },
            painter = painterResource(id = bgCalendar),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
        )
        /** dayOfWeek */
        Column(
            modifier = Modifier
                .constrainAs(dayInfo) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    linkTo(dayInfo.bottom, parent.bottom, bias = 0.4f)
                }
                .offset(y = 0.3.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            Row {
                Image(
                    modifier = Modifier
                        .width(70.dp)
                        .height(110.dp),
                    painter = painterResource(id = item.dayRes.first),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                )
                Image(
                    modifier = Modifier
                        .width(70.dp)
                        .height(110.dp),
                    painter = painterResource(id = item.dayRes.second),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                )
            }
            Text(
                text = item.dayOfWeek,
                color = item.colorRes,
                fontSize = 25.sp,
                fontFamily = OpenSansBold,
            )
        }
        /** AdditionalInfo */
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(quote) {
                    bottom.linkTo(additionalInfo.top)
                },
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                text = item.quote,
                color = Color.Black,
                fontSize = 17.sp,
                fontFamily = OpenSansMedium,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                text = item.author,
                color = Color.Black,
                fontSize = 17.sp,
                fontFamily = OpenSansMedium,
                textAlign = TextAlign.Right,
            )
        }

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(additionalInfo) {
                    bottom.linkTo(parent.bottom)
                },
        ) {
            val (lunarCalendar, info) = createRefs()
            /** Info */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 90.dp)
                    .background(
                        Color.White.copy(
                            alpha = 0.5f,
                        ),
                    )
                    .constrainAs(info) {
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(5.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        stringResource(id = R.string.hour).uppercase(),
                        fontSize = 20.sp,
                        fontFamily = OpenSansSemiBold,
                        color = NaturalGrey,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "20:48",
                        fontSize = 25.sp,
                        fontFamily = OpenSansSemiBold,
                        color = PersianBlue,
                    )
                    Text(
                        "Giờ Nhâm Tuất",
                        fontSize = 15.sp,
                        fontFamily = OpenSansMedium,
                        color = Color.Black,
                    )
                }
                Text(
                    modifier = Modifier.align(Alignment.Bottom),
                    text = "Ngày hoàng đạo",
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                    fontFamily = OpenSansMedium,
                    color = Color.Black,
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        stringResource(id = R.string.detail).uppercase(),
                        fontSize = 20.sp,
                        fontFamily = OpenSansSemiBold,
                        color = NaturalGrey,
                    )
                    Text(
                        "Năm Quý Mão",
                        fontSize = 15.sp,
                        fontFamily = OpenSansMedium,
                        color = Color.Black,
                    )
                    Text(
                        "Tháng Ất Mão",
                        fontSize = 15.sp,
                        fontFamily = OpenSansMedium,
                        color = Color.Black,
                    )
                    Text(
                        "Ngày Quý Mão",
                        fontSize = 15.sp,
                        fontFamily = OpenSansMedium,
                        color = Color.Black,
                    )
                }
            }
            /** Lunar Calendar */
            Column(
                modifier = Modifier
                    .size(120.dp)
                    .paint(painterResource(id = R.drawable.bg_lunar_calendar), contentScale = ContentScale.FillBounds)
                    .padding(bottom = 15.dp)
                    .constrainAs(lunarCalendar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("25", fontSize = 50.sp, fontFamily = OpenSansBold, color = PersianBlue)
                Divider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = PersianBlue,
                    thickness = 1.dp,
                )
                Text("THÁNG 2", fontSize = 15.sp, fontFamily = OpenSansMedium, color = PersianBlue)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
suspend fun PagerState.scrollToPage(dateCompare: LocalDate, items: List<CalendarPagerItem>) {
    val index = items.indexOfFirst { it.date == dateCompare }.takeIf { it != -1 } ?: 0
    scrollToPage(page = index)
}

fun isWeekend(date: LocalDate): Boolean {
    val dayOfWeek = date.dayOfWeek
    return dayOfWeek == DayOfWeek.SUNDAY
}

enum class VerticalDragDirection {
    BOTTOM_TO_TOP,
    TOP_TO_BOTTOM,
}
