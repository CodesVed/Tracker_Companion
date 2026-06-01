package com.example.trackercompanion.model

data class PPVEvent(
    val id: Int,
    val ppvNumber: Int,
    val name: String,
    val notes: String = ""
)
