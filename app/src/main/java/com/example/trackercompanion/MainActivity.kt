package com.example.trackercompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trackercompanion.data.db.AppDatabase
import com.example.trackercompanion.data.repository.SettingsRepository
import com.example.trackercompanion.model.enums.ThemeMode
import com.example.trackercompanion.ui.settings.SettingsViewModel
import com.example.trackercompanion.ui.settings.SettingsViewModelFactory
import com.example.trackercompanion.ui.theme.TrackerCompanionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getInstance(applicationContext)
        val settingsRepository = SettingsRepository(applicationContext)

        setContent {
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModelFactory(settingsRepository)
            )
            val themeMode by settingsViewModel.themeMode.collectAsState()

            val darkTheme = when (themeMode) {
                ThemeMode.DARK -> true
                ThemeMode.LIGHT -> false
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            TrackerCompanionTheme(darkTheme = darkTheme, dynamicColor = false) {
                AppEntry(database = database, settingsViewModel = settingsViewModel)
            }
        }
    }
}