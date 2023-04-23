package com.dunghn2201.eternalcalendar.ui.day_calendar

import android.app.Application
import androidx.annotation.RawRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.dunghn2201.eternalcalendar.R
import com.dunghn2201.eternalcalendar.base.BaseViewModel
import com.dunghn2201.eternalcalendar.model.Quotations
import com.dunghn2201.eternalcalendar.ui.theme.PersianBlue
import com.dunghn2201.eternalcalendar.ui.theme.RudyRed
import com.dunghn2201.eternalcalendar.util.extension.UiState
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DayCalendarViewModel @Inject constructor(val application: Application, val moshi: Moshi) :
    BaseViewModel(application) {
    var uiState by mutableStateOf(DayCalendarUiState())
        private set

    val now: LocalDate by lazy {
        LocalDate.now()
    }

    init {
        getCalendarInfoByDate(now)
    }

    fun getCalendarInfoByDate(date: LocalDate) {
        val targetDate = LocalDate.of(date.year, date.monthValue, date.dayOfMonth)
        val quoteTarget = getQuotes().random()
        val dayOfWeek = targetDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("vi"))
        uiState = uiState.copy(
            dayOfWeek = dayOfWeek,
            quote = quoteTarget.quote,
            author = quoteTarget.author,
            isWeekend = isWeekend(targetDate),
            date = targetDate,
            uiState = UiState.State.COMPLETE
        )
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

/*    fun getMoreDaysByMonth(date: LocalDate, monthType: GetMonthType, allowRefreshLatestPage: Boolean? = null) {
        val isValidToGetMore = date == (uiState.latestPage?.minusDays(1)) ||
            date == (uiState.latestPage?.plusDays(1)) ||
            (date.monthValue == uiState.latestPage?.monthValue && date.year == uiState.latestPage?.year)
        Timber.e("getMoreDaysByMonth latestPage ${uiState.latestPage}")
        Timber.e("getMoreDaysByMonth date $date")
        Timber.e("check 1 ${date == (uiState.latestPage?.minusDays(1))}")
        Timber.e("check 2 ${date == (uiState.latestPage?.plusDays(1))}")
        Timber.e(
            "check 3 ${
                (
                    date.monthValue == uiState.latestPage?.monthValue &&
                        date.year == uiState.latestPage?.year
                    )
            }",
        )
        if (!isValidToGetMore) return
        val dateTarget = when (monthType) {
            GetMonthType.SUBTRACT -> date.minusMonths(1)
            GetMonthType.PLUS -> date.plusMonths(1)
        }
        val isDateAlreadyExists = uiState.items.any {
            it.date.monthValue == dateTarget.monthValue && it.date.year == dateTarget.year
        }
        if (isDateAlreadyExists) return
        val totalDaysInMonth = dateTarget.lengthOfMonth()
        val items = List(totalDaysInMonth) { dayOfMonth ->
            val quote = quoteList.random()
            val dateUpdated = LocalDate.of(dateTarget.year, dateTarget.monthValue, dayOfMonth + 1)
            val dayOfWeek = dateUpdated.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("vi"))
            CalendarPagerItem(
                dayOfWeek = dayOfWeek.uppercase(),
                quote = quote.quote,
                author = quote.author,
                isWeekend = isWeekend(dateUpdated),
                date = dateUpdated,
            )
        }
        val itemsUpdated = when (monthType) {
            GetMonthType.SUBTRACT -> items + uiState.items
            GetMonthType.PLUS -> uiState.items + items
        }
        Timber.e("/// itemsUpdated ${itemsUpdated.map { it.date.monthValue }}")
        updateUiState(
            uiState.copy(
                items = itemsUpdated,
                latestPage = date,
                allowRefreshLatestPage = allowRefreshLatestPage ?: true,
                uiState = UiState.State.COMPLETE,
            ),
        )
    }*/
}

typealias DayDrawableResource = Pair<Int, Int>

data class DayCalendarUiState(
    val dayOfWeek: String = "",
    val quote: String = "",
    val author: String = "",
    val isWeekend: Boolean = false,
    val date: LocalDate = LocalDate.now(),
    override val uiState: UiState.State = UiState.State.INITIAL,
) : UiState {
    val colorRes = if (isWeekend) RudyRed else PersianBlue
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
            return when (date.dayOfMonth) {
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
        )

}
