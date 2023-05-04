package com.dunghn2201.eternalcalendar.ui.day_calendar

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.dunghn2201.eternalcalendar.R
import com.dunghn2201.eternalcalendar.ui.theme.*
import com.dunghn2201.eternalcalendar.util.extension.*
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*
import kotlinx.coroutines.NonDisposableHandle.parent
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
    var showPickDateDialog by remember {
        mutableStateOf(false)
    }
    val coroutine = rememberCoroutineScope()
    var dragDirection by remember {
        mutableStateOf(DragDirection.TOP_TO_BOTTOM)
    }
    var isVisibleTransitionType by remember {
        mutableStateOf(Pair(true, dragDirection))
    }
    var currentTime by remember {
        mutableStateOf(LocalTime.now())
    }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentTime = null
            currentTime = LocalTime.now()
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectVerticalDragGestures(onDragEnd = {
                    coroutine.launch {
                        isVisibleTransitionType = Pair(false, dragDirection) // exit old transition
                        delay(150)
                        val dateTarget = dragDirection.getDateDisplayTarget(uiState.calendar)
                        viewModel.getCalendarInfoByDate(dateTarget)
                        delay(150)
                        isVisibleTransitionType = Pair(true, dragDirection) // enter new transition
                    }
                }) { change, _ ->
                    val deltaY = change.positionChange().y
                    dragDirection = if (deltaY > 0) {
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
                        val dateTarget = dragDirection.getDateDisplayTarget(uiState.calendar)
                        viewModel.getCalendarInfoByDate(dateTarget)
                        delay(150)
                        isVisibleTransitionType = Pair(true, dragDirection) // enter new transition
                    }
                }) { change, _ ->
                    val deltaX = change.positionChange().x
                    dragDirection = if (deltaX > 0) {
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
            painter = painterResource(id = uiState.bgCalendarRes),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
        )
        /** Today */
        if (!uiState.calendar.isSameDate(uiState.now)) {
            SafeScaleAnimatedClickable(
                modifier = Modifier
                    .constrainAs(today) {
                        top.linkTo(pickDate.top)
                        bottom.linkTo(pickDate.bottom)
                    }
                    .padding(start = 10.dp),
                onClick = {
                    viewModel.getCalendarInfoByDate(uiState.now)
                },
            ) {
                Text(
                    modifier = Modifier
                        .background(
                            Color.White.copy(
                                alpha = 0.7f,
                            ),
                            RoundedCornerShape(30.dp),
                        )
                        .border(1.5.dp, ToryBlue, RoundedCornerShape(30.dp))
                        .padding(10.dp),
                    text = stringResource(R.string.today),
                    color = ToryBlue,
                    fontSize = 15.sp,
                    fontFamily = OpenSansMedium,
                )
            }
        }
        /** PickDate */
        Row(
            modifier = Modifier
                .constrainAs(pickDate) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .clickableWithoutRipple {
                    showPickDateDialog = true
                }
                .padding(30.dp)
                .background(
                    Color.White.copy(
                        alpha = 0.7f,
                    ),
                    RoundedCornerShape(30.dp),
                )
                .border(1.5.dp, ToryBlue, RoundedCornerShape(30.dp))
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.pick_date_display, uiState.month, uiState.year),
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
            exit = isVisibleTransitionType.second.exitTransition,
        ) {
            Column(
                modifier = Modifier.offset(y = 0.3.dp),
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
        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(quote) {
                    bottom.linkTo(additionalInfo.top, margin = 10.dp)
                },
            visible = isVisibleTransitionType.first,
            enter = isVisibleTransitionType.second.enterTransition,
            exit = isVisibleTransitionType.second.exitTransition,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
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
                    .padding(top = 100.dp)
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
                    modifier = Modifier.weight(2f),
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
                        stringResource(id = R.string.hour_minutes_args, currentTime.hour, currentTime.minute),
                        fontSize = 25.sp,
                        fontFamily = OpenSansSemiBold,
                        color = PersianBlue,
                    )
                    Text(
                        currentTime.hour.getCanChiHour(uiState.dayCanChi),
                        fontSize = 15.sp,
                        fontFamily = OpenSansMedium,
                        color = Color.Black,
                    )
                }
                Text(
                    modifier = Modifier
                        .weight(1.5f)
                        .align(Alignment.Bottom),
                    text = uiState.ngayHoangDaoMsg,
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                    fontFamily = OpenSansMedium,
                    color = if (uiState.ngayHoangDaoMsg.length > 12) Color.Red else NaturalGrey,
                )
                Column(
                    modifier = Modifier.weight(2f),
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
                        stringResource(id = R.string.year_args, uiState.yearCanChi),
                        fontSize = 15.sp,
                        fontFamily = OpenSansMedium,
                        color = Color.Black,
                    )
                    Text(
                        stringResource(id = R.string.month_args, uiState.monthCanChi),
                        fontSize = 15.sp,
                        fontFamily = OpenSansMedium,
                        color = Color.Black,
                    )
                    Text(
                        stringResource(id = R.string.month_args, uiState.dayCanChi),
                        fontSize = 15.sp,
                        fontFamily = OpenSansMedium,
                        color = Color.Black,
                    )
                }
            }
            /** Lunar Calendar */
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .paint(
                        painterResource(id = R.drawable.bg_lunar_calendar),
                        contentScale = ContentScale.Fit,
                    )
                    .padding(top = 25.dp, bottom = 35.dp, start = 35.dp, end = 35.dp)
                    .constrainAs(lunarCalendar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = String.format("%02d", uiState.dayLunar),
                    fontSize = 60.sp,
                    fontFamily = OpenSansBold,
                    color = PersianBlue,
                )
                Divider(
                    modifier = Modifier.width(70.dp),
                    color = PersianBlue,
                    thickness = 1.dp,
                )
                Text(
                    stringResource(id = R.string.month_num_args, uiState.monthLunar),
                    fontSize = 15.sp,
                    fontFamily = OpenSansMedium,
                    color = PersianBlue,
                )
            }
        }
        /** PickDateDialog */
        if (showPickDateDialog)
            PickDateDialog()
    }
}

@Preview
@Composable
fun PickDateDialog() {
    val boxSize = with(LocalDensity.current) { 300.dp.toPx() }
    Dialog(properties = DialogProperties(usePlatformDefaultWidth = false), onDismissRequest = { /*TODO*/ }) {
        Surface(
            modifier = Modifier.padding(10.dp),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, Color.White),
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (title, rowContent, Ok) = createRefs()
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(title) {
                            top.linkTo(parent.top)
                        },
                    text = "Ngày|Tháng|Năm",
                    textAlign = TextAlign.Center,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .constrainAs(rowContent) {
                            top.linkTo(title.bottom)
                        },
                ) {
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(Ok) {
                            top.linkTo(rowContent.bottom)
                            bottom.linkTo(parent.bottom)
                        },
                    text = "Đồng Ý",
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

enum class DragDirection {
    TOP_TO_BOTTOM, BOTTOM_TO_TOP, START_TO_END, END_TO_START;

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

    fun getDateDisplayTarget(calendar: Calendar): Calendar {
        when (this) {
            TOP_TO_BOTTOM -> calendar.add(Calendar.MONTH, 1)
            BOTTOM_TO_TOP -> calendar.add(Calendar.MONTH, -1)
            START_TO_END -> calendar.add(Calendar.DAY_OF_MONTH, -1)
            END_TO_START -> calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return calendar
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
