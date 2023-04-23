package com.dunghn2201.eternalcalendar.ui.day_calendar

import android.app.Application
import androidx.annotation.RawRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.dunghn2201.eternalcalendar.R
import com.dunghn2201.eternalcalendar.base.BaseViewModel
import com.dunghn2201.eternalcalendar.model.CalendarPagerItem
import com.dunghn2201.eternalcalendar.model.Quotations
import com.dunghn2201.eternalcalendar.util.extension.UiState
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*
import javax.inject.Inject
import timber.log.Timber

@HiltViewModel
class DayCalendarViewModel @Inject constructor(val application: Application, val moshi: Moshi) :
    BaseViewModel(application) {
    var uiState by mutableStateOf(DayCalendarUiState())
        private set

    val now: LocalDate by lazy {
        LocalDate.now()
    }
    var quoteList: List<Quotations.Quotation> = emptyList()

    init {
        fetchInitialItems()
    }

    private fun fetchInitialItems() {
        launchCoroutine {
            val quotes = getQuotes()
            quoteList = quotes
            val dateMonth = LocalDate.of(now.year, now.month, now.dayOfMonth)
            val totalDaysInMonth = dateMonth.lengthOfMonth()
            val items = List(totalDaysInMonth) { dayOfMonth ->
                val quote = quotes.random()
                val date = LocalDate.of(now.year, now.month, dayOfMonth + 1)
                val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("vi"))
                CalendarPagerItem(
                    dayOfWeek = dayOfWeek.uppercase(),
                    quote = quote.quote,
                    author = quote.author,
                    isWeekend = isWeekend(date),
                    date = date,
                )
            }
            updateUiState(
                uiState.copy(
                    items = items,
                    uiState = UiState.State.COMPLETE,
                ),
            )
        }
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

    fun updateUiState(dayCalendarUiState: DayCalendarUiState) {
        uiState = dayCalendarUiState
    }

    fun getMoreDaysByMonth(date: LocalDate, monthType: GetMonthType, allowRefreshLatestPage: Boolean? = null) {
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
    }
}

enum class GetMonthType {
    SUBTRACT,
    PLUS
}

data class DayCalendarUiState(
    val items: List<CalendarPagerItem> = listOf(),
    val latestPage: LocalDate? = null,
    val allowRefreshLatestPage: Boolean = false,
    override val uiState: UiState.State = UiState.State.INITIAL,
) : UiState

val bgCalendar =
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
