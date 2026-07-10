package com.example.trackercompanion.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.trackercompanion.model.enums.CardSlot
import com.example.trackercompanion.model.enums.Show

@Entity
data class Match(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val showId: Int,
    val showType: Show,
    val slot: CardSlot,
    val participants: String,
    val stipulation: String,
    val winnerId: Int?,
    val winnerLabel: String?,
    val isTagMatch: Boolean = false,
    val participantIds: List<Int>,
    val winnerIds: List<Int>,
    val notes: String = ""
)
