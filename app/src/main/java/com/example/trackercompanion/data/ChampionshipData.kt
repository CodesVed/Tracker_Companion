package com.example.trackercompanion.data

import com.example.trackercompanion.model.Championship
import com.example.trackercompanion.model.Contendership
import com.example.trackercompanion.model.TitleReign

object ChampionshipData {
    val titles: List<Championship> = listOf(
        Championship(id = 1, title = "WWE Championship", currentChampionId = 7),
        Championship(id = 2, title = "World Heavyweight Championship", currentChampionId = 51),
        Championship(id = 3, title = "WWE Tag Team Championship", currentChampionId = 8),
        Championship(id = 4, title = "World Tag Team Championship", currentChampionId = 16),
        Championship(id = 5, title = "WWE Intercontinental Championship", currentChampionId = null),
        Championship(id = 6, title = "WWE United States Championship", currentChampionId = null),
        Championship(id = 7, title = "WWE Women's Championship", currentChampionId = null)
    )

    val reigns: List<TitleReign> = listOf(
        // WWE Championship
        TitleReign(
            id = 1,
            titleId = 1,
            titleName = "WWE Championship",
            reignNumber = 1,
            holderIds = listOf(26),
            holderNames = listOf("John Cena"),
            wonAtEvent = "SD 5",
            lostAtEvent = "Royal Rumble",
            defenses = 0,
            notes = "Won tournament against Lesnar — breaking his undefeated streak.\nBiggest match in history"
        ),
        TitleReign(
            id = 2,
            titleId = 1,
            titleName = "WWE Championship",
            reignNumber = 2,
            holderIds = listOf(7),
            holderNames = listOf("Brock Lesnar"),
            wonAtEvent = "Royal Rumble",
            lostAtEvent = null,
            defenses = 0,
            notes = "Won from Cena after losing from him at tournament"
        ),

        // World Heavyweight Championship
        TitleReign(
            id = 3,
            titleId = 2,
            titleName = "World Heavyweight Championship",
            reignNumber = 1,
            holderIds = listOf(51),
            holderNames = listOf("Steve Austin"),
            wonAtEvent = "RAW 5",
            lostAtEvent = null,
            defenses = 0,
            notes = "Won HIAC vs HBK — tourney final"
        ),

        // WWE Tag Team Championship
        TitleReign(
            id = 4,
            titleId = 3,
            titleName = "WWE Tag Team Championship",
            reignNumber = 1,
            holderIds = listOf(16, 13),
            holderNames = listOf("Edge", "Christian"),
            wonAtEvent = "SD 5",
            lostAtEvent = null,
            defenses = 0,
            notes = "Won ladder match final vs Sheik & Steele"
        ),

        // World Tag Team Championship
        TitleReign(
            id = 10,
            titleId = 4,
            titleName = "World Tag Team Championship",
            reignNumber = 1,
            holderIds = listOf(8, 14),
            holderNames = listOf("Bubba Ray Dudley", "D-Von Dudley"),
            wonAtEvent = "RAW 5",
            lostAtEvent = null,
            defenses = 0,
            notes = "Won ladder match final vs BOD"
        ),
    )

    val contenderships: List<Contendership> = listOf(
        // WWE Championship contenders
        Contendership(id = 1, titleId = 1, wrestlerId = 57, wrestlerName = "The Rock", rank = 1),
        Contendership(id = 2, titleId = 1, wrestlerId = 26, wrestlerName = "John Cena", rank = 2),
        Contendership(id = 3, titleId = 1, wrestlerId = 16, wrestlerName = "Edge", rank = 3),
        Contendership(id = 4, titleId = 1, wrestlerId = 5, wrestlerName = "Big Show", rank = 4),
        Contendership(id = 5, titleId = 1, wrestlerId = 29, wrestlerName = "Kurt Angle", rank = 5),
        Contendership(id = 6, titleId = 1, wrestlerId = 13, wrestlerName = "Christian", rank = 6),
        Contendership(id = 7, titleId = 1, wrestlerId = 36, wrestlerName = "Rhyno", rank = 7),

        // World Heavyweight Championship contenders
        Contendership(id = 5, titleId = 2, wrestlerId = 60, wrestlerName = "Triple H", rank = 1),
        Contendership(id = 6, titleId = 2, wrestlerId = 47, wrestlerName = "Shawn Michaels", rank = 2),
        Contendership(id = 7, titleId = 2, wrestlerId = 27, wrestlerName = "Kane", rank = 3),
        Contendership(id = 8, titleId = 2, wrestlerId = 58, wrestlerName = "The Undertaker", rank = 4),
        Contendership(id = 8, titleId = 2, wrestlerId = 44, wrestlerName = "Scott Steiner", rank = 5),
        Contendership(id = 8, titleId = 2, wrestlerId = 6, wrestlerName = "Booker T", rank = 6),
        Contendership(id = 8, titleId = 2, wrestlerId = 32, wrestlerName = "Matt Hardy", rank = 7),
    )

    fun getReignsForWrestler(wrestlerId: Int): List<TitleReign> =
        reigns.filter { wrestlerId in it.holderIds }

    fun getReignsForTitle(titleId: Int): List<TitleReign> =
        reigns.filter { it.titleId == titleId }.sortedBy { it.reignNumber }

    fun getCurrentReign(titleId: Int): TitleReign? =
        reigns.find { it.titleId == titleId && it.lostAtEvent == null}

    fun getTitleById(id: Int): Championship? =
        titles.find { it.id == id }

    fun getContendersForTitle(titleId: Int) =
        contenderships.filter { it.titleId == titleId }.sortedBy { it.rank }
}