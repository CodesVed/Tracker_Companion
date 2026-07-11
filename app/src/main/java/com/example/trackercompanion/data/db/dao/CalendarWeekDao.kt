package com.example.trackercompanion.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.trackercompanion.model.CalendarWeek
import kotlinx.coroutines.flow.Flow

@Dao
interface CalendarWeekDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(calendarWeek: CalendarWeek)

    @Insert
    suspend fun insertAll(calendarWeeks: List<CalendarWeek>)

    @Update
    suspend fun update(calendarWeek: CalendarWeek)

    @Query("DELETE FROM CalendarWeek WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM CalendarWeek")
    fun getAllCalendarWeeks(): Flow<List<CalendarWeek>>
}