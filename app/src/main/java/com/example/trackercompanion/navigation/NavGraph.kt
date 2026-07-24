package com.example.trackercompanion.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.trackercompanion.data.ChampionshipData
import com.example.trackercompanion.data.db.AppDatabase
import com.example.trackercompanion.model.CalendarWeek
import com.example.trackercompanion.model.Contendership
import com.example.trackercompanion.model.computeStatsForWrestler
import com.example.trackercompanion.model.enums.Brand
import com.example.trackercompanion.model.enums.Show
import com.example.trackercompanion.ui.calendar.CalendarScreen
import com.example.trackercompanion.ui.championships.ChampionshipScreen
import com.example.trackercompanion.ui.dashboard.DashboardScreen
import com.example.trackercompanion.ui.roster.RosterScreen
import com.example.trackercompanion.ui.roster.WrestlerDetailScreen
import com.example.trackercompanion.ui.shows.ShowScreen
import com.example.trackercompanion.navigation.Routes.*
import com.example.trackercompanion.ui.calendar.AddEditWeekBottomSheet
import com.example.trackercompanion.ui.championships.AddContenderBottomSheet
import com.example.trackercompanion.ui.championships.LogTitleChangeBottomSheet
import com.example.trackercompanion.ui.championships.TitleDetailScreen
import com.example.trackercompanion.ui.roster.AddEditWrestlerScreen
import com.example.trackercompanion.ui.roster.WrestlerViewModel
import com.example.trackercompanion.ui.roster.WrestlerViewModelFactory
import com.example.trackercompanion.ui.shows.AddEpisodeResult
import com.example.trackercompanion.ui.shows.AddEpisodeScreen
import com.example.trackercompanion.ui.shows.EpisodeDetailScreen
import com.example.trackercompanion.ui.shows.ShowSource
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

@Composable
fun App(database: AppDatabase) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val wrestlerViewModel: WrestlerViewModel = viewModel(
        factory = WrestlerViewModelFactory(database.getWrestlerDao(), database.getMatchDao())
    )
    val championshipDao = database.getChampionshipDao()
    val reignDao = database.getTitleReignDao()
    val contendershipDao = database.getContendershipDao()
    val episodeDao = database.getShowEpisodeDao()
    val ppvEventDao = database.getPPVEventDao()
    val matchesDao = database.getMatchDao()
    val calendarWeekDao = database.getCalendarWeekDao()

    val wrestlers by wrestlerViewModel.wrestlers.collectAsState()
    val matches by matchesDao.getAllMatches().collectAsState(initial = emptyList())
    val episodes by episodeDao.getAllShowEpisodes().collectAsState(initial = emptyList())
    val ppvEvents by ppvEventDao.getAllPPVEvents().collectAsState(initial = emptyList())
    val championships by championshipDao.getAllChampionships().collectAsState(initial = emptyList())
    val reigns by reignDao.getAllTitleReigns().collectAsState(initial = emptyList())
    val contenders by contendershipDao.getAllContendership().collectAsState(initial = emptyList())
    val calendarWeeks by calendarWeekDao.getAllCalendarWeeks().collectAsState(initial = emptyList())

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) {innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Navigation.Main,
            modifier = Modifier.padding(innerPadding)) {
            navigation<Navigation.Main>(startDestination = Dashboard) {
                composable<Dashboard> {
                    DashboardScreen(
                        wrestlers = wrestlers,
                        matches = matches,
                        episodes = episodes,
                        ppvEvents = ppvEvents,
                        championships = championships,
                        reigns = reigns,
                        calendarWeeks = calendarWeeks,
                        onShowClick = { show ->
                            when {
                                show.linkedPPVId != null ->
                                    navController.navigate(EpisodeDetail(episodeId = show.linkedPPVId, isPPV = true))
                                show.linkedShowId != null ->
                                    navController.navigate(EpisodeDetail(episodeId = show.linkedShowId, isPPV = false))
                                else -> {}
                            }
                        },
                        onEpisodeClick = { episode ->
                            navController.navigate(EpisodeDetail(episodeId = episode.id, isPPV = false))
                        },
                        onPPVClick = { ppv ->
                            navController.navigate(EpisodeDetail(episodeId = ppv.id, isPPV = true))
                        }
                    )
                }

                composable<Roster> {
                    val selectedBrandType by wrestlerViewModel.selectedBrand.collectAsState()
                    val selectedSort by wrestlerViewModel.selectedSort.collectAsState()
                    val searchQuery by wrestlerViewModel.searchQuery.collectAsState()

                    val filteredWrestlers by wrestlerViewModel.filteredWrestlers.collectAsState()

                    RosterScreen(
                        wrestlers = filteredWrestlers,
                        matchSources = matches,
                        selectedBrand = selectedBrandType,
                        selectedSort = selectedSort,
                        searchQuery = searchQuery,
                        onSearchQueryChanged = wrestlerViewModel::onSearchQueryChanged,
                        onBrandSelected = wrestlerViewModel::onBrandSelected,
                        onSortSelected = wrestlerViewModel::onSortSelected,
                        onWrestlerClick = { id ->
                            navController.navigate(route = WrestlerDetail(id))
                        },
                        onAddWrestlerClick = {
                            navController.navigate(route = AddEditWrestler())
                        }
                    )
                }

                composable<WrestlerDetail> {backStackEntry ->
                    val detail = backStackEntry.toRoute<WrestlerDetail>()
                    val wrestler = wrestlers.find { it.id == detail.wrestlerId }

                    if (wrestler != null) {
                        val stats = computeStatsForWrestler(wrestler.id, matches)

                        WrestlerDetailScreen(
                            wrestler = wrestler,
                            stats = stats,
                            matchHistory = matches.filter { wrestler.id in it.participantIds },
                            titleReigns = reigns.filter { wrestler.id in it.holderIds },
                            onEditClick = {
                                navController.navigate(route = AddEditWrestler(wrestlerId = wrestler.id))
                            },
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }
                }

                composable<AddEditWrestler> {backStackEntry ->
                    val route = backStackEntry.toRoute<AddEditWrestler>()

                    val existing = if (route.wrestlerId != -1) {
                        wrestlers.find { it.id == route.wrestlerId }
                    } else null

                    AddEditWrestlerScreen(
                        existing = existing,
                        onSave = {saved ->
                            if (existing == null){
                                wrestlerViewModel.addWrestler(saved)
                            } else {
                                wrestlerViewModel.updateWrestler(saved)
                            }
                            navController.popBackStack()
                        },
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }

                composable<Shows> {
                    ShowScreen(
                        episodes = episodes,
                        ppvEvents = ppvEvents,
                        matches = matches,
                        onEpisodeClick = {episode ->
                            navController.navigate(route = EpisodeDetail(episodeId = episode.id, isPPV = false))
                        },
                        onPPVClick = {ppv ->
                            navController.navigate(route = EpisodeDetail(episodeId = ppv.id, isPPV = true))
                        },
                        onAddEpisodeClick = {
                            navController.navigate(route = AddEpisode)
                        },
                        onEpisodeEdited = {edited ->
                            val i = episodes.indexOfFirst { it.id == edited.id }
                            if (i != -1) scope.launch{ episodeDao.update(edited) }
                        },
                        onEpisodeDeleted = { deleted ->
                            scope.launch {
                                episodeDao.delete(deleted.id)
                                // Also delete all matches belonging to this episode
                                matchesDao.deleteMatchesForEpisode(deleted.id, Show.SHOW)
                            }
                        }
                    )
                }

                composable<EpisodeDetail> {backStackEntry ->
                    val route = backStackEntry.toRoute<EpisodeDetail>()

                    val showSource = if (route.isPPV) {
                        val ppv = ppvEvents.find { it.id == route.episodeId }
                        if (ppv != null) ShowSource.PPV(ppv) else null
                    } else {
                        val episode = episodes.find { it.id == route.episodeId }
                        if (episode != null) ShowSource.RegularShow(episode = episode) else null
                    }

                    val showType = if (route.isPPV) {
                        Show.PPV
                    } else {
                        Show.SHOW
                    }

                    val episodeMatches = matches.filter {
                        it.showId == route.episodeId && it.showType == showType
                    }

                    if (showSource != null) {
                        EpisodeDetailScreen(
                            showSource = showSource,
                            matches = episodeMatches,
                            wrestlers = wrestlers,
                            onMatchSaved = {savedMatch ->
                                // Edit mode — replace existing entry
                                val i = matches.indexOfFirst { it.id == savedMatch.id }
                                if (i != -1) scope.launch { matchesDao.update(savedMatch) }
                                // Add mode — append
                                else scope.launch { matchesDao.add(savedMatch) }
                            },
                            onMatchDeleted = { deletedMatch ->
                                scope.launch { matchesDao.delete(deletedMatch.id) }
                            },
                            onEpisodeEdited = { edited ->
                                val i = episodes.indexOfFirst { it.id == edited.id }
                                if (i != -1) scope.launch { episodeDao.update(edited) }
                            },
                            onPPVEdited = { edited ->
                                val i = ppvEvents.indexOfFirst { it.id == edited.id }
                                if (i != -1) scope.launch { ppvEventDao.update(edited) }
                            },
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }
                }

                composable<AddEpisode> {
                    val rawCount = episodes.count { it.brand == Brand.RAW }
                    val sdCount = episodes.count { it.brand == Brand.SD }

                    AddEpisodeScreen(
                        existingEpisodeCount = maxOf(rawCount, sdCount),
                        existingPPVCount = ppvEvents.size,
                        onSave = {result ->
                            when (result) {
                                is AddEpisodeResult.NewEpisode -> {
                                    scope.launch { episodeDao.add(result.episode) }
                                    navController.navigate(
                                        route = EpisodeDetail(result.episode.id, isPPV = false)
                                    ) {
                                        popUpTo<AddEpisode> {inclusive = true}
                                    }
                                }
                                is AddEpisodeResult.NewPPV -> {
                                    scope.launch { ppvEventDao.add(result.ppv) }
                                    navController.navigate(
                                        route = EpisodeDetail(result.ppv.id, isPPV = true)
                                    ) {
                                        popUpTo<AddEpisode> { inclusive = true }
                                    }
                                }
                            }
                        },
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }

                composable<Championships> {
                    ChampionshipScreen(
                        championships = championships,
                        reigns = reigns,
                        onTitleClick = { title ->
                            navController.navigate(route = TitleDetail(titleId = title.id))
                        }
                    )
                }

                composable<TitleDetail> { backStackEntry ->
                    val route = backStackEntry.toRoute<TitleDetail>()
                    val title = championships.find { it.id == route.titleId }
                    var showAddContender by remember { mutableStateOf(false) }
                    val isTagTitle  = title?.title?.contains("Tag", ignoreCase = true)

                    if (title != null) {
                        var showLogTitleChange by remember { mutableStateOf(false) }

                        TitleDetailScreen(
                            title = title,
                            allReignsForTitle = reigns.filter { it.titleId == title.id },
                            contenders = contenders.filter { it.titleId == title.id },
                            onAddContenderClick = {
                                showAddContender = true
                            },
                            onSuggestContenderClick = {
                                val titleContenders = contenders.filter { it.titleId == title.id }
                                val currentChampionIds = reigns
                                    .find { it.titleId == title.id && it.lostAtEvent == null }
                                    ?.holderIds?.toSet() ?: emptySet()

                                val suggested = ChampionshipData.suggestNextContender(
                                    titleId = title.id,
                                    titleBrand = title.brand,
                                    isTagTitle = isTagTitle == true,
                                    wrestlers = wrestlers,
                                    matches = matches,
                                    existingContenderIds = titleContenders.flatMap { it.wrestlerIds }.toSet(),
                                    currentChampionIds = currentChampionIds
                                )

                                if (suggested.isNotEmpty()) {
                                    scope.launch {
                                        contendershipDao.add(
                                            Contendership(
                                                id = System.currentTimeMillis().toInt(),
                                                titleId = title.id,
                                                wrestlerIds = suggested.map { it.id },
                                                wrestlerNames = suggested.map { it.name },
                                                rank = titleContenders.size + 1
                                            )
                                        )
                                    }
                                }
                            },
                            onMoveUp = { contender ->
                                val currentRank = contender.rank
                                if (currentRank > 1) {
                                    val other = contenders.find { it.titleId == title.id && it.rank == currentRank - 1 }
                                    if (other != null) {
                                        val idx1 = contenders.indexOfFirst { it.id == contender.id }
                                        val idx2 = contenders.indexOfFirst { it.id == other.id }
                                        if (idx1 != -1 && idx2 != -1) {
                                            scope.launch {
                                                contendershipDao.update(contender.copy(rank = currentRank - 1))
                                                contendershipDao.update(other.copy(rank = currentRank))
                                            }
                                        }
                                    }
                                }
                            },
                            onMoveDown = { contender ->
                                val currentRank = contender.rank
                                val titleContenders = contenders.filter { it.titleId == title.id }
                                if (currentRank < titleContenders.size) {
                                    val other = contenders.find { it.titleId == title.id && it.rank == currentRank + 1 }
                                    if (other != null) {
                                        val idx1 = contenders.indexOfFirst { it.id == contender.id }
                                        val idx2 = contenders.indexOfFirst { it.id == other.id }
                                        if (idx1 != -1 && idx2 != -1) {
                                            scope.launch {
                                                contendershipDao.update(contender.copy(rank = currentRank + 1))
                                                contendershipDao.update(other.copy(rank = currentRank))
                                            }
                                        }
                                    }
                                }
                            },
                            onRemove = { contender ->
                                val rankToRemove = contender.rank
                                scope.launch { contendershipDao.delete(contender.id) }
                                contenders.indices.forEach { i ->
                                    val c = contenders[i]
                                    if (c.titleId == title.id && c.rank > rankToRemove) {
                                        scope.launch { contendershipDao.update(c.copy(rank = c.rank - 1)) }
                                    }
                                }
                            },
                            onLogTitleChangeClick = {
                                showLogTitleChange = true
                            },
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )

                        if (showLogTitleChange) {
                            val currentReign = reigns.find { it.titleId == title.id && it.lostAtEvent == null}
                            val isTagTitle = title.title.contains("Tag", ignoreCase = true)

                            LogTitleChangeBottomSheet(
                                title = title,
                                currentReign = currentReign,
                                nextReignNumber = currentReign?.reignNumber?.plus(1) ?: 1,
                                isTagTitle = isTagTitle,
                                wrestlers = wrestlers,
                                onSave = { closedReign, newReign ->
                                    if (closedReign != null) {
                                        val i = reigns.indexOfFirst { it.id == closedReign.id }
                                        if (i != -1) scope.launch { reignDao.update(closedReign) }
                                    }
                                    if (newReign != null) {
                                        scope.launch { reignDao.add(newReign) }
                                    }
                                    showLogTitleChange = false
                                },
                                onDismiss = {
                                    showLogTitleChange = false
                                }
                            )
                        }

                        if (showAddContender) {
                            val titleContenders = contenders.filter { it.titleId == title.id }
                            val alreadyContendingIds = titleContenders.flatMap { it.wrestlerIds }.toSet()

                            val eligibleWrestlers = wrestlers.filter { w ->
                                (title.brand == null || w.brand == title.brand) &&
                                        w.id !in alreadyContendingIds
                            }

                            AddContenderBottomSheet(
                                isTagTitle = isTagTitle == true,
                                eligibleWrestlers = eligibleWrestlers,
                                currentContenderCount = titleContenders.size,
                                onSave = { selectedWrestlers ->
                                    val nextRank = titleContenders.size + 1
                                    scope.launch {
                                        contendershipDao.add(
                                            Contendership(
                                                id = System.currentTimeMillis().toInt(),
                                                titleId = title.id,
                                                wrestlerIds = selectedWrestlers.map { it.id },
                                                wrestlerNames = selectedWrestlers.map { it.name },
                                                rank = nextRank
                                            )
                                        )
                                    }
                                    showAddContender = false
                                },
                                onDismiss = {
                                    showAddContender = false
                                }
                            )
                        }
                    }
                }

                composable<Calendar> {
                    var showAddEditWeek by remember { mutableStateOf(false) }
                    var editingWeek by remember { mutableStateOf<CalendarWeek?>(null) }

                    CalendarScreen(
                        weeks = calendarWeeks,
                        onWeekClick = { week ->
                            when {
                                week.linkedPPVId != null ->
                                    navController.navigate(EpisodeDetail(episodeId = week.linkedPPVId, isPPV = true))
                                week.linkedShowId != null ->
                                    navController.navigate(EpisodeDetail(episodeId = week.linkedShowId, isPPV = false))
                                else -> {
                                    editingWeek = week
                                    showAddEditWeek = true
                                }
                            }
                        },
                        onAddWeekClick = {
                            editingWeek = null
                            showAddEditWeek = true
                        },
                        onWeekLongPress = { week ->
                            editingWeek = week
                            showAddEditWeek = true
                        }
                    )

                    if (showAddEditWeek) {
                        AddEditWeekBottomSheet(
                            existing = editingWeek,
                            episodes = episodes,
                            ppvEvents = ppvEvents,
                            onSave = { saved ->
                                if (saved.id != 0) {
                                    scope.launch { calendarWeekDao.update(saved) }
                                } else {
                                    scope.launch { calendarWeekDao.add(saved) }
                                }
                                showAddEditWeek = false
                                editingWeek = null
                            },
                            onDelete = { toDelete ->
                                scope.launch { calendarWeekDao.delete(toDelete.id) }
                                showAddEditWeek = false
                                editingWeek = null
                            },
                            onDismiss = {
                                showAddEditWeek = false
                                editingWeek = null
                            }
                        )
                    }
                }
            }
        }
    }
}