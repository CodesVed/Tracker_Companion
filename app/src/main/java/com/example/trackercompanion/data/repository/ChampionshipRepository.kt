package com.example.trackercompanion.data.repository

import com.example.trackercompanion.data.db.dao.ChampionshipDao
import com.example.trackercompanion.data.db.dao.ContendershipDao
import com.example.trackercompanion.data.db.dao.TitleReignDao
import com.example.trackercompanion.model.Championship
import com.example.trackercompanion.model.Contendership
import com.example.trackercompanion.model.TitleReign
import kotlinx.coroutines.flow.Flow

class ChampionshipRepository(
    private val championshipDao: ChampionshipDao,
    private val titleReignDao: TitleReignDao,
    private val contendershipDao: ContendershipDao
) {

    fun getAllChampionship(): Flow<List<Championship>> {
        return championshipDao.getAllChampionships()
    }

    fun getAllTitleReigns(): Flow<List<TitleReign>> {
        return titleReignDao.getAllTitleReigns()
    }

    fun getAllContendership(): Flow<List<Contendership>> {
        return contendershipDao.getAllContendership()
    }

    suspend fun addReign(reign: TitleReign) {
        titleReignDao.add(reign)
    }

    suspend fun addContender(contender: Contendership) {
        contendershipDao.add(contender)
    }

    suspend fun updateReign(reign: TitleReign) {
        titleReignDao.update(reign)
    }

    suspend fun updateContender(contender: Contendership) {
        contendershipDao.update(contender)
    }

    suspend fun deleteReign(reign: TitleReign) {
        titleReignDao.delete(reign.id)
    }

    suspend fun deleteContender(contender: Contendership) {
        contendershipDao.delete(contender.id)
    }
}