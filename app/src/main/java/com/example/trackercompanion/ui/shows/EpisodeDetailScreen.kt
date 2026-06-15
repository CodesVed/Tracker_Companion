package com.example.trackercompanion.ui.shows

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackercompanion.data.ShowData
import com.example.trackercompanion.data.WrestlerData
import com.example.trackercompanion.model.Match
import com.example.trackercompanion.model.PPVEvent
import com.example.trackercompanion.model.ShowEpisode
import com.example.trackercompanion.model.Wrestler
import com.example.trackercompanion.model.enums.Brand
import com.example.trackercompanion.model.enums.CardSlot
import com.example.trackercompanion.model.enums.Show
import com.example.trackercompanion.ui.theme.Blue
import com.example.trackercompanion.ui.theme.Gold
import com.example.trackercompanion.ui.theme.Green
import com.example.trackercompanion.ui.theme.Red

sealed class ShowSource {
    data class RegularShow(val episode: ShowEpisode) : ShowSource()
    data class PPV(val event: PPVEvent) : ShowSource()
}

@Composable
fun EpisodeDetailScreen(
    showSource: ShowSource,
    matches: List<Match>,
    wrestlers: List<Wrestler>,
    onMatchSaved: (Match) -> Unit,
    onMatchDeleted: (Match) -> Unit,
    onEpisodeEdited: (ShowEpisode) -> Unit,
    onPPVEdited: (PPVEvent) -> Unit,
    onBackClick: () -> Unit
) {
    var showMatchEntry by remember { mutableStateOf(false) }
    var editingMatch    by remember { mutableStateOf<Match?>(null) }
    var showEditEpisode by remember { mutableStateOf(false) }

    val title = when (showSource) {
        is ShowSource.RegularShow -> {
            val brandLabel = when (showSource.episode.brand){
                Brand.RAW -> "RAW"
                Brand.SD -> "Smackdown"
                else -> showSource.episode.brand.name
            }
            "$brandLabel ${showSource.episode.episodeNumber}"
        }
        is ShowSource.PPV -> showSource.event.name
    }

    val subtitle = when (showSource) {
        is ShowSource.RegularShow -> "Week ${showSource.episode.weekNumber}"
        is ShowSource.PPV -> "PPV #${showSource.event.ppvNumber}"
    }

    val themeNote = when (showSource) {
        is ShowSource.RegularShow -> showSource.episode.notes
        is ShowSource.PPV -> showSource.event.notes
    }

    val brandColor = when (showSource) {
        is ShowSource.RegularShow -> when (showSource.episode.brand) {
            Brand.RAW -> Red
            Brand.SD -> Blue
            else -> Gold
        }
        is ShowSource.PPV -> Gold
    }

    val isPPV = showSource is ShowSource.PPV

    val slotOrder = listOf(CardSlot.OPEN, CardSlot.MID, CardSlot.UPPER, CardSlot.MAIN)
    val sortedMatches =  matches.sortedBy { slotOrder.indexOf(it.slot) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp).clickable { onBackClick() },
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )

                    Text(
                        modifier = Modifier.weight(1f),
                        text = title,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    IconButton(
                        onClick = {
                            showEditEpisode = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit episode",
                            tint = brandColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = brandColor.copy(alpha = 0.08f)
                    ),
                    border = BorderStroke(1.dp, brandColor.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = if (isPPV) Icons.Default.Bolt else Icons.Default.LiveTv,
                            contentDescription = null,
                            tint = brandColor
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = brandColor
                            )

                            Text(
                                text = subtitle,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            if (themeNote.isNotBlank()) {
                                Text(
                                    text = themeNote,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = matches.size.toString(),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = brandColor
                            )

                            Text(
                                text = if (matches.size == 1) "match" else "matches",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Match Card",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "${matches.size} ${if (matches.size == 1) "match" else "matches"}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (sortedMatches.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No matches on this card.\nTap + to add a match.",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(sortedMatches, key = { it.id }) { match ->
                    MatchCardRow(
                        match = match,
                        onLongPress = {
                            editingMatch = match
                            showMatchEntry = true
                        }
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }

        FloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            onClick = { showMatchEntry = true }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                Text(text = "Add Match", fontSize = 16.sp)
            }
        }
    }

    if (showMatchEntry) {
        val showId = when (showSource) {
            is ShowSource.RegularShow -> showSource.episode.id
            is ShowSource.PPV -> showSource.event.id
        }
        val showType = when (showSource) {
            is ShowSource.RegularShow -> Show.SHOW
            is ShowSource.PPV -> Show.PPV
        }

        MatchEntryBottomSheet(
            showId = showId,
            showType = showType,
            wrestlers = wrestlers,
            existingMatchCount = matches.size,
            editingMatch = editingMatch,
            onSave = { savedMatch ->
                onMatchSaved(savedMatch)
                showMatchEntry = false
                editingMatch = null
            },
            onDismiss = {
                showMatchEntry = false
                editingMatch = null
            },
            onDelete = { matchToDelete ->
                onMatchDeleted(matchToDelete)
                showMatchEntry = false
                editingMatch = null
            }
        )
    }

    if (showEditEpisode) {
        when (showSource) {
            is ShowSource.RegularShow -> {
                EditEpisodeBottomSheet(
                    episode = showSource.episode,
                    onSave = { edited ->
                        onEpisodeEdited(edited)
                        showEditEpisode = false
                    },
                    onDismiss = { showEditEpisode = false }
                )
            }
            is ShowSource.PPV -> {
                EditPPVBottomSheet(
                    ppv = showSource.event,
                    onSave = { edited ->
                        onPPVEdited(edited)
                        showEditEpisode = false
                    },
                    onDismiss = { showEditEpisode = false }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MatchCardRow(match: Match, onLongPress: () -> Unit = {}) {
    val slotColor = when (match.slot) {
        CardSlot.OPEN -> Blue.copy(alpha = 0.8f)
        CardSlot.MID -> Color(0xFF888888)
        CardSlot.UPPER -> Color(0xFFD4691E)
        CardSlot.MAIN -> Red
    }

    val slotLabel = when(match.slot) {
        CardSlot.OPEN -> "Open"
        CardSlot.MID -> "Mid"
        CardSlot.UPPER -> "Upper"
        CardSlot.MAIN -> "Main"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {},
                onLongClick = onLongPress
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(width = 6.dp, height = 72.dp)
            ) {
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    drawRect(color = slotColor)
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f).padding(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = MaterialTheme.shapes.extraSmall,
                        color = slotColor.copy(alpha = 0.15f),
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            text = slotLabel,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = slotColor
                        )
                    }

                    if (match.isTagMatch) {
                        Spacer(modifier = Modifier.width(6.dp))

                        Icon(
                            modifier = Modifier.size(14.dp),
                            imageVector = Icons.Default.Group,
                            contentDescription = "Tag Match",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = match.participants,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = match.stipulation,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(
                modifier = Modifier.padding(end = 12.dp),
                horizontalAlignment = Alignment.End
            ) {
                if (match.winnerLabel != null) {
                    Text(
                        text = "Winner",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = match.winnerLabel,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        color = Green,
                        maxLines = 2,
                    )
                } else {
                    Text(
                        text = "No result",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPPVBottomSheet(
    ppv: PPVEvent,
    onSave: (PPVEvent) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var name      by remember { mutableStateOf(ppv.name) }
    var notes     by remember { mutableStateOf(ppv.notes) }
    var nameError by remember { mutableStateOf(false) }

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Edit PPV",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            HorizontalDivider()

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = false
                },
                label = { Text("PPV Name") },
                isError = nameError,
                supportingText = {
                    if (nameError) Text("Name cannot be empty",
                        color = MaterialTheme.colorScheme.error)
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                minLines = 2,
                maxLines = 4,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                modifier = Modifier.fillMaxWidth().height(52.dp),
                onClick = {
                    if (name.isBlank()) { nameError = true; return@Button }
                    onSave(ppv.copy(name = name.trim(), notes = notes.trim()))
                }
            ) {
                Text("Save Changes", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EpisodeDetailPreview() {
    EpisodeDetailScreen(
        showSource = ShowSource.RegularShow(ShowData.episodes.first()),
        matches = ShowData.getMatchesForShow(1),
        wrestlers = WrestlerData.roster,
        onMatchSaved = {},
        onBackClick = {},
        onEpisodeEdited = {},
        onPPVEdited = {},
        onMatchDeleted = {}
    )
}