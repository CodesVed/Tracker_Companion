package com.example.trackercompanion.ui.roster

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trackercompanion.data.db.dao.MatchDao
import com.example.trackercompanion.data.db.dao.WrestlerDao
import com.example.trackercompanion.model.Match
import com.example.trackercompanion.model.Wrestler
import com.example.trackercompanion.model.computeStatsForWrestler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WrestlerViewModel(
    private val wrestlerDao: WrestlerDao,
    private val matchDao: MatchDao
): ViewModel() {
    val wrestlers: StateFlow<List<Wrestler>> = wrestlerDao.getAllWrestlers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val matches: StateFlow<List<Match>> = matchDao.getAllMatches()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedBrand = MutableStateFlow("ALL")
    val selectedBrand = _selectedBrand.asStateFlow()

    private val _selectedSort = MutableStateFlow("Name")
    val selectedSort = _selectedSort.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val filteredWrestlers: StateFlow<List<Wrestler>> =
        combine(wrestlers, matches, selectedBrand, selectedSort, searchQuery){w, m, brand, sort, query ->
            w.filter {
              brand == "ALL" || it.brand.toString() == brand
            }. filter {
                query.isBlank() || it.name.contains(query, ignoreCase = true)
            }.sortedWith(
                when (sort) {
                    "Points" -> compareByDescending { computeStatsForWrestler(it.id, m).points }
                    "Win Rate" -> compareByDescending { computeStatsForWrestler(it.id, m).winPercent }
                    "Name" -> compareBy { it.name }
                    else -> compareBy { it.name }
                }
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addWrestler(wrestler: Wrestler) {
        viewModelScope.launch {
            wrestlerDao.add(wrestler)
        }
    }

    fun updateWrestler(wrestler: Wrestler) {
        viewModelScope.launch {
            wrestlerDao.update(wrestler)
        }
    }

    fun deleteWrestler(id: Int) {
        viewModelScope.launch {
            wrestlerDao.delete(id)
        }
    }

    fun onBrandSelected(brand: String) {
        _selectedBrand.value = brand
    }

    fun onSortSelected(sort: String) {
        _selectedSort.value = sort
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}

class WrestlerViewModelFactory(
    private val wrestlerDao: WrestlerDao,
    private val matchDao: MatchDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return WrestlerViewModel(wrestlerDao, matchDao) as T
    }
}