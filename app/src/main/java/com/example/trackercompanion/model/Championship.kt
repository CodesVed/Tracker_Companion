package com.example.trackercompanion.model

data class Championship(
    val id: Int,
    val title: String,
    val championId: Int?,
    val championName: String?,
    val reignNumber: Int,
    val reignStartEpisode: String,
    val defenses: Int = 0,
    val notes: String = ""
)
