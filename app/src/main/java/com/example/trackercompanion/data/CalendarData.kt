package com.example.trackercompanion.data

import com.example.trackercompanion.model.CalendarWeek

object CalendarData {
    val weeks: List<CalendarWeek> = listOf(
        CalendarWeek(id = 1, weekNumber = 1, showLabel = "RAW 1", linkedShowId = 1, notes = "Season Opener / Draft Night"),
        CalendarWeek(id = 2, weekNumber = 1, showLabel = "SD 1", linkedShowId = 2, notes = "SD Season Opener"),
        CalendarWeek(id = 3, weekNumber = 2, showLabel = "RAW 2", linkedShowId = 3, notes = "WHC Tournament Round 1"),
        CalendarWeek(id = 4, weekNumber = 2, showLabel = "SD 2", linkedShowId = 4, notes = "WWE Title Tournament Round 1"),
        CalendarWeek(id = 5, weekNumber = 3, showLabel = "RAW 3", linkedShowId = 5),
        CalendarWeek(id = 6, weekNumber = 3, showLabel = "SD 3", linkedShowId = 6),
        CalendarWeek(id = 7, weekNumber = 11, showLabel = "Royal Rumble", linkedPPVId = 1001)
    )
}