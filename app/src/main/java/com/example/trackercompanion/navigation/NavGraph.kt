package com.example.trackercompanion.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.trackercompanion.data.db.AppDatabase
import com.example.trackercompanion.data.repository.CalendarRepository
import com.example.trackercompanion.data.repository.ChampionshipRepository
import com.example.trackercompanion.data.repository.ShowRepository
import com.example.trackercompanion.data.repository.WrestlerRepository
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
import com.example.trackercompanion.ui.calendar.CalendarViewModel
import com.example.trackercompanion.ui.calendar.CalendarViewModelFactory
import com.example.trackercompanion.ui.championships.AddContenderBottomSheet
import com.example.trackercompanion.ui.championships.ChampionshipsViewModel
import com.example.trackercompanion.ui.championships.ChampionshipsViewModelFactory
import com.example.trackercompanion.ui.championships.LogTitleChangeBottomSheet
import com.example.trackercompanion.ui.championships.TitleDetailScreen
import com.example.trackercompanion.ui.roster.AddEditWrestlerScreen
import com.example.trackercompanion.ui.roster.WrestlerViewModel
import com.example.trackercompanion.ui.roster.WrestlerViewModelFactory
import com.example.trackercompanion.ui.shows.AddEpisodeResult
import com.example.trackercompanion.ui.shows.AddEpisodeScreen
import com.example.trackercompanion.ui.shows.EpisodeDetailScreen
import com.example.trackercompanion.ui.shows.ShowSource
import com.example.trackercompanion.ui.shows.ShowsViewModel
import com.example.trackercompanion.ui.shows.ShowsViewModelFactory

@Composable
fun App(database: AppDatabase) {
    val navController = rememberNavController()

    val calendarRepository = CalendarRepository(database.getCalendarWeekDao())
    val wrestlerRepository = WrestlerRepository(database.getWrestlerDao())
    val showRepository = ShowRepository(
        database.getShowEpisodeDao(),
        database.getMatchDao(),
        database.getPPVEventDao()
    )
    val championshipRepository = ChampionshipRepository(
        database.getChampionshipDao(),
        database.getTitleReignDao(),
        database.getContendershipDao()
    )

    val wrestlerViewModel: WrestlerViewModel = viewModel(
        factory = WrestlerViewModelFactory(
            wrestlerRepository,
            showRepository
        )
    )
    val showsViewModel: ShowsViewModel = viewModel(
        factory = ShowsViewModelFactory(showRepository)
    )
    val championshipsViewModel: ChampionshipsViewModel = viewModel(
        factory = ChampionshipsViewModelFactory(championshipRepository)
    )
    val calendarViewModel: CalendarViewModel = viewModel(
        factory = CalendarViewModelFactory(calendarRepository)
    )

    val wrestlers by wrestlerViewModel.wrestlers.collectAsState()
    val matches by showsViewModel.matches.collectAsState()
    val episodes by showsViewModel.episodes.collectAsState()
    val ppvEvents by showsViewModel.ppvEvents.collectAsState()
    val championships by championshipsViewModel.championships.collectAsState()
    val reigns by championshipsViewModel.reigns.collectAsState()
    val contenders by championshipsViewModel.contenders.collectAsState()
    val calendarWeeks by calendarViewModel.weeks.collectAsState()

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
                            showsViewModel.updateEpisode(edited)
                        },
                        onEpisodeDeleted = { deleted ->
                            showsViewModel.deleteEpisode(deleted)
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
                                showsViewModel.saveMatch(savedMatch)
                            },
                            onMatchDeleted = { deletedMatch ->
                                showsViewModel.deleteMatch(deletedMatch)
                            },
                            onEpisodeEdited = { edited ->
                                showsViewModel.updateEpisode(edited)
                            },
                            onPPVEdited = { edited ->
                                showsViewModel.updatePPVEvent(edited)
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
                                    showsViewModel.addEpisode(result.episode)
                                    navController.navigate(
                                        route = EpisodeDetail(result.episode.id, isPPV = false)
                                    ) {
                                        popUpTo<AddEpisode> {inclusive = true}
                                    }
                                }
                                is AddEpisodeResult.NewPPV -> {
                                    showsViewModel.addPPVEvent(result.ppv)
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
                    var showAddContender by rememberSaveable { mutableStateOf(false) }
                    val isTagTitle  = title?.title?.contains("Tag", ignoreCase = true)

                    if (title != null) {
                        var showLogTitleChange by rememberSaveable { mutableStateOf(false) }

                        TitleDetailScreen(
                            title = title,
                            allReignsForTitle = reigns.filter { it.titleId == title.id },
                            contenders = contenders.filter { it.titleId == title.id },
                            onAddContenderClick = {
                                showAddContender = true
                            },
                            onSuggestContenderClick = {
                                championshipsViewModel.suggestContender(
                                    titleId = title.id,
                                    titleBrand = title.brand,
                                    isTagTitle = isTagTitle == true,
                                    wrestlerIds = wrestlers,
                                    matches = matches
                                )
                            },
                            onMoveUp = { contender ->
                                championshipsViewModel.moveContenderUp(contender)
                            },
                            onMoveDown = { contender ->
                                championshipsViewModel.moveContenderDown(contender)
                            },
                            onRemove = { contender ->
                                championshipsViewModel.removeContender(contender)
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
                                    championshipsViewModel.closingReign(closedReign)
                                    championshipsViewModel.startReign(newReign)
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
                                    championshipsViewModel.addContender(
                                        Contendership(
                                            id = System.currentTimeMillis().toInt(),
                                            titleId = title.id,
                                            wrestlerIds = selectedWrestlers.map { it.id },
                                            wrestlerNames = selectedWrestlers.map { it.name },
                                            rank = nextRank
                                        )
                                    )
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
                    var showAddEditWeek by rememberSaveable { mutableStateOf(false) }
                    var editingWeekId by rememberSaveable { mutableStateOf<Int?>(null) }
                    val editingWeek = calendarWeeks.find { it.id == editingWeekId }

                    CalendarScreen(
                        weeks = calendarWeeks,
                        onWeekClick = { week ->
                            when {
                                week.linkedPPVId != null ->
                                    navController.navigate(EpisodeDetail(episodeId = week.linkedPPVId, isPPV = true))
                                week.linkedShowId != null ->
                                    navController.navigate(EpisodeDetail(episodeId = week.linkedShowId, isPPV = false))
                                else -> {
                                    editingWeekId = week.id
                                    showAddEditWeek = true
                                }
                            }
                        },
                        onAddWeekClick = {
                            editingWeekId = null
                            showAddEditWeek = true
                        },
                        onWeekLongPress = { week ->
                            editingWeekId = week.id
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
                                    calendarViewModel.editWeek(saved)
                                } else {
                                    calendarViewModel.addWeek(saved)
                                }
                                showAddEditWeek = false
                                editingWeekId = null
                            },
                            onDelete = { toDelete ->
                                calendarViewModel.deleteWeek(toDelete)
                                showAddEditWeek = false
                                editingWeekId = null
                            },
                            onDismiss = {
                                showAddEditWeek = false
                                editingWeekId = null
                            }
                        )
                    }
                }
            }
        }
    }
}