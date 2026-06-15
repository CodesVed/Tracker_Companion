package com.example.trackercompanion.model

data class Championship(
    val id: Int,
    val title: String,
    val titleImage: Int,
    val currentChampion: TitleReign?
)
