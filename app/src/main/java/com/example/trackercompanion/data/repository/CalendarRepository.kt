package com.example.trackercompanion.data.repository

import com.example.trackercompanion.data.db.dao.CalendarWeekDao
import com.example.trackercompanion.model.CalendarWeek
import kotlinx.coroutines.flow.Flow

class CalendarRepository(private val calendarWeekDao: CalendarWeekDao) {

    fun getAllWeeks(): Flow<List<CalendarWeek>> {
        return calendarWeekDao.getAllCalendarWeeks()
    }

    suspend fun addWeek(calendarWeek: CalendarWeek) {
        calendarWeekDao.add(calendarWeek)
    }

    suspend fun editWeek(calendarWeek: CalendarWeek) {
        calendarWeekDao.update(calendarWeek)
    }

    suspend fun deleteWeek(calendarWeek: CalendarWeek) {
        calendarWeekDao.delete(calendarWeek.id)
    }
}