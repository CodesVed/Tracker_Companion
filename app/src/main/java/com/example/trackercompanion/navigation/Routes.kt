package com.example.trackercompanion.navigation

import com.example.trackercompanion.model.Wrestler
import kotlinx.serialization.Serializable

sealed class Navigation{
    @Serializable
    object Main
}

sealed class Routes {
    @Serializable
    object Dashboard

    @Serializable
    object Roster

    @Serializable
    data class WrestlerDetail(val wrestlerId: Int)

    @Serializable
    data class AddEditWrestler(val wrestlerId: Int = -1)

    @Serializable
    object Shows

    @Serializable
    data class EpisodeDetail(val episodeId: Int, val isPPV: Boolean)

    @Serializable
    object AddEpisode

    @Serializable
    object Championships

    @Serializable
    data class TitleDetail(val titleId: Int)

    @Serializable
    object Calendar
}