package com.example.trackercompanion.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trackercompanion.data.db.dao.CalendarWeekDao
import com.example.trackercompanion.model.CalendarWeek
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CalendarViewModel(
    private val calendarWeekDao: CalendarWeekDao
): ViewModel() {

    val weeks: StateFlow<List<CalendarWeek>> = calendarWeekDao.getAllCalendarWeeks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addWeek(calendarWeek: CalendarWeek) {
        viewModelScope.launch {
            calendarWeekDao.add(calendarWeek)
        }
    }

    fun editWeek(calendarWeek: CalendarWeek) {
        viewModelScope.launch {
            calendarWeekDao.update(calendarWeek)
        }
    }

    fun deleteWeek(calendarWeek: CalendarWeek) {
        viewModelScope.launch {
            calendarWeekDao.delete(calendarWeek.id)
        }
    }
}

class CalendarViewModelFactory(
    private val calendarWeekDao: CalendarWeekDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return CalendarViewModel(calendarWeekDao) as T
    }
}