package com.example.trackercompanion.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.trackercompanion.model.Match
import com.example.trackercompanion.model.enums.Show
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(match: Match)

    @Insert
    suspend fun insertAll(matches: List<Match>)

    @Update
    suspend fun update(match: Match)

    @Query("DELETE FROM `Match` WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM `Match` WHERE showId = :showId AND showType = :showType")
    suspend fun deleteMatchesForEpisode(showId: Int, showType: Show)

    @Query("SELECT * FROM `Match`")
    fun getAllMatches(): Flow<List<Match>>
}