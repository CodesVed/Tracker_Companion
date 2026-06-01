package com.example.trackercompanion.model.data

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
    val totalMatches: Int = wins + loss,
    val teamWins: Int,
    val points: Int,
    val notes: String?
) {
    fun winPercentageCalculator(): Float{
        return (wins/totalMatches)*100.toFloat()
    }

    fun pointsCalculator(): Int{
        val winPercent = winPercentageCalculator()
        return (wins+winPercent).toInt()
    }
}