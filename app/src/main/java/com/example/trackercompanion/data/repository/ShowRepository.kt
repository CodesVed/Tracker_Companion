package com.example.trackercompanion.data.repository

import com.example.trackercompanion.data.db.dao.MatchDao
import com.example.trackercompanion.data.db.dao.PPVEventDao
import com.example.trackercompanion.data.db.dao.ShowEpisodeDao
import com.example.trackercompanion.model.Match
import com.example.trackercompanion.model.PPVEvent
import com.example.trackercompanion.model.ShowEpisode
import com.example.trackercompanion.model.enums.Show
import kotlinx.coroutines.flow.Flow

class ShowRepository(
    private val showEpisodeDao: ShowEpisodeDao,
    private val matchDao: MatchDao,
    private val ppvEventDao: PPVEventDao
) {

    fun getAllEpisodes(): Flow<List<ShowEpisode>> {
        return showEpisodeDao.getAllShowEpisodes()
    }

    fun getAllMatches(): Flow<List<Match>> {
        return matchDao.getAllMatches()
    }

    fun getAllPPVEvents(): Flow<List<PPVEvent>> {
        return ppvEventDao.getAllPPVEvents()
    }


    suspend fun addEpisode(episode: ShowEpisode) {
        showEpisodeDao.add(episode)
    }

    suspend fun addMatch(match: Match) {
        matchDao.add(match)
    }

    suspend fun updateEpisode(episode: ShowEpisode) {
        showEpisodeDao.update(episode)
    }

    suspend fun updateMatch(match: Match) {
        matchDao.update(match)
    }

    suspend fun deleteEpisode(episode: ShowEpisode) {
        showEpisodeDao.delete(episode.id)
    }

    suspend fun deleteMatchesForEpisode(episodeId: Int, episodeType: Show) {
        matchDao.deleteMatchesForEpisode(episodeId, episodeType)
    }

    suspend fun deleteMatch(match: Match) {
        matchDao.delete(match.id)
    }

    suspend fun addPPVEvent(ppvEvent: PPVEvent) {
        ppvEventDao.add(ppvEvent)
    }

    suspend fun updatePPVEvent(ppvEvent: PPVEvent) {
        ppvEventDao.update(ppvEvent)
    }

    suspend fun deletePPVEvent(ppvEvent: PPVEvent) {
        ppvEventDao.delete(ppvEvent.id)
    }

    suspend fun setEpisodeComplete(id: Int, isComplete: Boolean) {
        showEpisodeDao.setComplete(id, isComplete)
    }

    suspend fun setPPVComplete(id: Int, isComplete: Boolean) {
        ppvEventDao.setComplete(id, isComplete)
    }
}