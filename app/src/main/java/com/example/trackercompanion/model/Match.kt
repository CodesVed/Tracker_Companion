package com.example.trackercompanion.model

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
    val winnerLabel: String?,
    val isTagMatch: Boolean = false,
    val participantIds: List<Int> = emptyList(),    //for tag matches
    val winnerIds: List<Int> = emptyList(),         //for tag match winners
    val notes: String = ""
)
