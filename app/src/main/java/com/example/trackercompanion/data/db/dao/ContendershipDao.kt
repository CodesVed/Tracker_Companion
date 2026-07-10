package com.example.trackercompanion.data.db.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.trackercompanion.model.Contendership
import kotlinx.coroutines.flow.Flow

interface ContendershipDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(contendership: Contendership)

    @Update
    suspend fun update(contendership: Contendership)

    @Query("DELETE FROM Contendership WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM Contendership")
    fun getAllContenderehips(): Flow<List<Contendership>>
}