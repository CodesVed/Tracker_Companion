package com.example.trackercompanion.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.trackercompanion.data.ChampionshipData
import com.example.trackercompanion.data.ShowData
import com.example.trackercompanion.data.WrestlerData
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
                    RosterScreen(
                        wrestlers = wrestlers,
                        onWrestlerClick = {id ->
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
                        WrestlerDetailScreen(
                            wrestler = wrestler,
                            matchHistory = matches.filter {match ->
                                if (match.isTagMatch) {
                                    wrestler.id in match.participantIds
                                } else {
                                    wrestler.name in match.participants
                                }
                            },
                            titleHistory = titles.filter {title ->
                                title.championName == wrestler.name ||
                                title.championName?.contains(wrestler.name) == true ||
                                title.partnerName == wrestler.name
                            },
                            onEditClick = {
                                navController.navigate(AddEditWrestler)
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