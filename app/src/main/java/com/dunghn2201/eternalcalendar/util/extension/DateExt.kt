package com.dunghn2201.eternalcalendar.util.extension

import android.icu.util.ChineseCalendar
import java.text.SimpleDateFormat
import java.util.*

fun Calendar.getDayOfWeek(): String {
    val dayOfWeekPattern = "EEEE"
    val sdf = SimpleDateFormat(dayOfWeekPattern, Locale("vi", "VN"))
    return sdf.format(this.time)
}

fun Calendar.isWeekend(): Boolean {
    return get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
}

fun Calendar.isSameDate(targetCalendar: Calendar): Boolean {
    val isSameDay = get(Calendar.DAY_OF_MONTH) == targetCalendar.get(Calendar.DAY_OF_MONTH)
    val isSameMonth = get(Calendar.MONTH) == targetCalendar.get(Calendar.MONTH)
    val isSameYear = get(Calendar.YEAR) == targetCalendar.get(Calendar.YEAR)
    return isSameDay && isSameMonth && isSameYear
}

fun Int.getCanChiYear(): String {
    val can = listOf("Canh", "Tân", "Nhâm", "Quý", "Giáp", "Ất", "Bính", "Đinh", "Mậu", "Kỷ")
    val chi = listOf("Thân", "Dậu", "Tuất", "Hợi", "Tý", "Sửu", "Dần", "Mão", "Thìn", "Tỵ", "Ngọ", "Mùi")
    return "${can[this % 10]} ${chi[this % 12]}"
}

fun Int.getCanChiMonth(yearCanChi: String): String {
    if (yearCanChi.isEmpty()) return ""
    val yearThienCan = yearCanChi.substringBefore(" ")
    return when (this) {
        1 -> {
            when {
                yearThienCan.matches1In2("Giáp", "Kỷ") -> "Bính Dần"
                yearThienCan.matches1In2("Ất", "Canh") -> "Mậu Dần"
                yearThienCan.matches1In2("Bính", "Tân") -> "Canh Dần"
                yearThienCan.matches1In2("Đinh", "Nhâm") -> "Nhâm Dần"
                yearThienCan.matches1In2("Mậu", "Quý") -> "Giáp Dần"
                else -> ""
            }
        }
        2 -> {
            when {
                yearThienCan.matches1In2("Giáp", "Kỷ") -> "Đinh Mão"
                yearThienCan.matches1In2("Ất", "Canh") -> "Kỷ Mão"
                yearThienCan.matches1In2("Bính", "Tân") -> "Tân Mão"
                yearThienCan.matches1In2("Đinh", "Nhâm") -> "Quý Mão"
                yearThienCan.matches1In2("Mậu", "Quý") -> "Ất Mão"
                else -> ""
            }
        }
        3 -> {
            when {
                yearThienCan.matches1In2("Giáp", "Kỷ") -> "Mậu Thìn"
                yearThienCan.matches1In2("Ất", "Canh") -> "Canh Thìn"
                yearThienCan.matches1In2("Bính", "Tân") -> "Nhâm Thìn"
                yearThienCan.matches1In2("Đinh", "Nhâm") -> "Giáp Thìn"
                yearThienCan.matches1In2("Mậu", "Quý") -> "Bính Thìn"
                else -> ""
            }
        }
        4 -> {
            when {
                yearThienCan.matches1In2("Giáp", "Kỷ") -> "Kỷ Tị"
                yearThienCan.matches1In2("Ất", "Canh") -> "Tân Tị"
                yearThienCan.matches1In2("Bính", "Tân") -> "Quý Tị"
                yearThienCan.matches1In2("Đinh", "Nhâm") -> "Ất Tị"
                yearThienCan.matches1In2("Mậu", "Quý") -> "Đinh Tị"
                else -> ""
            }
        }
        5 -> {
            when {
                yearThienCan.matches1In2("Giáp", "Kỷ") -> "Canh Ngọ"
                yearThienCan.matches1In2("Ất", "Canh") -> "Nhâm Ngọ"
                yearThienCan.matches1In2("Bính", "Tân") -> "Giáp Ngọ"
                yearThienCan.matches1In2("Đinh", "Nhâm") -> "Bính Ngọ"
                yearThienCan.matches1In2("Mậu", "Quý") -> "Mậu Ngọ"
                else -> ""
            }
        }
        6 -> {
            when {
                yearThienCan.matches1In2("Giáp", "Kỷ") -> "Tân Mùi"
                yearThienCan.matches1In2("Ất", "Canh") -> "Quý Mùi"
                yearThienCan.matches1In2("Bính", "Tân") -> "Ất Mùi"
                yearThienCan.matches1In2("Đinh", "Nhâm") -> "Đinh Mùi"
                yearThienCan.matches1In2("Mậu", "Quý") -> "Kỷ Mùi"
                else -> ""
            }
        }
        7 -> {
            when {
                yearThienCan.matches1In2("Giáp", "Kỷ") -> "Nhâm Thân"
                yearThienCan.matches1In2("Ất", "Canh") -> "Giáp Thân"
                yearThienCan.matches1In2("Bính", "Tân") -> "Bính Thân"
                yearThienCan.matches1In2("Đinh", "Nhâm") -> "Mậu Thân"
                yearThienCan.matches1In2("Mậu", "Quý") -> "Canh Thân"
                else -> ""
            }
        }
        8 -> {
            when {
                yearThienCan.matches1In2("Giáp", "Kỷ") -> "Quý Dậu"
                yearThienCan.matches1In2("Ất", "Canh") -> "Ất Dậu"
                yearThienCan.matches1In2("Bính", "Tân") -> "Đinh Dậu"
                yearThienCan.matches1In2("Đinh", "Nhâm") -> "Kỷ Dậu"
                yearThienCan.matches1In2("Mậu", "Quý") -> "Tân Dậu"
                else -> ""
            }
        }
        9 -> {
            when {
                yearThienCan.matches1In2("Giáp", "Kỷ") -> "Giáp Tuất"
                yearThienCan.matches1In2("Ất", "Canh") -> "Bính Tuất"
                yearThienCan.matches1In2("Bính", "Tân") -> "Mậu Tuất"
                yearThienCan.matches1In2("Đinh", "Nhâm") -> "Canh Tuất"
                yearThienCan.matches1In2("Mậu", "Quý") -> "Nhâm Tuất"
                else -> ""
            }
        }
        10 -> {
            when {
                yearThienCan.matches1In2("Giáp", "Kỷ") -> "Ất Hợi"
                yearThienCan.matches1In2("Ất", "Canh") -> "Đinh Hợi"
                yearThienCan.matches1In2("Bính", "Tân") -> "Kỷ Hợi"
                yearThienCan.matches1In2("Đinh", "Nhâm") -> "Tân Hợi"
                yearThienCan.matches1In2("Mậu", "Quý") -> "Quý Hợi"
                else -> ""
            }
        }
        11 -> {
            when {
                yearThienCan.matches1In2("Giáp", "Kỷ") -> "Bính Tý"
                yearThienCan.matches1In2("Ất", "Canh") -> "Mậu Tý"
                yearThienCan.matches1In2("Bính", "Tân") -> "Canh Tý"
                yearThienCan.matches1In2("Đinh", "Nhâm") -> "Nhâm Tý"
                yearThienCan.matches1In2("Mậu", "Quý") -> "Giáp Tý"
                else -> ""
            }
        }
        12 -> {
            when {
                yearThienCan.matches1In2("Giáp", "Kỷ") -> "Đinh Sửu"
                yearThienCan.matches1In2("Ất", "Canh") -> "Kỷ Sửu"
                yearThienCan.matches1In2("Bính", "Tân") -> "Tân Sửu"
                yearThienCan.matches1In2("Đinh", "Nhâm") -> "Quý Sửu"
                yearThienCan.matches1In2("Mậu", "Quý") -> "Ất Sửu"
                else -> ""
            }
        }
        else -> ""
    }
}

fun Int.getCanChiHour(ngayCanChi: String): String {
    if (ngayCanChi.isEmpty()) return ""
    val yearThienCan = ngayCanChi.substringBefore(" ")
    return when (this) {
        23, 0 -> {
            when {
                yearThienCan.matches1In2("Giáp", "Kỷ") -> "Giáp Tý"
                yearThienCan.matches1In2("Ất", "Canh") -> "Bính Tý"
                yearThienCan.matches1In2("Bính", "Tân") -> "Mậu Tý"
                yearThienCan.matches1In2("Đinh", "Nhâm") -> "Canh Tý"
                yearThienCan.matches1In2("Mậu", "Quý") -> "Nhâm Tý"
                else -> ""
            }
        }
        1, 2 -> {
            when {
                yearThienCan.matches1In2("Giáp", "Kỷ") -> "Ất Sửu"
                yearThienCan.matches1In2("Ất", "Canh") -> "Đinh Sửu"
                yearThienCan.matches1In2("Bính", "Tân") -> "Kỷ Sửu"
                yearThienCan.matches1In2("Đinh", "Nhâm") -> "Tân Sửu"
                yearThienCan.matches1In2("Mậu", "Quý") -> "Quý Sửu"
                else -> ""
            }
        }
        3, 4 -> {
            when {
                yearThienCan.matches1In2("Giáp", "Kỷ") -> "Bính Dần"
                yearThienCan.matches1In2("Ất", "Canh") -> "Mậu Dần"
                yearThienCan.matches1In2("Bính", "Tân") -> "Canh Dần"
                yearThienCan.matches1In2("Đinh", "Nhâm") -> "Nhâm Dần"
                yearThienCan.matches1In2("Mậu", "Quý") -> "Giáp Dần"
                else -> ""
            }
        }
        5, 6 -> {
            when {
                yearThienCan.matches1In2("Giáp", "Kỷ") -> "Đinh Mão"
                yearThienCan.matches1In2("Ất", "Canh") -> "Kỷ Mão"
                yearThienCan.matches1In2("Bính", "Tân") -> "Tân Mão"
                yearThienCan.matches1In2("Đinh", "Nhâm") -> "Quý Mão"
                yearThienCan.matches1In2("Mậu", "Quý") -> "Ất Mão"
                else -> ""
            }
        }
        7, 8 -> {
            when {
                yearThienCan.matches1In2("Giáp", "Kỷ") -> "Mậu Thìn"
                yearThienCan.matches1In2("Ất", "Canh") -> "Canh Thìn"
                yearThienCan.matches1In2("Bính", "Tân") -> "Nhâm Thìn"
                yearThienCan.matches1In2("Đinh", "Nhâm") -> "Giáp Thìn"
                yearThienCan.matches1In2("Mậu", "Quý") -> "Bính Thìn"
                else -> ""
            }
        }
        9, 10 -> {
            when {
                yearThienCan.matches1In2("Giáp", "Kỷ") -> "Kỷ Tị"
                yearThienCan.matches1In2("Ất", "Canh") -> "Tân Tị"
                yearThienCan.matches1In2("Bính", "Tân") -> "Quý Tị"
                yearThienCan.matches1In2("Đinh", "Nhâm") -> "Ất Tị"
                yearThienCan.matches1In2("Mậu", "Quý") -> "Đinh Tị"
                else -> ""
            }
        }
        11, 12 -> {
            when {
                yearThienCan.matches1In2("Giáp", "Kỷ") -> "Canh Ngọ"
                yearThienCan.matches1In2("Ất", "Canh") -> "Nhâm Ngọ"
                yearThienCan.matches1In2("Bính", "Tân") -> "Giáp Ngọ"
                yearThienCan.matches1In2("Đinh", "Nhâm") -> "Bính Ngọ"
                yearThienCan.matches1In2("Mậu", "Quý") -> "Mậu Ngọ"
                else -> ""
            }
        }
        13, 14 -> {
            when {
                yearThienCan.matches1In2("Giáp", "Kỷ") -> "Tân Mùi"
                yearThienCan.matches1In2("Ất", "Canh") -> "Quý Mùi"
                yearThienCan.matches1In2("Bính", "Tân") -> "Ất Mùi"
                yearThienCan.matches1In2("Đinh", "Nhâm") -> "Đinh Mùi"
                yearThienCan.matches1In2("Mậu", "Quý") -> "Kỷ Mùi"
                else -> ""
            }
        }
        15, 16 -> {
            when {
                yearThienCan.matches1In2("Giáp", "Kỷ") -> "Nhâm Thân"
                yearThienCan.matches1In2("Ất", "Canh") -> "Giáp Thân"
                yearThienCan.matches1In2("Bính", "Tân") -> "Bính Thân"
                yearThienCan.matches1In2("Đinh", "Nhâm") -> "Mậu Thân"
                yearThienCan.matches1In2("Mậu", "Quý") -> "Canh Thân"
                else -> ""
            }
        }
        17, 18 -> {
            when {
                yearThienCan.matches1In2("Giáp", "Kỷ") -> "Quý Dậu"
                yearThienCan.matches1In2("Ất", "Canh") -> "Ất Dậu"
                yearThienCan.matches1In2("Bính", "Tân") -> "Đinh Dậu"
                yearThienCan.matches1In2("Đinh", "Nhâm") -> "Kỷ Dậu"
                yearThienCan.matches1In2("Mậu", "Quý") -> "Tân Dậu"
                else -> ""
            }
        }
        19, 20 -> {
            when {
                yearThienCan.matches1In2("Giáp", "Kỷ") -> "Giáp Tuất"
                yearThienCan.matches1In2("Ất", "Canh") -> "Bính Tuất"
                yearThienCan.matches1In2("Bính", "Tân") -> "Mậu Tuất"
                yearThienCan.matches1In2("Đinh", "Nhâm") -> "Canh Tuất"
                yearThienCan.matches1In2("Mậu", "Quý") -> "Nhâm Tuất"
                else -> ""
            }
        }
        21, 22 -> {
            when {
                yearThienCan.matches1In2("Giáp", "Kỷ") -> "Ất Dậu"
                yearThienCan.matches1In2("Ất", "Canh") -> "Đinh Hợi"
                yearThienCan.matches1In2("Bính", "Tân") -> "Kỷ Hợi"
                yearThienCan.matches1In2("Đinh", "Nhâm") -> "Tân Hợi"
                yearThienCan.matches1In2("Mậu", "Quý") -> "Quý Hợi"
                else -> ""
            }
        }
        else -> ""
    }
}


fun String.matches1In2(yearThienCan1: String, yearThienCan2: String) = this == yearThienCan1 || this == yearThienCan2
