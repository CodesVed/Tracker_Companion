package com.example.trackercompanion.data

import com.example.trackercompanion.R
import com.example.trackercompanion.model.Championship
import com.example.trackercompanion.model.Contendership
import com.example.trackercompanion.model.Match
import com.example.trackercompanion.model.MatchStats
import com.example.trackercompanion.model.TitleReign
import com.example.trackercompanion.model.Wrestler
import com.example.trackercompanion.model.computeStatsForWrestler
import com.example.trackercompanion.model.enums.Brand
import com.example.trackercompanion.model.enums.Status

object ChampionshipData {
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
            wonAtEvent = "Royal Rumble 1",
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
        Contendership(id = 1, titleId = 1, wrestlerIds = listOf(57), wrestlerNames = listOf("The Rock"), rank = 1),
        Contendership(id = 2, titleId = 1, wrestlerIds = listOf(26), wrestlerNames = listOf("John Cena"), rank = 2),
        Contendership(id = 3, titleId = 1, wrestlerIds = listOf(16), wrestlerNames = listOf("Edge"), rank = 3),
        Contendership(id = 4, titleId = 1, wrestlerIds = listOf(5), wrestlerNames = listOf("Big Show"), rank = 4),
        Contendership(id = 5, titleId = 1, wrestlerIds = listOf(29), wrestlerNames = listOf("Kurt Angle"), rank = 5),
        Contendership(id = 6, titleId = 1, wrestlerIds = listOf(13), wrestlerNames = listOf("Christian"), rank = 6),
        Contendership(id = 7, titleId = 1, wrestlerIds = listOf(36), wrestlerNames = listOf("Rhyno"), rank = 7),

        // World Heavyweight Championship contenders
        Contendership(id = 8, titleId = 2, wrestlerIds = listOf(60), wrestlerNames = listOf("Triple H"), rank = 1),
        Contendership(id = 9, titleId = 2, wrestlerIds = listOf(47), wrestlerNames = listOf("Shawn Michaels"), rank = 2),
        Contendership(id = 10, titleId = 2, wrestlerIds = listOf(27), wrestlerNames = listOf("Kane"), rank = 3),
        Contendership(id = 11, titleId = 2, wrestlerIds = listOf(58), wrestlerNames = listOf("The Undertaker"), rank = 4),
        Contendership(id = 12, titleId = 2, wrestlerIds = listOf(44), wrestlerNames = listOf("Scott Steiner"), rank = 5),
        Contendership(id = 13, titleId = 2, wrestlerIds = listOf(6), wrestlerNames = listOf("Booker T"), rank = 6),
        Contendership(id = 14, titleId = 2, wrestlerIds = listOf(32), wrestlerNames = listOf("Matt Hardy"), rank = 7),
    )

    val titles: List<Championship> = listOf(
        Championship(id = 1, title = "WWE Championship", titleImage = R.drawable.belt_wwe_championship, brand = Brand.SD),
        Championship(id = 2, title = "World Heavyweight Championship", titleImage = R.drawable.belt_world_heavyweight_championship, brand = Brand.RAW),
        Championship(id = 3, title = "WWE Tag Team Championship", titleImage = R.drawable.belt_tag_team_championship, brand = Brand.SD),
        Championship(id = 4, title = "World Tag Team Championship", titleImage = R.drawable.belt_world_tag_team_championship, brand = Brand.RAW),
        Championship(id = 5, title = "WWE Intercontinental Championship", titleImage = R.drawable.belt_intercontinental_championship),
        Championship(id = 6, title = "WWE United States Championship", titleImage = R.drawable.belt_us_championship),
        Championship(id = 7, title = "WWE Women's Championship", titleImage = R.drawable.belt_womens_champion)
    )

    fun getReignsForWrestler(wrestlerId: Int): List<TitleReign> =
        reigns.filter { wrestlerId in it.holderIds }

    fun getReignsForTitle(titleId: Int): List<TitleReign> =
        reigns.filter { it.titleId == titleId }.sortedBy { it.reignNumber }

    fun getCurrentReign(titleId: Int, reigns: List<TitleReign>): TitleReign? =
        reigns.find { it.titleId == titleId && it.lostAtEvent == null }

    fun getTitleById(id: Int): Championship? =
        titles.find { it.id == id }

    fun getContendersForTitle(titleId: Int): List<Contendership> =
        contenderships.filter { it.titleId == titleId }.sortedBy { it.rank }

    fun suggestNextContender(
        titleId: Int,
        titleBrand: Brand?,
        isTagTitle: Boolean,
        wrestlers: List<Wrestler>,
        matches: List<Match>,
        existingContenderIds: Set<Int>,
        currentChampionIds: Set<Int>
    ): List<Wrestler> {
        val eligible = wrestlers
            .filter { w ->
                (titleBrand == null || w.brand == titleBrand) &&
                        w.id !in existingContenderIds &&
                        w.id !in currentChampionIds &&
                        w.status == Status.ACTIVE
            }
            .map { w -> w to computeStatsForWrestler(w.id, matches) }
            .sortedWith(
                compareByDescending<Pair<Wrestler, MatchStats>> { it.second.points }
                    .thenByDescending { it.second.winPercent }
            )
            .map { it.first }

        return if (isTagTitle) {
            eligible.take(2)
        } else {
            eligible.take(1)
        }
    }
}