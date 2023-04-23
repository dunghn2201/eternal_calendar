package com.dunghn2201.eternalcalendar.model

import com.dunghn2201.eternalcalendar.R
import com.dunghn2201.eternalcalendar.ui.theme.PersianBlue
import com.dunghn2201.eternalcalendar.ui.theme.RudyRed
import java.time.LocalDate

typealias DayDrawableResource = Pair<Int, Int>

data class CalendarPagerItem(
    val dayOfWeek: String,
    val quote: String,
    val author: String,
    val isWeekend: Boolean,
    val date: LocalDate,
) {
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
}
