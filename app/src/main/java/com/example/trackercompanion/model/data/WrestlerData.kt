package com.example.trackercompanion.model.data

import com.example.trackercompanion.model.Wrestler
import com.example.trackercompanion.model.enums.Brand
import com.example.trackercompanion.model.enums.Status
import com.example.trackercompanion.model.enums.Type

object WrestlerData {
    val roster: List<Wrestler> = listOf(

        // ── RAW ────────────────────────────────────
        Wrestler(
            id = 1,
            name = "Steve Austin",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            wins = 8,
            loss = 2,
            teamWins = 0,
            imageRes = "img_steve_austin",
            notes = "World Heavyweight Champion"
        ),
        Wrestler(
            id = 2,
            name = "Triple H",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.BOTH,
            wins = 6,
            loss = 3,
            teamWins = 0,
            imageRes = "img_triple_h",
            notes = "#1 Contender — WHC"
        ),
        Wrestler(
            id = 3,
            name = "Shawn Michaels",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            wins = 5,
            loss = 4,
            teamWins = 0,
            imageRes = "img_shawn_michaels",
        ),
        Wrestler(
            id = 4,
            name = "Kane",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            wins = 5,
            loss = 4,
            teamWins = 0,
            imageRes = "img_kane",
        ),
        Wrestler(
            id = 6,
            name = "Booker T",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            wins = 5,
            loss = 4,
            teamWins = 0,
            imageRes = "img_booker_t",
        ),

        // ── Smackdown ────────────────────────────────────
        Wrestler(
            id = 26,
            name = "Brock Lesnar",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            wins = 10,
            loss = 1,
            teamWins = 0,
            imageRes = "img_brock_lesnar",
            notes = "WWE Champion - Reign #2"
        ),
        Wrestler(
            id = 27,
            name = "The Rock",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            wins = 9,
            loss = 2,
            teamWins = 0,
            imageRes = "img_the_rock",
            notes = "Royal Rumble winner — #1 Contender WWE Championship"
        ),
        Wrestler(
            id = 28,
            name = "John Cena",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            wins = 8,
            loss = 2,
            teamWins = 0,
            imageRes = "img_john_cena",
            notes = "Broke Lesnar's first undefeated streak. Won WWE title in tourney final."
        ),
        Wrestler(
            id = 29,
            name = "Edge",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            wins = 8,
            loss = 2,
            teamWins = 0,
            imageRes = "img_john_cena",
            notes = "Broke Lesnar's first undefeated streak. Won WWE title in tourney final."
        ),
        Wrestler(
            id = 30,
            name = "Big Show",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            wins = 8,
            loss = 2,
            teamWins = 0,
            imageRes = "img_john_cena",
            notes = "Broke Lesnar's first undefeated streak. Won WWE title in tourney final."
        ),

        // ── Free (Divas) ────────────────────────────────────
        Wrestler(
            id = 58,
            name = "Trish Stratus",
            brand = Brand.FREE,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            wins = 0,
            loss = 0,
            teamWins = 0,
            imageRes = "img_trish_stratus"
        ),
        Wrestler(
            id = 59,
            name = "Lita",
            brand = Brand.FREE,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            wins = 0,
            loss = 0,
            teamWins = 0,
            imageRes = "img_lita"
        ),
    )

    fun getById(id: Int) = roster.find { it.id == id }
    fun getByBrand(brand: String) = roster.filter { it.brand.toString() == brand }
}