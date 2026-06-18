package com.example.trackercompanion.model

data class CalendarWeek(
    val weekNumber: Int,
    val showLabel: String,
    val linkedShowId: Int? = null,
    val linkedPPVId: Int? = null,
    val notes: String = "",
)
