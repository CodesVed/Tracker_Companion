package com.example.trackercompanion.model.data

import com.example.trackercompanion.model.Match
import com.example.trackercompanion.model.PPVEvent
import com.example.trackercompanion.model.ShowEpisode
import com.example.trackercompanion.model.enums.Brand
import com.example.trackercompanion.model.enums.CardSlot
import com.example.trackercompanion.model.enums.Show

object ShowData {
    val episodes: List<ShowEpisode> = listOf(
        ShowEpisode(id = 1, episodeNumber = 1, brand = Brand.RAW, weekNumber = 1, notes = "WWE Draft Night / Season Opener"),
        ShowEpisode(id = 2, episodeNumber = 1, brand = Brand.SD, weekNumber = 2, notes = "SD Season Opener"),
        ShowEpisode(id = 3, episodeNumber = 2, brand = Brand.RAW, weekNumber = 3, notes = "WHC Tournament Round 1"),
        ShowEpisode(id = 4, episodeNumber = 2, brand = Brand.SD, weekNumber = 4, notes = "WWE Title Tournament Round 1"),
        ShowEpisode(id = 5, episodeNumber = 3, brand = Brand.RAW, weekNumber = 5),
        ShowEpisode(id = 6, episodeNumber = 3, brand = Brand.SD, weekNumber = 6),
    )

    val ppvEvents: List<PPVEvent> = listOf(
        PPVEvent(id = 101, ppvNumber = 1, name = "Royal Rumble", notes = "The Rock won entering at #22"),
    )

    val matches: List<Match> = listOf(
        Match(id = 1, showId = 1, showType = Show.SHOW, slot = CardSlot.OPEN,
            participants = "Goldberg vs Goldust", stipulation = "Normal", winnerId = 5, winnerLabel = "Goldberg"),
        Match(id = 2, showId = 1, showType = Show.SHOW, slot = CardSlot.MID,
            participants = "Booker T vs Matt Hardy", stipulation = "Normal", winnerId = 6, winnerLabel = "Booker T"),
        Match(id = 3, showId = 1, showType = Show.SHOW, slot = CardSlot.MID,
            participants = "The Dudley Boys vs Los Guerreros", stipulation = "Normal", winnerId = 7, winnerLabel = "The Dudley Boys", isTagMatch = true),
        Match(id = 4, showId = 1, showType = Show.SHOW, slot = CardSlot.UPPER,
            participants = "Kane vs RVD", stipulation = "Normal", winnerId = 4, winnerLabel = "Kane"),
        Match(id = 5, showId = 1, showType = Show.SHOW, slot = CardSlot.MAIN,
            participants = "Steve Austin vs Shawn Michaels", stipulation = "Normal", winnerId = 1, winnerLabel = "Steve Austin", notes = "WHC Tournament Round 1"),
    )

    fun getMatchesForShow(showId: Int) = matches.filter { it.showId == showId }
    fun getMatchesForEpisode(episodeId: Int) = matches.filter {
        it.showId == episodeId && it.showType.toString() == "SHOW"
    }
    fun getMatchesForPPV(ppvId: Int) = matches.filter {
        it.showId == ppvId && it.showType.toString() == "PPV"
    }
}