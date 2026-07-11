package com.example.trackercompanion.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.trackercompanion.model.Championship
import kotlinx.coroutines.flow.Flow

@Dao
interface ChampionshipDao {

    @Insert
    suspend fun insertAll(championships: List<Championship>)

    @Query("SELECT * FROM Championship")
    fun getAllChampionships(): Flow<List<Championship>>
}