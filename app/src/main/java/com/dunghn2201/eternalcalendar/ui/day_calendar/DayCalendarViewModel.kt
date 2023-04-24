package com.dunghn2201.eternalcalendar.ui.day_calendar

import android.app.Application
import android.icu.util.ChineseCalendar
import androidx.annotation.RawRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.dunghn2201.eternalcalendar.R
import com.dunghn2201.eternalcalendar.base.BaseViewModel
import com.dunghn2201.eternalcalendar.model.Events
import com.dunghn2201.eternalcalendar.model.Quotations
import com.dunghn2201.eternalcalendar.ui.theme.PersianBlue
import com.dunghn2201.eternalcalendar.ui.theme.RudyRed
import com.dunghn2201.eternalcalendar.util.extension.*
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlin.text.Typography.quote

@HiltViewModel
class DayCalendarViewModel @Inject constructor(val application: Application, val moshi: Moshi) :
    BaseViewModel(application) {
    var uiState by mutableStateOf(DayCalendarUiState())
        private set

    var quoteCollection: List<Quotations.Quotation> = listOf()
    var eventCollection: List<Events.Event> = listOf()

    init {
        quoteCollection = getQuotes()
        eventCollection = getEvent()
        getCalendarInfoByDate(uiState.now)
    }


    fun getCalendarInfoByDate(calendar: Calendar) {
        val quoteTarget = quoteCollection.shuffled().random()
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        val dateLunar = eventCollection.firstOrNull {
            it.daySolar == dayOfMonth &&
                it.monthSolar == month && it.yearSolar == year
        }
        val lunarChineseDate = ChineseCalendar(calendar.time)
        val dayLunar = lunarChineseDate.get(Calendar.DAY_OF_MONTH)
        val monthLunar = lunarChineseDate.get(Calendar.MONTH) + 1
        val yearCanChi = year.getCanChiYear()
        uiState = uiState.copy(
            dayOfMonth = dayOfMonth,
            month = month,
            year = year,
            dayLunar = dayLunar,
            monthLunar = monthLunar,
            dayOfWeek = calendar.getDayOfWeek(),
            yearCanChi = yearCanChi,
            monthCanChi = monthLunar.getCanChiMonth(yearCanChi),
            quote = quoteTarget.quote,
            author = quoteTarget.author,
            isWeekend = calendar.isWeekend(),
            calendar = calendar,
            uiState = UiState.State.COMPLETE,
        )
    }

    private fun getEvent(): List<Events.Event> {
        val jsonContent = readRawResourceToString(R.raw.events)
        val adapter = moshi.adapter(Events::class.java)
        val eventsResponse = adapter.fromJson(jsonContent)
        return eventsResponse?.events.orEmpty()
    }

    private fun getQuotes(): List<Quotations.Quotation> {
        val jsonContent = readRawResourceToString(R.raw.quotations)
        val adapter = moshi.adapter(Quotations::class.java)
        val quotesResponse = adapter.fromJson(jsonContent)
        return quotesResponse?.quotes.orEmpty()
    }

    private fun readRawResourceToString(@RawRes rawRes: Int): String {
        val inputStream = application.resources.openRawResource(rawRes)
        return inputStream.bufferedReader().use { it.readText() }
    }
}

typealias DayDrawableResource = Pair<Int, Int>

data class DayCalendarUiState(
    val dayOfMonth: Int = 1,
    val month: Int = 1,
    val year: Int = 1,
    val dayLunar: Int = 1,
    val monthLunar: Int = 1,
    val dayOfWeek: String = "",
    val quote: String = "",
    val author: String = "",
    val yearCanChi: String = "",
    val monthCanChi: String = "",
    val isWeekend: Boolean = false,
    val calendar: Calendar = Calendar.getInstance(),
    override
    val uiState: UiState.State = UiState.State.INITIAL,
) : UiState {
    val colorRes = if (isWeekend) RudyRed else PersianBlue
    val now: Calendar by lazy {
        Calendar.getInstance()
    }
    val dayRes
        get(): DayDrawableResource {
            val number0 = if (isWeekend) R.drawable.red0 else R.drawable.blue0
            val number1 = if (isWeekend) R.drawable.red1 else R.drawable.blue1
            val number2 = if (isWeekend) R.drawable.red2 else R.drawable.blue2
            val number3 = if (isWeekend) R.drawable.red3 else R.drawable.blue3
            val number4 = if (isWeekend) R.drawable.red4 else R.drawable.blue4
            val number5 = if (isWeekend) R.drawable.red5 else R.drawable.blue5
            val number6 = if (isWeekend) R.drawable.red6 else R.drawable.blue6
            val number7 = if (isWeekend) R.drawable.red7 else R.drawable.blue7
            val number8 = if (isWeekend) R.drawable.red8 else R.drawable.blue8
            val number9 = if (isWeekend) R.drawable.red9 else R.drawable.blue9
            return when (dayOfMonth) {
                1 -> Pair(number0, number1)
                2 -> Pair(number0, number2)
                3 -> Pair(number0, number3)
                4 -> Pair(number0, number4)
                5 -> Pair(number0, number5)
                6 -> Pair(number0, number6)
                7 -> Pair(number0, number7)
                8 -> Pair(number0, number8)
                9 -> Pair(number0, number9)
                10 -> Pair(number1, number0)
                11 -> Pair(number1, number1)
                12 -> Pair(number1, number2)
                13 -> Pair(number1, number3)
                14 -> Pair(number1, number4)
                15 -> Pair(number1, number5)
                16 -> Pair(number1, number6)
                17 -> Pair(number1, number7)
                18 -> Pair(number1, number8)
                19 -> Pair(number1, number9)
                20 -> Pair(number2, number0)
                21 -> Pair(number2, number1)
                22 -> Pair(number2, number2)
                23 -> Pair(number2, number3)
                24 -> Pair(number2, number4)
                25 -> Pair(number2, number5)
                26 -> Pair(number2, number6)
                27 -> Pair(number2, number7)
                28 -> Pair(number2, number8)
                29 -> Pair(number2, number9)
                30 -> Pair(number3, number0)
                31 -> Pair(number3, number1)
                else -> Pair(number0, number1)
            }
        }

    val bgCalendarRes =
        listOf(
            R.drawable.bg_lich0,
            R.drawable.bg_lich1,
            R.drawable.bg_lich2,
            R.drawable.bg_lich3,
            R.drawable.bg_lich4,
            R.drawable.bg_lich5,
            R.drawable.bg_lich6,
            R.drawable.bg_lich7,
            R.drawable.bg_lich8,
            R.drawable.bg_lich9,
            R.drawable.bg_lich10,
            R.drawable.bg_lich11,
            R.drawable.bg_lich12,
            R.drawable.bg_lich13,
            R.drawable.bg_lich14,
            R.drawable.bg_lich15,
            R.drawable.bg_lich16,
            R.drawable.bg_lich17,
            R.drawable.bg_lich18,
            R.drawable.bg_lich19,
            R.drawable.bg_lich20,
        ).shuffled().random()
}
