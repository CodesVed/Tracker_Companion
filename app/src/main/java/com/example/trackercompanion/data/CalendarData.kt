package com.example.trackercompanion.data

import com.example.trackercompanion.model.CalendarWeek

object CalendarData {
    val weeks: List<CalendarWeek> = listOf(
        CalendarWeek(weekNumber = 1, showLabel = "RAW 1", notes = "Season Opener / Draft Night"),
        CalendarWeek(weekNumber = 2, showLabel = "SD 1", notes = "SDSeason Opener"),
        CalendarWeek(weekNumber = 3, showLabel = "RAW 2", notes = "WHC Tournament Round 1"),
        CalendarWeek(weekNumber = 4, showLabel = "SD 2", notes = "WWE Title Tournament Round 1"),
        CalendarWeek(weekNumber = 5, showLabel = "RAW 3"),
        CalendarWeek(weekNumber = 6, showLabel = "SD 3"),
    )
}