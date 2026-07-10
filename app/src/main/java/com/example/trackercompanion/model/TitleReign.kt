package com.example.trackercompanion.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TitleReign(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val titleId: Int,
    val titleName: String,
    val reignNumber: Int,
    val holderIds: List<Int>,
    val holderNames: List<String>,
    val holderImages: List<Int> = listOf(),
    val wonAtEvent: String,
    val lostAtEvent: String?,
    val defenses: Int = 0,
    val notes: String = ""
)
