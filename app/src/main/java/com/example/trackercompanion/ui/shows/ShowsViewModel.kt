package com.example.trackercompanion.ui.shows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trackercompanion.data.repository.ShowRepository
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
    private val showRepository: ShowRepository,
): ViewModel() {
    val episodes: StateFlow<List<ShowEpisode>> = showRepository.getAllEpisodes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val ppvEvents: StateFlow<List<PPVEvent>> = showRepository.getAllPPVEvents()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val matches: StateFlow<List<Match>> = showRepository.getAllMatches()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addEpisode(showEpisode: ShowEpisode) {
        viewModelScope.launch {
            showRepository.addEpisode(showEpisode)
        }
    }

    fun updateEpisode(showEpisode: ShowEpisode) {
        viewModelScope.launch {
            showRepository.updateEpisode(showEpisode)
        }
    }

    fun deleteEpisode(showEpisode: ShowEpisode) {
        viewModelScope.launch {
            showRepository.deleteEpisode(showEpisode)
            showRepository.deleteMatchesForEpisode(showEpisode.id, Show.SHOW)
        }
    }

    fun addPPVEvent(ppvEvent: PPVEvent) {
        viewModelScope.launch {
            showRepository.addPPVEvent(ppvEvent)
        }
    }

    fun updatePPVEvent(ppvEvent: PPVEvent) {
        viewModelScope.launch {
            showRepository.updatePPVEvent(ppvEvent)
        }
    }

    fun saveMatch(match: Match) {
        val i = matches.value.indexOfFirst { it.id == match.id }
        viewModelScope.launch {
            // Edit mode — replace existing entry
            if (i != -1) showRepository.updateMatch(match)
            // Add mode — append
            else showRepository.addMatch(match)
        }
    }

    fun deleteMatch(match: Match) {
        viewModelScope.launch {
            showRepository.deleteMatch(match)
        }
    }

    fun toggleEpisodeComplete(episode: ShowEpisode) {
        viewModelScope.launch {
            showRepository.setEpisodeComplete(episode.id, !episode.isComplete)
        }
    }

    fun togglePPVComplete(episode: PPVEvent) {
        viewModelScope.launch {
            showRepository.setPPVComplete(episode.id, !episode.isComplete)
        }
    }
}

class ShowsViewModelFactory(
    private val showRepository: ShowRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ShowsViewModel(showRepository) as T
    }
}
