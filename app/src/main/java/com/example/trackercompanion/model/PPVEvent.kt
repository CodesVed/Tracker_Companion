package com.example.trackercompanion.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PPVEvent(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val ppvNumber: Int,
    val name: String,
    val notes: String = ""
)
