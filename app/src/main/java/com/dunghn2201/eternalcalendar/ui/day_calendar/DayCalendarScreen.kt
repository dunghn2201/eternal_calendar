package com.dunghn2201.eternalcalendar.ui.day_calendar

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
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
import com.dunghn2201.eternalcalendar.ui.theme.*
import com.dunghn2201.eternalcalendar.util.extension.SafeScaleAnimatedClickable
import com.dunghn2201.eternalcalendar.util.extension.UiState
import java.time.DayOfWeek
import java.time.LocalDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun DayCalendarScreen() {
    val viewModel: DayCalendarViewModel = hiltViewModel()
    val uiState by lazy {
        viewModel.uiState
    }
    if (uiState.uiState != UiState.State.COMPLETE) return
    val coroutine = rememberCoroutineScope()
    var dragDirection by remember {
        mutableStateOf(DragDirection.TOP_TO_BOTTOM)
    }
    Timber.e("/// date current ${uiState.date}")
    var isVisibleTransitionType by remember {
        mutableStateOf(Pair(true, dragDirection))
    }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectVerticalDragGestures(onDragEnd = {
                    coroutine.launch {
                        isVisibleTransitionType = Pair(false, dragDirection) // exit old transition
                        delay(150)
                        Timber.e("/// dragDirection Vertical $dragDirection")
                        val dateTarget = dragDirection.getDateDisplayTarget(uiState.date)
                        viewModel.getCalendarInfoByDate(dateTarget)
                        delay(150)
                        isVisibleTransitionType = Pair(true, dragDirection) // enter new transition
                    }

                }) { change, _ ->
                    val deltaY = change.positionChange().y
                    dragDirection =
                        if (deltaY > 0) {
                            DragDirection.TOP_TO_BOTTOM
                        } else {
                            DragDirection.BOTTOM_TO_TOP
                        }
                }
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(onDragEnd = {
                    coroutine.launch {
                        isVisibleTransitionType = Pair(false, dragDirection) // exit old transition
                        delay(150)
                        Timber.e("/// dragDirection tHorizontal $dragDirection")
                        val dateTarget = dragDirection.getDateDisplayTarget(uiState.date)
                        viewModel.getCalendarInfoByDate(dateTarget)
                        delay(150)
                        isVisibleTransitionType = Pair(true, dragDirection) // enter new transition
                    }
                }) { change, _ ->
                    val deltaX = change.positionChange().x
                    dragDirection =
                        if (deltaX > 0) {
                            DragDirection.START_TO_END
                        } else {
                            DragDirection.END_TO_START
                        }
                }
            },
    ) {
        val (bg, today, pickDate, mainDisplayDate, quote, additionalInfo) = createRefs()
        /** background */
        Image(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(bg) {
                    top.linkTo(parent.top)
                },
            painter = painterResource(id = uiState.bgCalendarRes.random()),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
        )
        /** Today */
        SafeScaleAnimatedClickable(
            modifier = Modifier
                .constrainAs(today) {
                    top.linkTo(pickDate.top)
                    bottom.linkTo(pickDate.bottom)
                }
                .padding(start = 10.dp),
            onClick = {
                viewModel.getCalendarInfoByDate(viewModel.now)
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
        /** PickDate */
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
                text = stringResource(R.string.pick_date_display, uiState.date.monthValue, uiState.date.year),
                color = ToryBlue,
                fontSize = 18.sp,
                fontFamily = OpenSansSemiBold,
            )
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = ToryBlue)
        }
        /** dayOfWeek */
        AnimatedVisibility(
            modifier = Modifier
                .constrainAs(mainDisplayDate) {
                    top.linkTo(pickDate.bottom)
                    bottom.linkTo(quote.top)
                }
                .fillMaxSize(),
            visible = isVisibleTransitionType.first,
            enter = isVisibleTransitionType.second.enterTransition,
            exit = isVisibleTransitionType.second.exitTransition
        ) {
            Column(
                modifier = Modifier
                    .offset(y = 0.3.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                Row {
                    Image(
                        modifier = Modifier
                            .width(70.dp)
                            .height(110.dp),
                        painter = painterResource(id = uiState.dayRes.first),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                    )
                    Image(
                        modifier = Modifier
                            .width(70.dp)
                            .height(110.dp),
                        painter = painterResource(id = uiState.dayRes.second),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                    )
                }
                Text(
                    text = uiState.dayOfWeek,
                    color = uiState.colorRes,
                    fontSize = 25.sp,
                    fontFamily = OpenSansBold,
                )
            }
        }

        /** Quote Info */
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
                text = uiState.quote,
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
                text = uiState.author,
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
                    .paint(
                        painterResource(id = R.drawable.bg_lunar_calendar),
                        contentScale = ContentScale.FillBounds
                    )
                    .padding(bottom = 15.dp)
                    .constrainAs(lunarCalendar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .clickable {

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

fun isWeekend(date: LocalDate): Boolean {
    val dayOfWeek = date.dayOfWeek
    return dayOfWeek == DayOfWeek.SUNDAY
}

enum class DragDirection {
    TOP_TO_BOTTOM,
    BOTTOM_TO_TOP,
    START_TO_END,
    END_TO_START;


    val enterTransition
        get() = when (this) {
            TOP_TO_BOTTOM -> EnterTransitionType.TOP_TO_TARGET.enterTransition
            BOTTOM_TO_TOP -> EnterTransitionType.BOTTOM_TO_TARGET.enterTransition
            START_TO_END -> EnterTransitionType.START_TO_TARGET.enterTransition
            END_TO_START -> EnterTransitionType.END_TO_TARGET.enterTransition
        }

    val exitTransition
        get() = when (this) {
            TOP_TO_BOTTOM -> ExitTransitionType.TARGET_TO_BOTTOM.enterTransition
            BOTTOM_TO_TOP -> ExitTransitionType.TARGET_TO_TOP.enterTransition
            START_TO_END -> ExitTransitionType.TARGET_TO_END.enterTransition
            END_TO_START -> ExitTransitionType.TARGET_TO_START.enterTransition
        }

    fun getDateDisplayTarget(date: LocalDate): LocalDate {
        val unitAction = 1L
        return when (this) {
            TOP_TO_BOTTOM -> date.plusMonths(unitAction)
            BOTTOM_TO_TOP -> date.minusMonths(unitAction)
            START_TO_END -> date.minusDays(unitAction)
            END_TO_START -> date.plusDays(unitAction)
        }
    }

}

enum class EnterTransitionType(val enterTransition: EnterTransition) {
    TOP_TO_TARGET(slideInVertically() + fadeIn()),
    BOTTOM_TO_TARGET(slideInVertically(initialOffsetY = { it / 2 }) + fadeIn()),
    START_TO_TARGET(slideInHorizontally() + fadeIn()),
    END_TO_TARGET(slideInHorizontally(initialOffsetX = { it / 2 }) + fadeIn())
}

enum class ExitTransitionType(val enterTransition: ExitTransition) {
    TARGET_TO_TOP(slideOutVertically() + fadeOut()),
    TARGET_TO_BOTTOM(slideOutVertically(targetOffsetY = { it / 2 }) + fadeOut()),
    TARGET_TO_END(slideOutHorizontally(targetOffsetX = { it / 2 }) + fadeOut()),
    TARGET_TO_START(slideOutHorizontally() + fadeOut())
}

