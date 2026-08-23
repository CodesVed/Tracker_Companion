package com.example.trackercompanion.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trackercompanion.data.db.dao.CalendarWeekDao
import com.example.trackercompanion.data.repository.CalendarRepository
import com.example.trackercompanion.model.CalendarWeek
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CalendarViewModel(
    private val calendarRepository: CalendarRepository
): ViewModel() {

    val weeks: StateFlow<List<CalendarWeek>> = calendarRepository.getAllWeeks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addWeek(calendarWeek: CalendarWeek) {
        viewModelScope.launch {
            calendarRepository.addWeek(calendarWeek)
        }
    }

    fun editWeek(calendarWeek: CalendarWeek) {
        viewModelScope.launch {
            calendarRepository.editWeek(calendarWeek)
        }
    }

    fun deleteWeek(calendarWeek: CalendarWeek) {
        viewModelScope.launch {
            calendarRepository.deleteWeek(calendarWeek)
        }
    }
}

class CalendarViewModelFactory(
    private val calendarRepository: CalendarRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return CalendarViewModel(calendarRepository) as T
    }
}