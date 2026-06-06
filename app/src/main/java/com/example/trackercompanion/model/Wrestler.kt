package com.example.trackercompanion.model

import androidx.annotation.DrawableRes
import com.example.trackercompanion.model.enums.Brand
import com.example.trackercompanion.model.enums.Status
import com.example.trackercompanion.model.enums.Type

data class Wrestler(
    val id: Int,
    val name: String,
    val brand: Brand,
    val status: Status,
    val type: Type,
    val wins: Int,
    val loss: Int,
    val teamWins: Int,
    val imageRes: Int,
    val notes: String = "",
) {
    val totalMatches: Int
        get() = wins + loss

    val winPercentage: Float
        get() = if (totalMatches == 0) 0f else (wins.toFloat()/totalMatches)*100f

    val points: Int
        get() = (wins*5) + (teamWins*3) + totalMatches
}