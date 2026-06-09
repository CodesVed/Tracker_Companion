package com.example.trackercompanion.data

import com.example.trackercompanion.model.Match
import com.example.trackercompanion.model.PPVEvent
import com.example.trackercompanion.model.ShowEpisode
import com.example.trackercompanion.model.enums.Brand
import com.example.trackercompanion.model.enums.CardSlot
import com.example.trackercompanion.model.enums.Show

object ShowData {
    val episodes: List<ShowEpisode> = listOf(
        ShowEpisode(
            id = 1,
            episodeNumber = 1,
            brand = Brand.RAW,
            weekNumber = 1,
            notes = "WHC Tournament Opener"
        ),
        ShowEpisode(
            id = 2,
            episodeNumber = 1,
            brand = Brand.SD,
            weekNumber = 1,
            notes = "WWE Championship tournament opener"
        ),
        ShowEpisode(
            id = 3,
            episodeNumber = 2,
            brand = Brand.RAW,
            weekNumber = 2,
            notes = "WHC Tournament Round 1"
        ),
        ShowEpisode(
            id = 4,
            episodeNumber = 2,
            brand = Brand.SD,
            weekNumber = 2,
            notes = "WWE Title Tournament Round 1"
        ),
        ShowEpisode(
            id = 5,
            episodeNumber = 3,
            brand = Brand.RAW,
            weekNumber = 3,
            notes = "WHC Tournament Round 2"
        ),
        ShowEpisode(
            id = 6,
            episodeNumber = 3,
            brand = Brand.SD,
            weekNumber = 3,
            notes = "WWE Championship Tournament Round 2"
        ),
    )

    val ppvEvents: List<PPVEvent> = listOf(
        PPVEvent(
            id = 1001,
            ppvNumber = 1,
            name = "Royal Rumble",
            notes = "The Rock won entering at #22"
        ),
    )

    val matches: List<Match> = listOf(
        // RAW matches
        Match(
            id = 1,
            showId = 1,
            showType = Show.SHOW,
            slot = CardSlot.OPEN,
            participants = "Goldberg vs Shawn Michaels",
            participantIds = listOf(19, 47),
            stipulation = "Normal",
            winnerId = 47,
            winnerLabel = "Shawn Michaels",
            winnerIds = listOf(47),
            notes = "Tourney Round 1"
        ),
        Match(
            id = 2,
            showId = 1,
            showType = Show.SHOW,
            slot = CardSlot.MID,
            participants = "Randy vs Test",
            participantIds = listOf(34, 55),
            stipulation = "Normal",
            winnerId = 34,
            winnerLabel = "Orton",
            winnerIds = listOf(34),
            notes = "Tourney Round 1"
        ),
        Match(
            id = 3,
            showId = 1,
            showType = Show.SHOW,
            slot = CardSlot.MID,
            participants = "Venis vs Jericho",
            participantIds = listOf(64, 12),
            stipulation = "Normal",
            winnerId = 12,
            winnerIds = listOf(12),
            winnerLabel = "Jericho",
            notes = "Tourney Round 1"
        ),
        Match(
            id = 4,
            showId = 1,
            showType = Show.SHOW,
            slot = CardSlot.MID,
            participants = "Kane vs Rico",
            participantIds = listOf(27, 38),
            stipulation = "Normal",
            winnerId = 27,
            winnerIds = listOf(27),
            winnerLabel = "Kane",
            notes = "Tourney Round 1"
        ),
        Match(
            id = 5,
            showId = 1,
            showType = Show.SHOW,
            slot = CardSlot.UPPER,
            participants = "Christian vs The Undertaker",
            participantIds = listOf(13, 58),
            stipulation = "Normal",
            winnerId = 58,
            winnerIds = listOf(58),
            winnerLabel = "The Undertaker",
            notes = "Tourney Round 1"
        ),
        Match(
            id = 6,
            showId = 1,
            showType = Show.SHOW,
            slot = CardSlot.UPPER,
            participants = "Batista vs Lance",
            participantIds = listOf(4, 30),
            stipulation = "Normal",
            winnerId = 4,
            winnerIds = listOf(4),
            winnerLabel = "Batista",
            notes = "Tourney Round 1"
        ),
        Match(
            id = 7,
            showId = 1,
            showType = Show.SHOW,
            slot = CardSlot.MAIN,
            participants = "Triple H vs Matt",
            participantIds = listOf(60, 32),
            stipulation = "Normal",
            winnerId = 60,
            winnerIds = listOf(60),
            winnerLabel = "Triple H",
            notes = "Tourney Round 1"
        ),

        Match(
            id = 8,
            showId = 2,
            showType = Show.SHOW,
            slot = CardSlot.OPEN,
            participants = "Eric vs Ric",
            participantIds = listOf(17, 37),
            stipulation = "Normal",
            winnerId = 37,
            winnerIds = listOf(37),
            winnerLabel = "Ric Flair",
            notes = "Tourney Round 1"
        ),
        Match(
            id = 11,
            showId = 2,
            showType = Show.SHOW,
            slot = CardSlot.MID,
            participants = "DiBiase vs Vince",
            participantIds = listOf(54, 66),
            stipulation = "Normal",
            winnerId = 66,
            winnerIds = listOf(66),
            winnerLabel = "Vince McMahon",
            notes = "Tourney Round 1"
        ),
        Match(
            id = 12,
            showId = 2,
            showType = Show.SHOW,
            slot = CardSlot.MID,
            participants = "Booker vs Sean",
            participantIds = listOf(6, 45),
            stipulation = "Normal",
            winnerId = 6,
            winnerIds = listOf(6),
            winnerLabel = "Booker T",
            notes = "Tourney Round 1"
        ),
        Match(
            id = 13,
            showId = 2,
            showType = Show.SHOW,
            slot = CardSlot.UPPER,
            participants = "Steiner vs RVD",
            participantIds = listOf(44, 42),
            stipulation = "Normal",
            winnerId = 42,
            winnerIds = listOf(42),
            winnerLabel = "RVD",
            notes = "Tourney Round 1"
        ),
        Match(
            id = 14,
            showId = 2,
            showType = Show.SHOW,
            slot = CardSlot.MAIN,
            participants = "Austin vs Goldust",
            participantIds = listOf(51, 20),
            stipulation = "Normal",
            winnerId = 51,
            winnerIds = listOf(51),
            winnerLabel = "Steve Austin",
            notes = "Tourney Round 1"
        ),

        // SD matches
        Match(
            id = 15,
            showId = 3,
            showType = Show.SHOW,
            slot = CardSlot.OPEN,
            participants = "Hurricane vs Big Show",
            participantIds = listOf(56, 5),
            stipulation = "Normal",
            winnerId = 5,
            winnerIds = listOf(5),
            winnerLabel = "Big Show",
            notes = "Tourney Round 1"
        ),
        Match(
            id = 16,
            showId = 3,
            showType = Show.SHOW,
            slot = CardSlot.MID,
            participants = "A-Train vs Rhyno",
            participantIds = listOf(3, 36),
            stipulation = "Normal",
            winnerId = 3,
            winnerIds = listOf(3),
            winnerLabel = "A-Train",
            notes = "Tourney Round 1"
        ),
        Match(
            id = 17,
            showId = 3,
            showType = Show.SHOW,
            slot = CardSlot.MID,
            participants = "Richards vs Edge",
            participantIds = listOf(52, 16),
            stipulation = "Normal",
            winnerId = 16,
            winnerIds = listOf(16),
            winnerLabel = "Edge",
            notes = "Tourney Round 1"
        ),
        Match(
            id = 18,
            showId = 3,
            showType = Show.SHOW,
            slot = CardSlot.UPPER,
            participants = "Tajiri vs Undertaker",
            participantIds = listOf(53, 63),
            stipulation = "Normal",
            winnerId = 63,
            winnerIds = listOf(63),
            winnerLabel = "Undertaker",
            notes = "Tourney Round 1"
        ),
        Match(
            id = 19,
            showId = 3,
            showType = Show.SHOW,
            slot = CardSlot.MAIN,
            participants = "Lesnar vs Rodney",
            participantIds = listOf(7, 41),
            stipulation = "Normal",
            winnerId = 7,
            winnerIds = listOf(7),
            winnerLabel = "Brock Lesnar",
            notes = "Tourney Round 1"
        ),

        Match(
            id = 20,
            showId = 4,
            showType = Show.SHOW,
            slot = CardSlot.OPEN,
            participants = "Piper vs Angle",
            participantIds = listOf(40, 29),
            stipulation = "Normal",
            winnerId = 29,
            winnerIds = listOf(29),
            winnerLabel = "Kurt Angle",
            notes = "Tourney Round 1"
        ),
        Match(
            id = 21,
            showId = 4,
            showType = Show.SHOW,
            slot = CardSlot.MID,
            participants = "Snuka vs Rikishi",
            participantIds = listOf(25, 39),
            stipulation = "Normal",
            winnerId = 25,
            winnerIds = listOf(25),
            winnerLabel = "Jimmy Snuka",
            notes = "Tourney Round 1"
        ),
        Match(
            id = 22,
            showId = 4,
            showType = Show.SHOW,
            slot = CardSlot.MID,
            participants = "Benoit vs Nash",
            participantIds = listOf(11, 28),
            stipulation = "Normal",
            winnerId = 28,
            winnerIds = listOf(28),
            winnerLabel = "Kevin Nash",
            notes = "Tourney Round 1"
        ),
        Match(
            id = 23,
            showId = 4,
            showType = Show.SHOW,
            slot = CardSlot.UPPER,
            participants = "Rock vs Dragon",
            participantIds = listOf(57, 62),
            stipulation = "Normal",
            winnerId = 57,
            winnerIds = listOf(57),
            winnerLabel = "The Rock",
            notes = "Tourney Round 1"
        ),
        Match(
            id = 24,
            showId = 4,
            showType = Show.SHOW,
            slot = CardSlot.MAIN,
            participants = "Cena vs Mysterio",
            participantIds = listOf(26, 35),
            stipulation = "Normal",
            winnerId = 26,
            winnerIds = listOf(26),
            winnerLabel = "John Cena",
            notes = "Tourney Round 1"
        ),

        // PPV matches
        Match(
            id = 25,
            showId = 5,
            showType = Show.PPV,
            slot = CardSlot.OPEN,
            participants = "Steiner vs Test",
            participantIds = listOf(44, 55),
            stipulation = "Normal",
            winnerId = 44,
            winnerIds = listOf(44),
            winnerLabel = "Scott Steiner",
            notes = "Feud Match"
        ),
        Match(
            id = 26,
            showId = 5,
            showType = Show.PPV,
            slot = CardSlot.MID,
            participants = "Rhyno vs Rikishi",
            participantIds = listOf(36, 39),
            stipulation = "Normal",
            winnerId = 39,
            winnerIds = listOf(39),
            winnerLabel = "Rikishi",
            notes = "Feud Match"
        ),
        Match(
            id = 27,
            showId = 5,
            showType = Show.PPV,
            slot = CardSlot.MID,
            participants = "Edge & Christian vs Sheik & Steele",
            stipulation = "Normal Tag",
            participantIds = listOf(16, 13, 23, 18),
            winnerId = 16,
            winnerLabel = "Edge & Christian",
            winnerIds = listOf(16, 13),
            notes = "Feud Match"
        ),
        Match(
            id = 28,
            showId = 5,
            showType = Show.PPV,
            slot = CardSlot.UPPER,
            participants = "Dudley Boyz vs Brothers of Destruction",
            stipulation = "Normal Tag",
            participantIds = listOf(8, 14, 27, 58),
            winnerId = 14,
            winnerLabel = "Dudley Boyz",
            winnerIds = listOf(8, 14),
            notes = "Feud Match"
        ),
        Match(
            id = 29,
            showId = 5,
            showType = Show.PPV,
            slot = CardSlot.UPPER,
            participants = "Austin vs Steiner",
            participantIds = listOf(51, 44),
            stipulation = "Normal",
            winnerId = 51,
            winnerIds = listOf(51),
            winnerLabel = "Steve Austin",
            notes = "Feud Match"
        ),
        Match(
            id = 30,
            showId = 5,
            showType = Show.PPV,
            slot = CardSlot.UPPER,
            participants = "Cena vs Brock",
            participantIds = listOf(26, 7),
            stipulation = "Normal",
            winnerId = 7,
            winnerIds = listOf(7),
            winnerLabel = "Brock Lesnar",
            notes = "WWE Championship match (defender - Cena)"
        ),
        Match(
            id = 31,
            showId = 5,
            showType = Show.PPV,
            slot = CardSlot.MAIN,
            participants = "All",
            participantIds = listOf(),
            stipulation = "Elimination",
            winnerId = 57,
            winnerIds = listOf(57),
            winnerLabel = "The Rock",
            notes = "Won entering #22"
        ),
    )

    fun getShowLabel(showId: Int, showType: Show): String {
        return when (showType) {
            Show.PPV -> ppvEvents.find { it.id == showId }?.name ?: "PPV #${showId}"
            Show.SHOW -> {
                val episode = episodes.find { it.id == showId }
                if (episode != null) {
                    val brandLabel = when (episode.brand) {
                        Brand.RAW -> "RAW"
                        Brand.SD -> "SD"
                        else -> {}
                    }
                    "$brandLabel ${episode.episodeNumber}"
                } else {
                    "Show #$showId"
                }
            }
        }
    }

    fun getMatchesForShow(showId: Int) = matches.filter { it.showId == showId }
    fun getMatchesForEpisode(episodeId: Int) = matches.filter {
        it.showId == episodeId && it.showType.toString() == "SHOW"
    }

    fun getMatchesForPPV(ppvId: Int) = matches.filter {
        it.showId == ppvId && it.showType.toString() == "PPV"
    }
}