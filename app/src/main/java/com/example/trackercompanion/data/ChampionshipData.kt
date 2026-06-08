package com.example.trackercompanion.data

import com.example.trackercompanion.model.Championship
import com.example.trackercompanion.model.Contendership

object ChampionshipData {
    val titles: List<Championship> = listOf(
        Championship(
            id = 1,
            title = "WWE Championship",
            championId = 7,
            championName = "Brock Lesnar",
            reignNumber = 2,
            reignStartEpisode = "Royal Rumble",
            reignEndEpisode = null,
            defenses = 0,
            notes = "Won back from John Cena after Cena's tourney victory"
        ),
        Championship(
            id = 2,
            title = "World Heavyweight Championship",
            championId = 51,
            championName = "Steve Austin",
            reignNumber = 1,
            reignStartEpisode = "Royal Rumble",
            reignEndEpisode = null,
            defenses = 0,
            notes = "Won HIAC vs Shawn Michaels — tourney final"
        ),
        Championship(
            id = 3,
            title = "WWE Tag Championship",
            championId = null,
            championName = "Edge & Christian",
            reignNumber = 1,
            reignStartEpisode = "Royal Rumble",
            reignEndEpisode = null,
            defenses = 0,
            partnerName = "Christian",
            notes = "Won against Dudleys in Ladder match"
        ),
        Championship(
            id = 4,
            title = "World Tag Championship",
            championId = null,
            championName = "The Dudley Boys",
            reignNumber = 1,
            reignStartEpisode = "Royal Rumble",
            reignEndEpisode = null,
            defenses = 0,
            partnerName = "Bubba Ray Dudley",
            notes = "Won against BOD in a Normal Tag match"
        ),
        Championship(
            id = 5,
            title = "WWE US Championship",
            championId = null,
            championName = "N/A",
            reignNumber = 0,
            reignStartEpisode = "N/A",
            defenses = 0,
            notes = "In progress"
        ),
        Championship(
            id = 6,
            title = "WWE IC Championship",
            championId = null,
            championName = "N/A",
            reignNumber = 0,
            reignStartEpisode = "N/A",
            defenses = 0,
            notes = "In progress"
        ),
        Championship(
            id = 7,
            title = "WWE Women's Championship",
            championId = null,
            championName = null,
            reignNumber = 0,
            reignStartEpisode = "N/A",
            defenses = 0,
            notes = "Tournament in progress"
        ),
    )

    val contenderships: List<Contendership> = listOf(
        // WWE Championship contenders
        Contendership(id = 1,  titleId = 1, wrestlerId = 57, wrestlerName = "The Rock", rank = 1),
        Contendership(id = 2,  titleId = 1, wrestlerId = 26, wrestlerName = "John Cena", rank = 2),
        Contendership(id = 3,  titleId = 1, wrestlerId = 16, wrestlerName = "Edge", rank = 3),
        Contendership(id = 4,  titleId = 1, wrestlerId = 5, wrestlerName = "Big Show", rank = 4),
        Contendership(id = 5,  titleId = 1, wrestlerId = 29, wrestlerName = "Kurt Angle", rank = 5),
        Contendership(id = 6,  titleId = 1, wrestlerId = 13, wrestlerName = "Christian", rank = 6),
        Contendership(id = 7,  titleId = 1, wrestlerId = 36, wrestlerName = "Rhyno", rank = 7),

        // World Heavyweight Championship contenders
        Contendership(id = 5,  titleId = 2, wrestlerId = 60, wrestlerName = "Triple H", rank = 1),
        Contendership(id = 6,  titleId = 2, wrestlerId = 47, wrestlerName = "Shawn Michaels", rank = 2),
        Contendership(id = 7,  titleId = 2, wrestlerId = 27, wrestlerName = "Kane", rank = 3),
        Contendership(id = 8,  titleId = 2, wrestlerId = 58, wrestlerName = "The Undertaker", rank = 4),
        Contendership(id = 8,  titleId = 2, wrestlerId = 44, wrestlerName = "Scott Steiner", rank = 5),
        Contendership(id = 8,  titleId = 2, wrestlerId = 6, wrestlerName = "Booker T", rank = 6),
        Contendership(id = 8,  titleId = 2, wrestlerId = 32, wrestlerName = "Matt Hardy", rank = 7),
    )

    fun getContendersForTitle(titleId: Int) =
        contenderships.filter { it.titleId == titleId }.sortedBy { it.rank }
}