package com.example.trackercompanion.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CalendarWeek(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val weekNumber: Int,
    val showLabel: String,
    val linkedShowId: Int? = null,
    val linkedPPVId: Int? = null,
    val notes: String = "",
)
