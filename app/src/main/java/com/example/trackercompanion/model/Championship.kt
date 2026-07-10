package com.example.trackercompanion.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.trackercompanion.model.enums.Brand

@Entity
data class Championship(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val title: String,
    val titleImage: Int,
    val brand: Brand? = null       // null = open for all
)
