package com.dunghn2201.eternalcalendar.model

data class Quotations(
    val quotes: List<Quotation>,
) {
    data class Quotation(
        val quote: String,
        val author: String,
    )
}
