package com.example.trackercompanion.ui.shows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trackercompanion.data.db.dao.MatchDao
import com.example.trackercompanion.data.db.dao.PPVEventDao
import com.example.trackercompanion.data.db.dao.ShowEpisodeDao
import com.example.trackercompanion.model.Match
import com.example.trackercompanion.model.PPVEvent
import com.example.trackercompanion.model.ShowEpisode
import com.example.trackercompanion.model.enums.Show
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.collections.indexOfFirst

class ShowsViewModel(
    private val showEpisodeDao: ShowEpisodeDao,
    private val ppvEventDao: PPVEventDao,
    private val matchDao: MatchDao
): ViewModel() {
    val episodes: StateFlow<List<ShowEpisode>> = showEpisodeDao.getAllShowEpisodes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val ppvEvents: StateFlow<List<PPVEvent>> = ppvEventDao.getAllPPVEvents()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val matches: StateFlow<List<Match>> = matchDao.getAllMatches()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addEpisode(showEpisode: ShowEpisode) {
        viewModelScope.launch {
            showEpisodeDao.add(showEpisode)
        }
    }

    fun updateEpisode(showEpisode: ShowEpisode) {
        viewModelScope.launch {
            showEpisodeDao.update(showEpisode)
        }
    }

    fun deleteEpisode(showEpisode: ShowEpisode) {
        viewModelScope.launch {
            showEpisodeDao.delete(showEpisode.id)
            matchDao.deleteMatchesForEpisode(showEpisode.id, Show.SHOW)
        }
    }

    fun addPPVEvent(ppvEvent: PPVEvent) {
        viewModelScope.launch {
            ppvEventDao.add(ppvEvent)
        }
    }

    fun updatePPVEvent(ppvEvent: PPVEvent) {
        viewModelScope.launch {
            ppvEventDao.update(ppvEvent)
        }
    }

    fun saveMatch(match: Match) {
        val i = matches.value.indexOfFirst { it.id == match.id }
        viewModelScope.launch {
            // Edit mode — replace existing entry
            if (i != -1) matchDao.update(match)
            // Add mode — append
            else matchDao.add(match)
        }
    }

    fun deleteMatch(match: Match) {
        viewModelScope.launch {
            matchDao.delete(match.id)
        }
    }
}

class ShowsViewModelFactory(
    private val showEpisodeDao: ShowEpisodeDao,
    private val ppvEventDao: PPVEventDao,
    private val matchDao: MatchDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ShowsViewModel(showEpisodeDao, ppvEventDao, matchDao) as T
    }
}
