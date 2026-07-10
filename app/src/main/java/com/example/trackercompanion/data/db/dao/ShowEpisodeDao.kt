package com.example.trackercompanion.data.db.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.trackercompanion.model.ShowEpisode
import kotlinx.coroutines.flow.Flow

interface ShowEpisodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(showEpisode: ShowEpisode)

    @Update
    suspend fun update(showEpisode: ShowEpisode)

    @Query("DELETE FROM ShowEpisode WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM ShowEpisode")
    fun getAllShowEpisodes(): Flow<List<ShowEpisode>>
}