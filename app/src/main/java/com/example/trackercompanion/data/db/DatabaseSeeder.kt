package com.example.trackercompanion.data.db

import com.example.trackercompanion.data.CalendarData
import com.example.trackercompanion.data.ChampionshipData
import com.example.trackercompanion.data.ShowData
import com.example.trackercompanion.data.WrestlerData

class DatabaseSeeder(private val database: AppDatabase) {

    suspend fun seedIfEmpty() {
        val wrestlerDao = database.getWrestlerDao()

        if (wrestlerDao.getAllWrestlersOnce().isEmpty()) {
            // In order of data referenced by id first
            wrestlerDao.insertAll(WrestlerData.roster)

            database.getChampionshipDao().insertAll(ChampionshipData.titles)
        }
    }

    suspend fun resetUniverse() {
        database.clearAllTables()
        seedIfEmpty()
    }
}