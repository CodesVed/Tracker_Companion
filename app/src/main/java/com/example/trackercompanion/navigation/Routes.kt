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
    object AddEditWrestler

    @Serializable
    object Shows

    @Serializable
    object Championships

    @Serializable
    object Calendar
}