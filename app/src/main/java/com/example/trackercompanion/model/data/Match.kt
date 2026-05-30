package com.example.trackercompanion.model.data

import com.example.trackercompanion.model.enums.CardSlot
import com.example.trackercompanion.model.enums.Show

data class Match(
    val id: Int,
    val showId: Int,
    val showType: Show,
    val slot: CardSlot,
    val participants: String,
    val stipulation: String,
    val winnerId: Int?,
    val isTagMatch: Boolean,
    val notes: String?
)
