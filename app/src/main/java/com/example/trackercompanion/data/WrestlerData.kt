package com.example.trackercompanion.data

import com.example.trackercompanion.model.Wrestler
import com.example.trackercompanion.model.enums.Brand
import com.example.trackercompanion.model.enums.Status
import com.example.trackercompanion.model.enums.Type
import com.example.trackercompanion.R

object WrestlerData {
    val roster: List<Wrestler> = listOf(

        // ── RAW ────────────────────────────────────
        Wrestler(
            id = 4,
            name = "Batista",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.BOTH,
            imageUrl = "file:////android_asset/images/wrestler/img_batista.webp",

        ),
        Wrestler(
            id = 6,
            name = "Booker T",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_booker_t.webp"
        ),
        Wrestler(
            id = 8,
            name = "Bubba Ray Dudley",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.TEAM,
            imageUrl = "file:////android_asset/images/wrestler/img_bubba_ray_dudley.webp"
        ),
        Wrestler(
            id = 12,
            name = "Chris Jericho",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_chris_jericho.webp"
        ),
        Wrestler(
            id = 14,
            name = "D-Von Dudley",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.TEAM,
            imageUrl = "file:////android_asset/images/wrestler/img_dvon_dudley.webp"
        ),
        Wrestler(
            id = 17,
            name = "Eric Bischoff",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_eric_bischoff.webp"
        ),
        Wrestler(
            id = 19,
            name = "Goldberg",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_goldberg.webp"
        ),
        Wrestler(
            id = 20,
            name = "Goldust",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_goldust.webp"
        ),
        Wrestler(
            id = 22,
            name = "Hillbilly Jim",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.TEAM,
            imageUrl = "file:////android_asset/images/wrestler/img_hillbilly_jim.webp"
        ),
        Wrestler(
            id = 27,
            name = "Kane",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.BOTH,
            imageUrl = "file:////android_asset/images/wrestler/img_kane.webp"
        ),
        Wrestler(
            id = 30,
            name = "Lance Storm",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_lance_storm.webp"
        ),
        Wrestler(
            id = 32,
            name = "Matt Hardy",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_matt_hardy.webp"
        ),
        Wrestler(
            id = 33,
            name = "Nikolai Volkoff",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.TEAM,
            imageUrl = "file:////android_asset/images/wrestler/img_nikolai_volkoff.webp"
        ),
        Wrestler(
            id = 34,
            name = "Randy Orton",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.BOTH,
            imageUrl = "file:////android_asset/images/wrestler/img_randy_orton.webp"
        ),
        Wrestler(
            id = 37,
            name = "Ric Flair",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.BOTH,
            imageUrl = "file:////android_asset/images/wrestler/img_ric_flair.webp"
        ),
        Wrestler(
            id = 38,
            name = "Rico",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_rico.webp"
        ),
        Wrestler(
            id = 42,
            name = "Rob Van Dam (RVD)",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_rob_van_dam.webp"
        ),
        Wrestler(
            id = 44,
            name = "Scott Steiner",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_scott_steiner.webp"
        ),
        Wrestler(
            id = 45,
            name = "Sean O'Haire",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_sean_ohaire.webp"
        ),
        Wrestler(
            id = 47,
            name = "Shawn Michaels (HBK)",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_shawn_michaels.webp"
        ),
        Wrestler(
            id = 51,
            name = "Steve Austin",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_steve_austin.webp"
        ),
        Wrestler(
            id = 54,
            name = "Ted DiBiase",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_ted_dibiase.webp"
        ),
        Wrestler(
            id = 55,
            name = "Test",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_test.webp"
        ),
        Wrestler(
            id = 58,
            name = "The Undertaker",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.BOTH,
            imageUrl = "file:////android_asset/images/wrestler/img_the_undertaker.webp"
        ),
        Wrestler(
            id = 60,
            name = "Triple H",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.BOTH,
            imageUrl = "file:////android_asset/images/wrestler/img_triple_h.webp",
            notes = "#1 Contender - WHC"
        ),
        Wrestler(
            id = 64,
            name = "Val Venis",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_val_venis.webp"
        ),
        Wrestler(
            id = 66,
            name = "Vince McMahon",
            brand = Brand.RAW,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_vince_mcmahon.webp"
        ),

        // ── Smackdown ────────────────────────────────────
        Wrestler(
            id = 2,
            name = "Animal",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.TEAM,
            imageUrl = "file:////android_asset/images/wrestler/img_animal.webp",
        ),
        Wrestler(
            id = 3,
            name = "A-Train",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_atrain.webp",
        ),
        Wrestler(
            id = 5,
            name = "Big Show",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_big_show.webp"
        ),
        Wrestler(
            id = 7,
            name = "Brock Lesnar",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_brock_lesnar.webp",
            notes = "WWE Champion - Reign #2"
        ),
        Wrestler(
            id = 9,
            name = "Charlie Haas",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.TEAM,
            imageUrl = "file:////android_asset/images/wrestler/img_charlie_haas.webp",
        ),
        Wrestler(
            id = 10,
            name = "Chavo Guerrero",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.TEAM,
            imageUrl = "file:////android_asset/images/wrestler/img_chavo_guerrero.webp",
        ),
        Wrestler(
            id = 11,
            name = "Chris Benoit",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_chris_benoit.webp",
        ),
        Wrestler(
            id = 13,
            name = "Christian",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.BOTH,
            imageUrl = "file:////android_asset/images/wrestler/img_christian.webp",
        ),
        Wrestler(
            id = 15,
            name = "Eddie Guerrero",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.TEAM,
            imageUrl = "file:////android_asset/images/wrestler/img_eddie_guerrero.webp",
        ),
        Wrestler(
            id = 16,
            name = "Edge",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.BOTH,
            imageUrl = "file:////android_asset/images/wrestler/img_edge.webp",
        ),
        Wrestler(
            id = 18,
            name = "George Steele",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.TEAM,
            imageUrl = "file:////android_asset/images/wrestler/img_george_steele.webp",
        ),
        Wrestler(
            id = 21,
            name = "Hawk",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.TEAM,
            imageUrl = "file:////android_asset/images/wrestler/img_hawk.webp",
        ),
        Wrestler(
            id = 23,
            name = "Iron Sheik",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.TEAM,
            imageUrl = "file:////android_asset/images/wrestler/img_iron_sheik.webp",
        ),
        Wrestler(
            id = 25,
            name = "Jimmy Snuka",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_jimmy_snuka.webp",
        ),
        Wrestler(
            id = 26,
            name = "John Cena",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_john_cena.webp",
            notes = "Broke Lesnar's undefeated streak, winning WWE Championship tourney"
        ),
        Wrestler(
            id = 28,
            name = "Kevin Nash",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_kevin_nash.webp",
        ),
        Wrestler(
            id = 29,
            name = "Kurt Angle",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_kurt_angle.webp",
        ),
        Wrestler(
            id = 35,
            name = "Rey Mysterio",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_rey_mysterio.webp",
        ),Wrestler(
            id = 36,
            name = "Rhyno",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_rhyno.webp",
        ),
        Wrestler(
            id = 39,
            name = "Rikishi",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_rikishi.webp",
        ),
        Wrestler(
            id = 40,
            name = "Roddy Piper",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_roddy_piper.webp",
        ),
        Wrestler(
            id = 41,
            name = "Rodney Mack",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_rodney_mack.webp",
        ),
        Wrestler(
            id = 46,
            name = "Sgt. Slaughter",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_sgt_slaughter.webp",
        ),
        Wrestler(
            id = 48,
            name = "Shelton Benjamin",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.TEAM,
            imageUrl = "file:////android_asset/images/wrestler/img_shelton_benjamin.webp",
        ),
        Wrestler(
            id = 52,
            name = "Steven Richards",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_steven_richards.webp",
        ),
        Wrestler(
            id = 53,
            name = "Tajiri",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_tajiri.webp",
        ),
        Wrestler(
            id = 56,
            name = "The Hurricane",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_the_hurricane.webp",
        ),
        Wrestler(
            id = 57,
            name = "The Rock",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_the_rock.webp",
            notes = "WWE Championship - #1 Contender"
        ),
        Wrestler(
            id = 62,
            name = "Ultimo Dragon",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_ultimo_dragon.webp",
        ),
        Wrestler(
            id = 63,
            name = "Undertaker",
            brand = Brand.SD,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_undertaker.webp",
        ),

        // ── Free (Divas) ────────────────────────────────────
        Wrestler(
            id = 24,
            name = "Jazz",
            brand = Brand.DIVA,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_jazz.webp",
        ),
        Wrestler(
            id = 31,
            name = "Lita",
            brand = Brand.DIVA,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_lita.webp",
        ),
        Wrestler(
            id = 43,
            name = "Sable",
            brand = Brand.DIVA,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_sable.webp",
        ),
        Wrestler(
            id = 49,
            name = "Stacy Keibler",
            brand = Brand.DIVA,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_stacy_keibler.webp",
        ),
        Wrestler(
            id = 50,
            name = "Stephanie McMahon",
            brand = Brand.DIVA,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_stephanie_mcmahon.webp",
        ),
        Wrestler(
            id = 59,
            name = "Torrie Wilson",
            brand = Brand.DIVA,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_torrie_wilson.webp",
        ),
        Wrestler(
            id = 61,
            name = "Trish Stratus",
            brand = Brand.DIVA,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_trish_stratus.webp",
        ),
        Wrestler(
            id = 65,
            name = "Victoria",
            brand = Brand.DIVA,
            status = Status.ACTIVE,
            type = Type.SINGLE,
            imageUrl = "file:////android_asset/images/wrestler/img_victoria.webp",

        ),
    )

    fun getById(id: Int) = roster.find { it.id == id }
    fun getByBrand(brand: String) = roster.filter { it.brand.toString() == brand }
}