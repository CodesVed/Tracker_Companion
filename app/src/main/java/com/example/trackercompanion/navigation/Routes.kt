package com.example.trackercompanion.navigation

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
    object WrestlerDetail

    @Serializable
    object Shows

    @Serializable
    object Championships

    @Serializable
    object Calendar: Routes()
}