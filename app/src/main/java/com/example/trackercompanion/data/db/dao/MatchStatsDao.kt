package com.example.trackercompanion.data.db.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.trackercompanion.model.MatchStats
import kotlinx.coroutines.flow.Flow

interface MatchStatsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(matchStats: MatchStats)

    @Update
    suspend fun update(matchStats: MatchStats)

    @Query("SELECT * FROM MatchStats")
    fun getAllMatchStats(): Flow<List<MatchStats>>
}