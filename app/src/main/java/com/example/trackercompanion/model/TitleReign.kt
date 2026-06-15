package com.example.trackercompanion.model

data class TitleReign(
    val id: Int,
    val titleId: Int,
    val titleName: String,
    val reignNumber: Int,
    val holderIds: List<Int>,
    val holderNames: List<String>,
    val holderImages: List<Int> = listOf(),
    val wonAtEvent: String,
    val lostAtEvent: String?,
    val defenses: Int = 0,
    val notes: String = ""
)
