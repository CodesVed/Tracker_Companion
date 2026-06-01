package com.example.trackercompanion.model.data

import com.example.trackercompanion.model.Championship
import com.example.trackercompanion.model.Contendership

object ChampionshipData {
    val titles: List<Championship> = listOf(
        Championship(
            id = 1,
            title = "WWE Championship",
            championId = 26,
            championName = "Brock Lesnar",
            reignNumber = 2,
            reignStartEpisode = "Royal Rumble",
            defenses = 0,
            notes = "Won back from John Cena after Cena's tourney victory"
        ),
        Championship(
            id = 2,
            title = "World Heavyweight Championship",
            championId = 1,
            championName = "Steve Austin",
            reignNumber = 1,
            reignStartEpisode = "Royal Rumble",
            defenses = 0,
            notes = "Won HIAC vs Shawn Michaels — tourney final"
        ),
        Championship(
            id = 7,
            title = "WWE Women's Championship",
            championId = null,
            championName = null,
            reignNumber = 0,
            reignStartEpisode = "",
            defenses = 0,
            notes = "Tournament in progress"
        ),
    )

    val contenderships: List<Contendership> = listOf(
        // WWE Championship contenders
        Contendership(id = 1,  titleId = 1, wrestlerId = 27, wrestlerName = "The Rock", rank = 1),
        Contendership(id = 2,  titleId = 1, wrestlerId = 28, wrestlerName = "John Cena", rank = 2),
        Contendership(id = 3,  titleId = 1, wrestlerId = 29, wrestlerName = "Edge", rank = 3),
        Contendership(id = 4,  titleId = 1, wrestlerId = 30, wrestlerName = "Big Show", rank = 4),

        // World Heavyweight Championship contenders
        Contendership(id = 5,  titleId = 2, wrestlerId = 2, wrestlerName = "Triple H", rank = 1),
        Contendership(id = 6,  titleId = 2, wrestlerId = 3, wrestlerName = "Shawn Michaels", rank = 2),
        Contendership(id = 7,  titleId = 2, wrestlerId = 4, wrestlerName = "Kane", rank = 3),
        Contendership(id = 8,  titleId = 2, wrestlerId = 6, wrestlerName = "Booker T", rank = 4),
    )

    fun getContendersForTitle(titleId: Int) =
        contenderships.filter { it.titleId == titleId }.sortedBy { it.rank }
}