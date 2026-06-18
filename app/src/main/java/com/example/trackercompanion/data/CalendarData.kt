package com.example.trackercompanion.data

import com.example.trackercompanion.model.CalendarWeek

object CalendarData {
    val weeks: List<CalendarWeek> = listOf(
        CalendarWeek(weekNumber = 1, showLabel = "RAW 1", linkedShowId = 1, notes = "Season Opener / Draft Night"),
        CalendarWeek(weekNumber = 2, showLabel = "SD 1", linkedShowId = 2, notes = "SD Season Opener"),
        CalendarWeek(weekNumber = 3, showLabel = "RAW 2", linkedShowId = 3, notes = "WHC Tournament Round 1"),
        CalendarWeek(weekNumber = 4, showLabel = "SD 2", linkedShowId = 4, notes = "WWE Title Tournament Round 1"),
        CalendarWeek(weekNumber = 5, showLabel = "RAW 3", linkedShowId = 5),
        CalendarWeek(weekNumber = 6, showLabel = "SD 3", linkedShowId = 6),
        CalendarWeek(weekNumber = 11, showLabel = "Royal Rumble", linkedPPVId = 1001)
    )
}