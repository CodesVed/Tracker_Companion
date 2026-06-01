package com.example.trackercompanion.model

import com.example.trackercompanion.model.enums.Brand

data class ShowEpisode(
    val id: Int,
    val episodeNumber: Int,
    val brand: Brand,
    val weekNumber: Int,
    val notes: String = ""
)
