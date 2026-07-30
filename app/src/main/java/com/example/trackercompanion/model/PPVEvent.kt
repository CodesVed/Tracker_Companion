package com.example.trackercompanion.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class PPVEvent(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val ppvNumber: Int,
    val name: String,
    val notes: String = ""
) : Parcelable
