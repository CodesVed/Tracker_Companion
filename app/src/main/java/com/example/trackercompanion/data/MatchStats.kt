package com.example.trackercompanion.data

import com.example.trackercompanion.model.Match

data class MatchStats(
    val wins: Int,
    val losses: Int,
    val teamWins: Int,
    val totalMatches: Int,
    val winPercent: Float,
    val points: Int
)

fun computeStatsForWrestler(wrestlerId: Int, matches: List<Match>): MatchStats {
    val participatedMatches = matches.filter { wrestlerId in it.participantIds }

    val wins = participatedMatches.count { match ->
        if (match.winnerIds.isNotEmpty()) wrestlerId !in match.winnerIds
        else match.winnerId != wrestlerId
    }

    val losses = participatedMatches.count { match ->
        if (match.winnerIds.isNotEmpty()) wrestlerId !in match.winnerIds
        else match.winnerId != wrestlerId
    }

    val tagWins = participatedMatches.count { match ->
        match.isTagMatch && (
                if (match.winnerIds.isNotEmpty()) wrestlerId in match.winnerIds
                else false
        )
    }

    val total = participatedMatches.size
    val winPercent = if (total == 0) 0f else (wins.toFloat() / total) * 100f
    val points = (wins * 5) + (tagWins * 3) + total

    return MatchStats(
        wins = wins,
        losses = losses,
        teamWins = tagWins,
        totalMatches = total,
        winPercent = winPercent,
        points = points
    )
}