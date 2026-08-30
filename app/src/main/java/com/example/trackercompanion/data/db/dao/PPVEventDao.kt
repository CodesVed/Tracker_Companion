package com.example.trackercompanion.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.trackercompanion.model.PPVEvent
import kotlinx.coroutines.flow.Flow

@Dao
interface PPVEventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(ppvEvent: PPVEvent)

    @Insert
    suspend fun insertAll(ppvEvents: List<PPVEvent>)

    @Update
    suspend fun update(ppvEvent: PPVEvent)

    @Query("DELETE FROM PPVEvent WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM PPVEvent")
    fun getAllPPVEvents(): Flow<List<PPVEvent>>

    @Query("UPDATE PPVEvent SET isComplete = :isComplete WHERE id = :id")
    suspend fun setComplete(id: Int, isComplete: Boolean)
}