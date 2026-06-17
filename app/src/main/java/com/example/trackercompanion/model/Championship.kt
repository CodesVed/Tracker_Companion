package com.example.trackercompanion.model

import com.example.trackercompanion.model.enums.Brand

data class Championship(
    val id: Int,
    val title: String,
    val titleImage: Int,
    val brand: Brand? = null       // null = open for all
)
