package com.example.trackercompanion.model.data

import com.example.trackercompanion.model.enums.Brand

data class ShowEpisode(
    val int: Int,
    val episodeNumber: Int,
    val brand: Brand,
    val weekNumber: Int,
    val notes: String?
)
