package com.dunghn2201.eternalcalendar.util.extension

import android.icu.util.ChineseCalendar
import java.text.SimpleDateFormat
import java.util.*
import timber.log.Timber

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
                yearThienCan.matches1In2("Giáp", "Kỷ") -> "Kỷ Tỵ"
                yearThienCan.matches1In2("Ất", "Canh") -> "Tân Tỵ"
                yearThienCan.matches1In2("Bính", "Tân") -> "Quý Tỵ"
                yearThienCan.matches1In2("Đinh", "Nhâm") -> "Ất Tỵ"
                yearThienCan.matches1In2("Mậu", "Quý") -> "Đinh Tỵ"
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

fun getCanChiDay(targetYear: Int, targetMonth: Int, targetDay: Int): String {
    val thapNhiDiaChi = listOf("Tý", "Sửu", "Dần", "Mão", "Thìn", "Tỵ", "Ngọ", "Mùi", "Thân", "Dậu", "Tuất", "Hợi")
    val thapThienCan = listOf("Canh", "Tân", "Nhâm", "Quý", "Giáp", "Ất", "Bính", "Đinh", "Mậu", "Kỷ")
    val yearLastTwoDigits = targetYear % 100
    val targetCentury = targetYear.toString().substring(0, 2).toInt()
    var month = (targetMonth / 2)
    println("month 1 ${month % 2 == 0}")

    if (targetMonth % 2 == 0) month += 30

    if (targetMonth == 9 || targetMonth == 11) month += 1
    println("month final $month")

    val year = 5 * ((yearLastTwoDigits % 60) % 12) + (yearLastTwoDigits / 4)
    // 16cen -> 21
    val century = 33 - 16 * (targetCentury % 4) - 3 * (targetCentury / 4)
    val wrongNum = when {
        targetMonth > 2 -> -2
        else -> if (targetYear % 4 == 0) -1 else 0
    }
    val result = targetDay + month + year + century + wrongNum
    return thapThienCan[result % 10] + " " + thapNhiDiaChi[result % 12]
}

fun Int.getCanChiHour(ngayCanChi: String): String {
    if (ngayCanChi.isEmpty()) return ""
    val ngayThienCan = ngayCanChi.substringBefore(" ")
    return when (this) {
        23, 0 -> {
            when {
                ngayThienCan.matches1In2("Giáp", "Kỷ") -> "Giáp Tý"
                ngayThienCan.matches1In2("Ất", "Canh") -> "Bính Tý"
                ngayThienCan.matches1In2("Bính", "Tân") -> "Mậu Tý"
                ngayThienCan.matches1In2("Đinh", "Nhâm") -> "Canh Tý"
                ngayThienCan.matches1In2("Mậu", "Quý") -> "Nhâm Tý"
                else -> ""
            }
        }
        1, 2 -> {
            when {
                ngayThienCan.matches1In2("Giáp", "Kỷ") -> "Ất Sửu"
                ngayThienCan.matches1In2("Ất", "Canh") -> "Đinh Sửu"
                ngayThienCan.matches1In2("Bính", "Tân") -> "Kỷ Sửu"
                ngayThienCan.matches1In2("Đinh", "Nhâm") -> "Tân Sửu"
                ngayThienCan.matches1In2("Mậu", "Quý") -> "Quý Sửu"
                else -> ""
            }
        }
        3, 4 -> {
            when {
                ngayThienCan.matches1In2("Giáp", "Kỷ") -> "Bính Dần"
                ngayThienCan.matches1In2("Ất", "Canh") -> "Mậu Dần"
                ngayThienCan.matches1In2("Bính", "Tân") -> "Canh Dần"
                ngayThienCan.matches1In2("Đinh", "Nhâm") -> "Nhâm Dần"
                ngayThienCan.matches1In2("Mậu", "Quý") -> "Giáp Dần"
                else -> ""
            }
        }
        5, 6 -> {
            when {
                ngayThienCan.matches1In2("Giáp", "Kỷ") -> "Đinh Mão"
                ngayThienCan.matches1In2("Ất", "Canh") -> "Kỷ Mão"
                ngayThienCan.matches1In2("Bính", "Tân") -> "Tân Mão"
                ngayThienCan.matches1In2("Đinh", "Nhâm") -> "Quý Mão"
                ngayThienCan.matches1In2("Mậu", "Quý") -> "Ất Mão"
                else -> ""
            }
        }
        7, 8 -> {
            when {
                ngayThienCan.matches1In2("Giáp", "Kỷ") -> "Mậu Thìn"
                ngayThienCan.matches1In2("Ất", "Canh") -> "Canh Thìn"
                ngayThienCan.matches1In2("Bính", "Tân") -> "Nhâm Thìn"
                ngayThienCan.matches1In2("Đinh", "Nhâm") -> "Giáp Thìn"
                ngayThienCan.matches1In2("Mậu", "Quý") -> "Bính Thìn"
                else -> ""
            }
        }
        9, 10 -> {
            when {
                ngayThienCan.matches1In2("Giáp", "Kỷ") -> "Kỷ Tỵ"
                ngayThienCan.matches1In2("Ất", "Canh") -> "Tân Tỵ"
                ngayThienCan.matches1In2("Bính", "Tân") -> "Quý Tỵ"
                ngayThienCan.matches1In2("Đinh", "Nhâm") -> "Ất Tỵ"
                ngayThienCan.matches1In2("Mậu", "Quý") -> "Đinh Tỵ"
                else -> ""
            }
        }
        11, 12 -> {
            when {
                ngayThienCan.matches1In2("Giáp", "Kỷ") -> "Canh Ngọ"
                ngayThienCan.matches1In2("Ất", "Canh") -> "Nhâm Ngọ"
                ngayThienCan.matches1In2("Bính", "Tân") -> "Giáp Ngọ"
                ngayThienCan.matches1In2("Đinh", "Nhâm") -> "Bính Ngọ"
                ngayThienCan.matches1In2("Mậu", "Quý") -> "Mậu Ngọ"
                else -> ""
            }
        }
        13, 14 -> {
            when {
                ngayThienCan.matches1In2("Giáp", "Kỷ") -> "Tân Mùi"
                ngayThienCan.matches1In2("Ất", "Canh") -> "Quý Mùi"
                ngayThienCan.matches1In2("Bính", "Tân") -> "Ất Mùi"
                ngayThienCan.matches1In2("Đinh", "Nhâm") -> "Đinh Mùi"
                ngayThienCan.matches1In2("Mậu", "Quý") -> "Kỷ Mùi"
                else -> ""
            }
        }
        15, 16 -> {
            when {
                ngayThienCan.matches1In2("Giáp", "Kỷ") -> "Nhâm Thân"
                ngayThienCan.matches1In2("Ất", "Canh") -> "Giáp Thân"
                ngayThienCan.matches1In2("Bính", "Tân") -> "Bính Thân"
                ngayThienCan.matches1In2("Đinh", "Nhâm") -> "Mậu Thân"
                ngayThienCan.matches1In2("Mậu", "Quý") -> "Canh Thân"
                else -> ""
            }
        }
        17, 18 -> {
            when {
                ngayThienCan.matches1In2("Giáp", "Kỷ") -> "Quý Dậu"
                ngayThienCan.matches1In2("Ất", "Canh") -> "Ất Dậu"
                ngayThienCan.matches1In2("Bính", "Tân") -> "Đinh Dậu"
                ngayThienCan.matches1In2("Đinh", "Nhâm") -> "Kỷ Dậu"
                ngayThienCan.matches1In2("Mậu", "Quý") -> "Tân Dậu"
                else -> ""
            }
        }
        19, 20 -> {
            when {
                ngayThienCan.matches1In2("Giáp", "Kỷ") -> "Giáp Tuất"
                ngayThienCan.matches1In2("Ất", "Canh") -> "Bính Tuất"
                ngayThienCan.matches1In2("Bính", "Tân") -> "Mậu Tuất"
                ngayThienCan.matches1In2("Đinh", "Nhâm") -> "Canh Tuất"
                ngayThienCan.matches1In2("Mậu", "Quý") -> "Nhâm Tuất"
                else -> ""
            }
        }
        21, 22 -> {
            when {
                ngayThienCan.matches1In2("Giáp", "Kỷ") -> "Ất Dậu"
                ngayThienCan.matches1In2("Ất", "Canh") -> "Đinh Hợi"
                ngayThienCan.matches1In2("Bính", "Tân") -> "Kỷ Hợi"
                ngayThienCan.matches1In2("Đinh", "Nhâm") -> "Tân Hợi"
                ngayThienCan.matches1In2("Mậu", "Quý") -> "Quý Hợi"
                else -> ""
            }
        }
        else -> ""
    }
}

fun Int.getNgayHoangDao(ngayCanChi: String): String {
    if (ngayCanChi.isEmpty()) return ""
    val ngayThienCan = ngayCanChi.substringAfter(" ")
    val hoangDao = "Ngày Hoàng Đạo"
    val hacDao = "Ngày Hắc Đạo"
    return when (this) {
        1, 7 -> {
            when (ngayThienCan) {
                "Tý" -> hoangDao
                "Sửu" -> hoangDao
                "Dần" -> hacDao
                "Mão" -> hacDao
                "Thìn" -> hoangDao
                "Tỵ" -> hoangDao
                "Ngọ" -> hacDao
                "Mùi" -> hoangDao
                "Thân" -> hacDao
                "Dậu" -> hacDao
                "Tuất" -> hoangDao
                "Hợi" -> hacDao
                else -> ""
            }
        }
        2, 8 -> {
            when (ngayThienCan) {
                "Dần" -> hoangDao
                "Mão" -> hoangDao
                "Thìn" -> hacDao
                "Tỵ" -> hacDao
                "Ngọ" -> hoangDao
                "Mùi" -> hoangDao
                "Thân" -> hacDao
                "Dậu" -> hoangDao
                "Tuất" -> hacDao
                "Hợi" -> hacDao
                "Tý" -> hoangDao
                "Sửu" -> hacDao
                else -> ""
            }
        }
        3, 9 -> {
            when (ngayThienCan) {
                "Thìn" -> hoangDao
                "Tỵ" -> hoangDao
                "Ngọ" -> hacDao
                "Mùi" -> hacDao
                "Thân" -> hoangDao
                "Dậu" -> hoangDao
                "Tuất" -> hacDao
                "Hợi" -> hoangDao
                "Tý" -> hacDao
                "Sửu" -> hacDao
                "Dần" -> hoangDao
                "Mão" -> hacDao
                else -> ""
            }
        }
        4, 10 -> {
            when (ngayThienCan) {
                "Ngọ" -> hoangDao
                "Mùi" -> hoangDao
                "Thân" -> hacDao
                "Dậu" -> hacDao
                "Tuất" -> hoangDao
                "Hợi" -> hoangDao
                "Tý" -> hacDao
                "Sửu" -> hoangDao
                "Dần" -> hacDao
                "Mão" -> hacDao
                "Thìn" -> hoangDao
                "Tỵ" -> hacDao
                else -> ""
            }
        }
        5, 11 -> {
            when (ngayThienCan) {
                "Thân" -> hoangDao
                "Dậu" -> hoangDao
                "Tuất" -> hacDao
                "Hợi" -> hacDao
                "Tý" -> hoangDao
                "Sửu" -> hoangDao
                "Dần" -> hacDao
                "Mão" -> hoangDao
                "Thìn" -> hacDao
                "Tỵ" -> hacDao
                "Ngọ" -> hoangDao
                "Mùi" -> hacDao
                else -> ""
            }
        }
        6, 12 -> {
            when (ngayThienCan) {
                "Tuất" -> hoangDao
                "Hợi" -> hoangDao
                "Tý" -> hacDao
                "Sửu" -> hacDao
                "Dần" -> hoangDao
                "Mão" -> hoangDao
                "Thìn" -> hacDao
                "Tỵ" -> hoangDao
                "Ngọ" -> hacDao
                "Mùi" -> hacDao
                "Thân" -> hoangDao
                "Dậu" -> hacDao
                else -> ""
            }
        }
        else -> ""
    }
}

fun String.matches1In2(yearThienCan1: String, yearThienCan2: String) = this == yearThienCan1 || this == yearThienCan2
