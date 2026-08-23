package com.example.trackercompanion.data.repository

import com.example.trackercompanion.data.db.dao.WrestlerDao
import com.example.trackercompanion.model.Wrestler
import kotlinx.coroutines.flow.Flow

class WrestlerRepository(private val wrestlerDao: WrestlerDao) {

    fun getAllWrestlers(): Flow<List<Wrestler>> {
        return wrestlerDao.getAllWrestlers()
    }

    suspend fun addWrestler(wrestler: Wrestler) {
        wrestlerDao.add(wrestler)
    }

    suspend fun updateWrestler(wrestler: Wrestler) {
        wrestlerDao.update(wrestler)
    }

    suspend fun deleteWrestler(id: Int) {
        wrestlerDao.delete(id)
    }
}