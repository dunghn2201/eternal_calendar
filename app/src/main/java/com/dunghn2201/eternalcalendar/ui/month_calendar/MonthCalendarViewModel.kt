package com.dunghn2201.eternalcalendar.ui.month_calendar

import android.app.Application
import android.icu.util.ChineseCalendar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.dunghn2201.eternalcalendar.base.BaseViewModel
import com.dunghn2201.eternalcalendar.ui.theme.LightSkyBlue
import com.dunghn2201.eternalcalendar.util.extension.*
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import timber.log.Timber

@HiltViewModel
class MonthCalendarViewModel @Inject constructor(application: Application) : BaseViewModel(application) {
    var daysDisplay by mutableStateOf(listOf<DayCalendarItem>())
        private set

    init {
        getDaysDisplay()
    }

    private fun getDaysDisplay() {
        launchCoroutine {
            val localDateNow = LocalDate.now()
            val dayOfMonth = localDateNow.dayOfMonth
            val month = localDateNow.monthValue
            val year = localDateNow.year


            val totalDaysInTargetMonth = localDateNow.lengthOfMonth()
            Timber.e("/// currentDayOfMonth $dayOfMonth")
            Timber.e("/// currentMonth $month")
            Timber.e("/// currentYear $year")
            Timber.e("/// totalDaysInTargetMonth $totalDaysInTargetMonth")
            val currentMonth = List(totalDaysInTargetMonth) { index ->
                val localDate = LocalDate.of(year, month, index + 1)
                val dayOfMonthTarget = localDate.dayOfMonth
                val monthTarget = localDate.monthValue
                val yearTarget = localDate.year
                val lunarChineseDate = ChineseCalendar(localDate.convertLocalDateToDate())
                val dayLunar = lunarChineseDate.get(Calendar.DAY_OF_MONTH)
                val monthLunar = lunarChineseDate.get(Calendar.MONTH) + 1
                val yearCanChi = year.getCanChiYear()
                val monthCanChi = monthLunar.getCanChiMonth(yearCanChi)
                val dayCanChi = getCanChiDay(yearTarget, monthTarget, dayOfMonthTarget)
                DayCalendarItem(
                    dayOfMonth = dayOfMonthTarget,
                    month = month,
                    year = year,
                    dayLunar = dayLunar,
                    monthLunar = monthLunar,
                    dayOfWeek = localDate.getDayOfWeekVIFormat(),
                    yearCanChi = yearCanChi,
                    monthCanChi = monthCanChi,
                    dayCanChi = dayCanChi,
                    isWeekend = localDate.isWeekend(),
                    date = localDate,
                )
            }
            daysDisplay = currentMonth
            Timber.e("/// list $currentMonth")
        }
    }

    fun LocalDate.convertLocalDateToDate(): Date {
        val zoneId = ZoneId.systemDefault()
        val instant = atStartOfDay(zoneId).toInstant()
        return Date.from(instant)
    }

    fun LocalDate.isWeekend(): Boolean {
        val dayOfWeek: DayOfWeek = dayOfWeek
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY
    }

}

data class DayCalendarItem(
    val dayOfMonth: Int = 1,
    val month: Int = 1,
    val year: Int = 1,
    val dayLunar: Int = 1,
    val monthLunar: Int = 1,
    val dayOfWeek: String = "",
    val yearCanChi: String = "",
    val monthCanChi: String = "",
    val dayCanChi: String = "",
    val isWeekend: Boolean = false,
    val date: LocalDate = LocalDate.now(),
) {
    val ngayHoangDaoMsg get() = monthLunar.getNgayHoangDao(dayCanChi)
    val isNgayHoangDao get() = ngayHoangDaoMsg.length > 12
    val colorDayText get() = if (isWeekend) Color.Red else Color.Black
    val colorDot get() = if (isNgayHoangDao) Color.Red else Color.Black
    val isNow
        get() :Boolean {
            val date: LocalDate = LocalDate.now()
            val dayOfMonthTarget = date.dayOfMonth
            val monthTarget = date.monthValue
            val yearTarget = date.year
            return dayOfMonth == dayOfMonthTarget &&
                month == monthTarget &&
                year == yearTarget
        }
    val bgColor get() = if (isNow) LightSkyBlue else Color.White

}
