package com.example.trackercompanion.ui.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.SettingsSystemDaydream
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackercompanion.model.enums.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    themeMode: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit,
    onResetUniverseClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit
) {
    val options = listOf(
        ThemeMode.LIGHT to Icons.Default.LightMode,
        ThemeMode.DARK to Icons.Default.DarkMode,
        ThemeMode.SYSTEM to Icons.Default.SettingsSystemDaydream
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // ── Theme section ──────────────────────────
            Text(
                text = "Appearance",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                options.forEachIndexed { index, (mode, icon) ->
                    SegmentedButton(
                        selected = themeMode == mode,
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = options.size
                        ),
                        icon = {
                            SegmentedButtonDefaults.Icon(active = themeMode == mode) {
                                Icon(imageVector = icon, contentDescription = mode.name)
                            }
                        },
                        onClick = { onThemeSelected(mode) },
                    ) {
                        Text(text = mode.name)
                    }
                }
            }

            HorizontalDivider()

            // ── Danger zone ─────────────────────────────
            Text(
                text = "Universe",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Reset Universe will clear the existing simulation data and start everything from scratch.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                onClick = onResetUniverseClick,
            ) {
                Text(text = "Reset Universe")
            }
        }
    }
}

@Composable
fun ConfirmResetDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(imageVector = Icons.Default.Warning, contentDescription = null) },
        title = { Text(text = "Are you sure about resetting current universe?") },
        text = {
            Text(text = "This permanently deletes all shows, matches, title reigns, contenderships, and calendar weeks. Your wrestler roster and championship titles will be restored to their starting state. This cannot be undone.")
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = "Reset", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun SettingsPreview() {
    SettingsScreen(
        themeMode = ThemeMode.DARK,
        onThemeSelected = {},
        onResetUniverseClick = {},
        snackbarHostState = SnackbarHostState(),
        onBackClick = {}
    )
}