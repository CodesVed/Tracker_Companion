package com.example.trackercompanion.model.data

data class Championship(
    val id: Int,
    val title: String,
    val championId: Int,
    val reignNumber: Int,
    val reignStartEpisode: String?,
    val defenses: Int,
    val notes: String?
)
