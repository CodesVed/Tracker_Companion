package com.example.trackercompanion.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.trackercompanion.model.ShowEpisode
import com.example.trackercompanion.model.enums.Brand
import kotlinx.coroutines.flow.Flow

@Dao
interface ShowEpisodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(showEpisode: ShowEpisode)

    @Insert
    suspend fun insertAll(showEpisodes: List<ShowEpisode>)

    @Update
    suspend fun update(showEpisode: ShowEpisode)

    @Query("DELETE FROM ShowEpisode WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM ShowEpisode")
    fun getAllShowEpisodes(): Flow<List<ShowEpisode>>

    @Query("SELECT * FROM ShowEpisode WHERE brand = :brand")
    fun getEpisodesByBrand(brand: String): Flow<List<ShowEpisode>>

    @Query("UPDATE ShowEpisode SET isComplete = :isComplete WHERE id = :id")
    suspend fun setComplete(id: Int, isComplete: Boolean)
}