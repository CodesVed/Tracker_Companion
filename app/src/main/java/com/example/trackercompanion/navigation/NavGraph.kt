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
import com.example.trackercompanion.ui.calendar.CalendarScreen
import com.example.trackercompanion.ui.championships.ChampionshipScreen
import com.example.trackercompanion.ui.dashboard.DashboardScreen
import com.example.trackercompanion.ui.roster.RosterScreen
import com.example.trackercompanion.ui.roster.WrestlerDetailScreen
import com.example.trackercompanion.ui.shows.ShowScreen
import com.example.trackercompanion.navigation.Routes.*
import com.example.trackercompanion.ui.roster.AddEditWrestlerScreen

@Composable
fun App() {
    val navController = rememberNavController()

    val wrestlers = remember { mutableStateListOf(*WrestlerData.roster.toTypedArray()) }
    val matches = remember { mutableStateListOf(*ShowData.matches.toTypedArray()) }
    val titles = remember { mutableStateListOf(*ChampionshipData.titles.toTypedArray()) }

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

                    val filteredWrestlers = wrestlers
                        .filter { w ->
                            selectedBrandType == "ALL" || w.brand.toString() == selectedBrandType
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
                        matchesSources = matches,
                        selectedBrand = selectedBrandType,
                        selectedSort = selectedSort,
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
                            navController.navigate(route = AddEditWrestler)
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
                                navController.navigate(route = AddEditWrestler)
                            },
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }
                }

                composable<AddEditWrestler> {
                    AddEditWrestlerScreen(
                        onSave = {newWrestler ->
                            wrestlers.add(newWrestler)
                            navController.popBackStack()
                        },
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }

                composable<Shows> {
                    ShowScreen()
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