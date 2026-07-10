package com.example.trackercompanion.data.db.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.trackercompanion.model.Wrestler
import kotlinx.coroutines.flow.Flow

interface WrestlerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(wrestler: Wrestler)

    @Update
    suspend fun update(wrestler: Wrestler)

    @Query("DELETE FROM Wrestler WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM Wrestler")
    fun getAllWrestlers(): Flow<List<Wrestler>>
}