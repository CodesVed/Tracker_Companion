package com.example.trackercompanion.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.trackercompanion.data.ChampionshipData
import com.example.trackercompanion.data.ShowData
import com.example.trackercompanion.data.WrestlerData
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
import com.example.trackercompanion.ui.roster.AddEditWrestlerScreen
import com.example.trackercompanion.ui.shows.AddEpisodeResult
import com.example.trackercompanion.ui.shows.AddEpisodeScreen
import com.example.trackercompanion.ui.shows.EpisodeDetailScreen
import com.example.trackercompanion.ui.shows.ShowSource

@Composable
fun App() {
    val navController = rememberNavController()

    val wrestlers = remember { mutableStateListOf(*WrestlerData.roster.toTypedArray()) }
    val matches = remember { mutableStateListOf(*ShowData.matches.toTypedArray()) }
    val episodes = remember { mutableStateListOf(*ShowData.episodes.toTypedArray()) }
    val ppvEvents = remember { mutableStateListOf(*ShowData.ppvEvents.toTypedArray()) }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) {innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Navigation.Main,
            modifier = Modifier.padding(innerPadding)) {
            navigation<Navigation.Main>(startDestination = Dashboard) {
                composable<Dashboard> {
                    DashboardScreen()
                }

                composable<Roster> {
                    var selectedBrandType by remember { mutableStateOf("ALL") }
                    var selectedSort by remember { mutableStateOf("Name") }
                    var searchQuery by remember { mutableStateOf("") }

                    val filteredWrestlers = wrestlers
                        .filter { w ->
                            selectedBrandType == "ALL" || w.brand.toString() == selectedBrandType
                        }
                        .filter { w ->
                            searchQuery.isBlank() ||
                            w.name.contains(searchQuery, ignoreCase = true)
                        }
                        .sortedWith(
                            when (selectedSort) {
                                "Points" -> compareByDescending { computeStatsForWrestler(it.id, matches).points }
                                "Win Rate" -> compareByDescending { computeStatsForWrestler(it.id, matches).winPercent }
                                "Name" -> compareBy { it.name }
                                else -> compareBy { it.name }
                            }
                        )

                    RosterScreen(
                        wrestlers = filteredWrestlers,
                        matcheSources = matches,
                        selectedBrand = selectedBrandType,
                        selectedSort = selectedSort,
                        searchQuery = searchQuery,
                        onSearchQueryChanged = {
                            searchQuery = it
                        },
                        onBrandSelected = {
                            selectedBrandType = it
                        },
                        onSortSelected = {
                            selectedSort = it
                        },
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
                            titleReigns = ChampionshipData.getReignsForWrestler(wrestler.id),
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
                                wrestlers.add(saved)
                            } else {
                                val index = wrestlers.indexOfFirst { it.id == saved.id }
                                if (index != -1) wrestlers[index] = saved
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
                            if (i != -1) episodes[i] = edited
                        },
                        onEpisodeDeleted = { deleted ->
                            episodes.removeIf { it.id == deleted.id }
                            // Also delete all matches belonging to this episode
                            matches.removeIf {
                                it.showId == deleted.id && it.showType == Show.SHOW
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
                                if (i != -1) matches[i] = savedMatch
                                // Add mode — append
                                else matches.add(savedMatch)
                            },
                            onMatchDeleted = { deletedMatch ->
                                matches.removeIf { it.id == deletedMatch.id }
                            },
                            onEpisodeEdited = { edited ->
                                val i = episodes.indexOfFirst { it.id == edited.id }
                                if (i != -1) episodes[i] = edited
                            },
                            onPPVEdited = { edited ->
                                val i = ppvEvents.indexOfFirst { it.id == edited.id }
                                if (i != -1) ppvEvents[i] = edited
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
                                    episodes.add(result.episode)
                                    navController.navigate(
                                        route = EpisodeDetail(result.episode.id, isPPV = false)
                                    ) {
                                        popUpTo<AddEpisode> {inclusive = true}
                                    }
                                }
                                is AddEpisodeResult.NewPPV -> {
                                    ppvEvents.add(result.ppv)
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
                    ChampionshipScreen()
                }
                composable<Calendar> {
                    CalendarScreen()
                }
            }
        }
    }
}