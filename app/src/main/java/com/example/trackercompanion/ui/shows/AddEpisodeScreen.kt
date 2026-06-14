package com.example.trackercompanion.ui.shows

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackercompanion.model.PPVEvent
import com.example.trackercompanion.model.ShowEpisode
import com.example.trackercompanion.model.enums.Brand
import com.example.trackercompanion.ui.roster.DropdownField

sealed class AddEpisodeResult {
    data class NewEpisode(val episode: ShowEpisode) : AddEpisodeResult()
    data class NewPPV(val ppv: PPVEvent) : AddEpisodeResult()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEpisodeScreen(
    existingEpisodeCount: Int,
    existingPPVCount: Int,
    onSave: (AddEpisodeResult) -> Unit,
    onBack: () -> Unit
) {
    var showType     by remember { mutableStateOf("RAW") }
    var episodeNumStr by remember { mutableStateOf("") }
    var weekNumStr   by remember { mutableStateOf("") }
    var themeNotes   by remember { mutableStateOf("") }
    var ppvName      by remember { mutableStateOf("") }

    var episodeNumError by remember { mutableStateOf(false) }
    var weekNumError    by remember { mutableStateOf(false) }
    var ppvNameError    by remember { mutableStateOf(false) }

    val isPPV = showType == "PPV"

    val showTypeOptions = listOf("RAW", "SD", "PPV")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Episode",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // ── Show type ──────────────────────────────────
            DropdownField(
                label = "Show Type",
                selected = showType,
                options = showTypeOptions,
                onOptionsSelected = {
                    showType = it
                    // Reset fields when switching type
                    episodeNumStr = ""
                    ppvName = ""
                    episodeNumError = false
                    ppvNameError = false
                }
            )

            // ── PPV-specific fields ────────────────────────
            if (isPPV) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = ppvName,
                    onValueChange = {
                        ppvName = it
                        ppvNameError = false
                    },
                    label = { Text("PPV Name") },
                    placeholder = { Text("e.g. No Way Out, WrestleMania XIX") },
                    isError = ppvNameError,
                    supportingText = {
                        if (ppvNameError) {
                            Text(
                                text = "PPV name cannot be empty",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    singleLine = true
                )
            }

            // ── Episode number (RAW / SD only) ─────────────
            if (!isPPV) {
                OutlinedTextField(
                    value = episodeNumStr,
                    onValueChange = {
                        if (it.all { c -> c.isDigit() }) {
                            episodeNumStr = it
                            episodeNumError = false
                        }
                    },
                    label = { Text("Episode Number") },
                    placeholder = { Text("e.g. ${existingEpisodeCount + 1}") },
                    isError = episodeNumError,
                    supportingText = {
                        if (episodeNumError) {
                            Text(
                                text = "Episode number required",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // ── Week number (all types) ────────────────────
            OutlinedTextField(
                value = weekNumStr,
                onValueChange = {
                    if (it.all { c -> c.isDigit() }) {
                        weekNumStr = it
                        weekNumError = false
                    }
                },
                label = { Text("Week Number") },
                placeholder = { Text("e.g. 12") },
                isError = weekNumError,
                supportingText = {
                    if (weekNumError) {
                        Text(
                            text = "Week number required",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // ── Theme / notes ──────────────────────────────
            OutlinedTextField(
                value = themeNotes,
                onValueChange = { themeNotes = it },
                label = { Text("Theme/Notes (optional)") },
                placeholder = {
                    Text(
                        if (isPPV) "e.g. First PPV of the season"
                        else "e.g. IC Tournament Round 2"
                    )
                },
                minLines = 2,
                maxLines = 4,
                modifier = Modifier.fillMaxWidth()
            )

            // ── Info text ──────────────────────────────────
            Text(
                text = if (isPPV)
                    "After saving, you'll be taken directly to the PPV card to start logging matches."
                else
                    "After saving, you'll be taken directly to the episode card to start logging matches.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            // ── Save button ────────────────────────────────
            Button(
                modifier = Modifier.fillMaxWidth().height(52.dp),
                onClick = {
                    var valid = true

                    val weekNum = weekNumStr.toIntOrNull()
                    if (weekNum == null) {
                        weekNumError = true
                        valid = false
                    }

                    if (isPPV) {
                        if (ppvName.isBlank()) {
                            ppvNameError = true
                            valid = false
                        }
                    } else {
                        if (episodeNumStr.isBlank()) {
                            episodeNumError = true
                            valid = false
                        }
                    }

                    if (!valid) return@Button

                    val result = if (isPPV) {
                        AddEpisodeResult.NewPPV(
                            PPVEvent(
                                id = System.currentTimeMillis().toInt(),
                                ppvNumber = existingPPVCount + 1,
                                name = ppvName.trim(),
                                notes = themeNotes.trim()
                            )
                        )
                    } else {
                        AddEpisodeResult.NewEpisode(
                            ShowEpisode(
                                id = System.currentTimeMillis().toInt(),
                                episodeNumber = episodeNumStr.toIntOrNull()
                                    ?: (existingEpisodeCount + 1),
                                brand = if (showType == "RAW") Brand.RAW else Brand.SD,
                                weekNumber = weekNum!!,
                                notes = themeNotes.trim()
                            )
                        )
                    }

                    onSave(result)
                }
            ) {
                Text(
                    text = "Save & Open Card",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddEpisodePreview() {
    AddEpisodeScreen(
        existingEpisodeCount = 9,
        existingPPVCount = 1,
        onSave = {},
        onBack = {}
    )
}