package com.example.trackercompanion.ui.championships

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trackercompanion.data.ChampionshipData
import com.example.trackercompanion.data.db.dao.ChampionshipDao
import com.example.trackercompanion.data.db.dao.ContendershipDao
import com.example.trackercompanion.data.db.dao.TitleReignDao
import com.example.trackercompanion.model.Championship
import com.example.trackercompanion.model.Contendership
import com.example.trackercompanion.model.Match
import com.example.trackercompanion.model.TitleReign
import com.example.trackercompanion.model.Wrestler
import com.example.trackercompanion.model.enums.Brand
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.collections.filter
import kotlin.collections.find
import kotlin.collections.indexOfFirst
import kotlin.collections.indices

class ChampionshipsViewModel(
    private val championshipDao: ChampionshipDao,
    private val reignDao: TitleReignDao,
    private val contendershipDao: ContendershipDao
): ViewModel() {

    val championships: StateFlow<List<Championship>> = championshipDao.getAllChampionships()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val reigns: StateFlow<List<TitleReign>> = reignDao.getAllTitleReigns()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val contenders: StateFlow<List<Contendership>> = contendershipDao.getAllContendership()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    fun closingReign(reign: TitleReign?) {
        viewModelScope.launch {
            reign?.let { reignDao.update(it) }
        }
    }

    fun startReign(reign: TitleReign?) {
        viewModelScope.launch {
            reign?.let { reignDao.add(it) }
        }
    }

    fun addContender(contender: Contendership) {
        viewModelScope.launch {
            contendershipDao.add(contender)
        }
    }

    fun moveContenderUp(contender: Contendership) {
        val currentRank = contender.rank
        if (currentRank > 1) {
            val other = contenders.value.find { it.titleId == contender.titleId && it.rank == currentRank - 1 }
            if (other != null) {
                viewModelScope.launch {
                    contendershipDao.update(contender.copy(rank = currentRank - 1))
                    contendershipDao.update(other.copy(rank = currentRank))
                }
            }
        }
    }

    fun moveContenderDown(contender: Contendership) {
        val currentRank = contender.rank
        val titleContenders = contenders.value.filter { it.titleId == contender.titleId }
        if (currentRank < titleContenders.size) {
            val other = contenders.value.find { it.titleId == contender.titleId && it.rank == currentRank + 1 }
            if (other != null) {
                val idx1 = contenders.value.indexOfFirst { it.id == contender.id }
                val idx2 = contenders.value.indexOfFirst { it.id == other.id }
                if (idx1 != -1 && idx2 != -1) {
                    viewModelScope.launch {
                        contendershipDao.update(contender.copy(rank = currentRank + 1))
                        contendershipDao.update(other.copy(rank = currentRank))
                    }
                }
            }
        }
    }

    fun removeContender(contender: Contendership) {
        val rankToRemove = contender.rank
        viewModelScope.launch {
            contendershipDao.delete(contender.id)
            contenders.value.indices.forEach { i ->
                val c = contenders.value[i]
                if (c.titleId == contender.titleId && c.rank > rankToRemove) {
                    contendershipDao.update(c.copy(rank = c.rank - 1))
                }
            }
        }
    }

    fun suggestContender(
        titleId: Int, titleBrand: Brand?, isTagTitle: Boolean,
        wrestlerIds: List<Wrestler>, matches: List<Match>) {
        val titleContenders = contenders.value.filter { it.titleId == titleId }
        val currentChampionIds = reigns.value
            .find { it.titleId == titleId && it.lostAtEvent == null }
            ?.holderIds?.toSet() ?: emptySet()

        val suggested = ChampionshipData.suggestNextContender(
            titleId = titleId,
            titleBrand = titleBrand,
            isTagTitle = isTagTitle,
            wrestlers = wrestlerIds,
            matches = matches,
            existingContenderIds = titleContenders.flatMap { it.wrestlerIds }.toSet(),
            currentChampionIds = currentChampionIds
        )

        if (suggested.isNotEmpty()) {
            viewModelScope.launch {
                contendershipDao.add(
                    Contendership(
                        id = System.currentTimeMillis().toInt(),
                        titleId = titleId,
                        wrestlerIds = suggested.map { it.id },
                        wrestlerNames = suggested.map { it.name },
                        rank = titleContenders.size + 1
                    )
                )
            }
        }
    }
}

class ChampionshipsViewModelFactory(
    private val championshipDao: ChampionshipDao,
    private val reignDao: TitleReignDao,
    private val contendershipDao: ContendershipDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ChampionshipsViewModel(championshipDao, reignDao, contendershipDao) as T
    }
}