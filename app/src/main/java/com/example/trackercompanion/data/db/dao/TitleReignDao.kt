package com.example.trackercompanion.data.db.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.trackercompanion.model.TitleReign
import kotlinx.coroutines.flow.Flow

interface TitleReignDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(titleReign: TitleReign)

    @Update
    suspend fun update(titleReign: TitleReign)

    @Query("DELETE FROM TitleReign WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM TitleReign")
    fun getAllTitleReigns(): Flow<List<TitleReign>>
}