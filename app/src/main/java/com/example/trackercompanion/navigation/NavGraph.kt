package com.example.trackercompanion.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.trackercompanion.ui.calendar.CalendarScreen
import com.example.trackercompanion.ui.championships.ChampionshipScreen
import com.example.trackercompanion.ui.dashboard.DashboardScreen
import com.example.trackercompanion.ui.roster.RosterScreen
import com.example.trackercompanion.ui.shows.ShowScreen

@Composable
fun App() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) {innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Navigation.Main,
            modifier = Modifier.padding(innerPadding)) {
            navigation<Navigation.Main>(startDestination = Routes.Dashboard) {
                composable<Routes.Dashboard> {
                    DashboardScreen()
                }
                composable<Routes.Roster> {
                    RosterScreen()
                }
                composable<Routes.Shows> {
                    ShowScreen()
                }
                composable<Routes.Championships> {
                    ChampionshipScreen()
                }
                composable<Routes.Calendar> {
                    CalendarScreen()
                }
            }
        }
    }
}