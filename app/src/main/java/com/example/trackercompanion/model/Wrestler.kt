package com.example.trackercompanion.model

import androidx.annotation.DrawableRes
import com.example.trackercompanion.model.enums.Brand
import com.example.trackercompanion.model.enums.Status
import com.example.trackercompanion.model.enums.Type

data class Wrestler(
    val id: Int,
    val name: String,
    val brand: Brand,
    val status: Status,
    val type: Type,
    val imageRes: Int,
    val notes: String = "",
)