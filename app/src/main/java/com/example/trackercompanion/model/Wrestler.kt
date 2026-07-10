package com.example.trackercompanion.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.trackercompanion.model.enums.Brand
import com.example.trackercompanion.model.enums.Status
import com.example.trackercompanion.model.enums.Type

@Entity
data class Wrestler(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val brand: Brand,
    val status: Status,
    val type: Type,
    val imageRes: Int,
    val notes: String = "",
)