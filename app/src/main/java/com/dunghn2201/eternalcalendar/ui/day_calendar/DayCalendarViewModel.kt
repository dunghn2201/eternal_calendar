package com.dunghn2201.eternalcalendar.ui.day_calendar

import android.app.Application
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

@HiltViewModel
class DayCalendarViewModel @Inject constructor(val application: Application, val moshi: Moshi) :
    BaseViewModel(application) {
    val now by lazy {
        LocalDate.now()
    }
    var uiState by mutableStateOf(DayCalendarUiState())
        private set

    init {
        fetchInitialItems()
    }

    private fun fetchInitialItems() {
        launchCoroutine {
            val quotes = getQuotes()
            val dateMonth = LocalDate.of(now.year, now.month, now.dayOfMonth)
            val totalDaysInMonth = dateMonth.lengthOfMonth()
            val items = List(totalDaysInMonth) { dayOfMonth ->
                val quote = quotes.random()
                val date = LocalDate.of(now.year, now.month, dayOfMonth + 1)
                val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("vi"))
                CalendarPagerItem(
                    day = dayOfMonth + 1,
                    month = now.monthValue,
                    year = now.year,
                    dayOfWeek = dayOfWeek.uppercase(),
                    quote = quote.quote,
                    author = quote.author,
                    isWeekend = isWeekend(date),
                )
            }
            uiState = uiState.copy(
                items = items,
                uiState = UiState.State.COMPLETE,
            )
        }
    }

    private fun getQuotes(): List<Quotations.Quotation> {
        val inputStream = application.resources.openRawResource(R.raw.quotations)
        val jsonContent = inputStream.bufferedReader().use { it.readText() }
        val adapter = moshi.adapter(Quotations::class.java)
        val quotesResponse = adapter.fromJson(jsonContent)
        return quotesResponse?.quotes.orEmpty()
    }

    data class DayCalendarUiState(
        val items: List<CalendarPagerItem> = listOf(),
        override val uiState: UiState.State = UiState.State.INITIAL
    ) : UiState
}

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
