package com.example.trackercompanion.data.db.dao

import androidx.room.Query
import com.example.trackercompanion.model.Championship
import kotlinx.coroutines.flow.Flow

interface ChampionshipDao {

    @Query("SELECT * FROM Championship")
    fun getAllChampionships(): Flow<List<Championship>>
}