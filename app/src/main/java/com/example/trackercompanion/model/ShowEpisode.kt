package com.example.trackercompanion.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.trackercompanion.model.enums.Brand
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class ShowEpisode(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val episodeNumber: Int,
    val brand: Brand,
    val weekNumber: Int,
    val notes: String = "",

    @ColumnInfo(defaultValue = "0")
    val isComplete: Boolean = false
) : Parcelable
