package com.example.trackercompanion.navigation

import kotlinx.serialization.Serializable

sealed class Navigation{
    @Serializable
    object Main: Navigation()
}

sealed class Routes {
    @Serializable
    object Home: Routes()
}