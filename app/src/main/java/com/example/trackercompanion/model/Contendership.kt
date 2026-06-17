package com.example.trackercompanion.model

data class Contendership(
    val id: Int,
    val titleId: Int,
    val wrestlerIds: List<Int>,
    val wrestlerNames: List<String>,
    val rank: Int
)
