package com.dunghn2201.eternalcalendar.model

data class Events(
    val events: List<Event>,
) {
    data class Event(
        val id: Int,
        val title: String,
        val dayLunar: Int,
        val daySolar: Int,
        val id_content: Int,
        val lunar: Boolean,
        val monthLunar: Int,
        val monthSolar: Int,
        val yearLunar: Int?,
        val yearSolar: Int,
    )
}
